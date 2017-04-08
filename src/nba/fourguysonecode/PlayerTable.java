package nba.fourguysonecode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;

import nba.fourguysonecode.objects.Player;

/**
 * Class to make and manipulate the player table
 * @author joshuasellers
 * Created on 4/2/17.
 */
public class PlayerTable {
    /**
     * Reads a cvs file for data and adds them to the player table
     *
     * Does not create the table. It must already be created
     *
     * @param conn: database connection to work with
     * @param fileName
     * @throws SQLException
     */

    public static void populatePlayerTableFromCSV(Connection conn,
                                                  String fileName)
            throws SQLException{
        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<Player> players = new ArrayList<Player>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                players.add(new Player(split));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        /**
         * Creates the SQL query to do a bulk add of all players
         * that were read in. This is more efficient then adding one
         * at a time
         */
        String sql = createPlayerInsertSQL(players);

        /**
         * Create and execute a SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    /**
     * Create the player table with the given attributes
     *
     * @param conn: the database connection to work with
     */
    public static void createPlayerTable(Connection conn){
        try {
            String query = "CREATE TABLE IF NOT EXISTS players("
                    + "PLAYER_ID INT PRIMARY KEY,"
                    + "TEAM_ID INT,"
                    + "FIRST_NAME VARCHAR(255),"
                    + "LAST_NAME VARCHAR(255),"
                    + "DOB VARCHAR(8),"
                    + ");" ;

            /**
             * Create a query and execute
             */
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a single player to the database
     *
     * @param conn
     * @param player_id
     * @param team_id
     * @param first_name
     * @param last_name
     * @param dob
     */
    public static void addPlayer(Connection conn,
                                 int player_id,
                                 int team_id,
                                 String first_name,
                                 String last_name,
                                 String dob){

        /*
         * SQL insert statement
         */
        String query = String.format("INSERT INTO players "
                        + "VALUES(%d, %d,\'%s\',\'%s\',\'%s\');",
                player_id, team_id, first_name, last_name, dob);
        try {
            /*
             * create and execute the query
             */
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * This creates an sql statement to do a bulk add of players
     *
     * @param players: list of Player objects to add
     *
     * @return
     */
    public static String createPlayerInsertSQL(ArrayList<Player> players){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO players (PLAYER_ID, TEAM_ID, FIRST_NAME, LAST_NAME, DOB) VALUES");

        /**
         * For each player append a tuple
         *
         * If it is not the last player add a comma to seperate
         *
         * If it is the last player add a semi-colon to end the statement
         */
        for(int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            sb.append(String.format("(%d, %d,\'%s\',\'%s\',\'%s\')",
                    p.getPlayer_id(), p.getTeam_id(), p.getFirst_name(), p.getLast_name(), p.getDob()));
            if( i != players.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }

        return sb.toString();
    }

    /**
     * Makes a query to the player table
     * with given columns and conditions
     *
     * @param conn
     * @param columns: columns to return
     * @param whereClauses: conditions to limit query by
     * @return
     */
    public static ResultSet queryPlayerTable(Connection conn,
                                             ArrayList<String> columns,
                                             ArrayList<String> whereClauses){
        StringBuilder sb = new StringBuilder();

        /**
         * Start the select query
         */
        sb.append("SELECT ");

        /**
         * If we gave no columns just give them all to us
         *
         * other wise add the columns to the query
         * adding a comma top seperate
         */
        if(columns.isEmpty()){
            sb.append("* ");
        }
        else{
            for(int i = 0; i < columns.size(); i++){
                if(i != columns.size() - 1){
                    sb.append(columns.get(i) + ", ");
                }
                else{
                    sb.append(columns.get(i) + " ");
                }
            }
        }

        /**
         * Tells it which table to get the data from
         */
        sb.append("FROM players ");

        /**
         * If we gave it conditions append them
         * place an AND between them
         */
        if(!whereClauses.isEmpty()){
            sb.append("WHERE ");
            for(int i = 0; i < whereClauses.size(); i++){
                if(i != whereClauses.size() -1){
                    sb.append(whereClauses.get(i) + " AND ");
                }
                else{
                    sb.append(whereClauses.get(i));
                }
            }
        }

        /**
         * close with semi-colon
         */
        sb.append(";");

        //Print it out to verify it made it right
        System.out.println("Query: " + sb.toString());
        try {
            /**
             * Execute the query and return the result set
             */
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Queries and print the table
     * @param conn
     */
    public static void printPlayerTable(Connection conn)
    {
        String query = "SELECT * FROM Player " +
                "INNER JOIN playerstats ON player.player_id = playerstats.player_id " +
                "INNER JOIN Team ON player.team_id = team.team_id";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.println("Name:   DOB:   Team:\n  GP:  MIN:  PTS:  FGA: FGM: \n" +
                    "3PA: 3PM: FTA: FTM: OREB: DREB: \nAST: STL: BLK: TOV:");
            while(result.next()){
                System.out.format("%-5s %-5s %-5s %-5s \n"  +           //NAME;DOB;TEAM
                                  "%-5d %-5d %-5d %-5d %-5d\n" +        //GP:  MIN:  PTS:  FGA: FGM:
                                  "%-5d %-5d %-5d %-5d %-5d %-5d\n" +   //3PA: 3PM: FTA: FTM: OREB: DREB:
                                  "%-5d %-5d %-5d %-5d\n",              //AST: STL: BLK: TOV:

                                  result.getString(3),  //First name
                                  result.getString(4),  //Last name
                                  result.getDate(5),    //DOB
                                  result.getString(24), //Should be team name
                                              
                                  result.getInt(7),     //GP
                                  result.getInt(8),     //MIN
                                  result.getInt(9),     //PTS
                                  result.getInt(10),    //FGA
                                  result.getInt(11),    //FGM
                                    
                                  result.getInt(12),    //3PA
                                  result.getInt(13),    //3PM
                                  result.getInt(14),    //FTA
                                  result.getInt(15),    //FTM
                                  result.getInt(16),    //OREB
                                  result.getInt(17),    //DREB
                                  
                                  result.getInt(18),    //AST
                                  result.getInt(19),    //STL
                                  result.getInt(20),    //BLK
                                  result.getInt(21)     //TOV
                );


            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
