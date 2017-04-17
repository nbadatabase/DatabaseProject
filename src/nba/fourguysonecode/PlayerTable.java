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

    public static final String TableName = "PLAYERS";

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

            // Skip the first line which is just the format specifier for the CSV file.
            String line = br.readLine();
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
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
                    + "DOB VARCHAR(10),"
                    + "FOREIGN KEY (TEAM_ID) REFERENCES teams);" ;

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
                player_id, team_id, first_name.replace("\'", "\'\'"), last_name.replace("\'", "\'\'"), dob);
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
                    p.getPlayer_id(), p.getTeam_id(), p.getFirst_name().replace("\'", "\'\'"),
                    p.getLast_name().replace("\'", "\'\'"), p.getDob()));
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
        String query = "SELECT * FROM players " +
                "INNER JOIN playerstats ON players.player_id = playerstats.player_id " +
                "INNER JOIN teams ON players.team_id = teams.team_id";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            while(result.next()){
                System.out.println("*************************************************************");
                System.out.format("%-5s %-5s \nDOB: %-5s Team: %-5s \n"  +
                                  "GP: %-5d MIN: %-4.2f PTS: %-4.2f FGA: %-5d FGM: %-5d\n" +
                                  "3PA: %-5d 3PM: %-5d FTA: %-5d FTM: %-5d OREB: %-5d DREB: %-5d\n" +
                                  "AST: %-5d STL: %-5d BLK: %-5d TOV: %-5d\n",

                                  result.getString(3),  //First name
                                  result.getString(4),  //Last name
                                  result.getString(5),    //DOB
                                  result.getString(24), //Should be team name
                                              
                                  result.getInt(7),     //GP
                                  result.getFloat(8),     //MIN
                                  result.getFloat(9),     //PTS
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

    /**
     * Queries and print the table
     * @param conn
     */
    public static void printPlayerTableBasic(Connection conn){
        String query = "SELECT * FROM players";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            while(result.next()){
                System.out.println("*************************************");
                System.out.printf("%s  %s: DOB - %s\n",
                        result.getString(3),
                        result.getString(4),
                        result.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Queries and prints specific tables
     * @param conn
     * @param inp2
     */
    public static void printPlayerTableMulti(Connection conn, String[] inp2) {
        int s = inp2.length;
        for (int i = 0; i < s; i++) {
            String[] name = inp2[i].split("\\s");
            String query = "SELECT * FROM players " +
                    "INNER JOIN playerstats ON players.player_id = playerstats.player_id "+
                    "INNER JOIN teams ON players.team_id = teams.team_id " +
                    "WHERE players.first_name = \'"+ name[0] +"\' AND players.last_name = \'" + name[1] + "\'";
            try {
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(query);
                while(result.next()){
                    System.out.println("*************************************************************");
                    System.out.format("%-5s %-5s \nDOB: %-5s Team: %-5s \n"  +
                                    "GP: %-5d MIN: %-4.2f PTS: %-4.2f FGA: %-5d FGM: %-5d\n" +
                                    "3PA: %-5d 3PM: %-5d FTA: %-5d FTM: %-5d OREB: %-5d DREB: %-5d\n" +
                                    "AST: %-5d STL: %-5d BLK: %-5d TOV: %-5d\n",

                            result.getString(3),  //First name
                            result.getString(4),  //Last name
                            result.getString(5),    //DOB
                            result.getString(24), //Should be team name

                            result.getInt(7),     //GP
                            result.getFloat(8),     //MIN
                            result.getFloat(9),     //PTS
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

    /**
     * Queries and prints specific tables
     * @param conn
     * @param inp2
     */
    public static void printPlayerTableMultiBasic(Connection conn, String[] inp2) {
        int s = inp2.length;
        for (int i = 0; i < s; i++) {
            String[] name = inp2[i].split("\\s");
            String query = "SELECT * FROM players " +
                    "WHERE players.first_name = \'"+ name[0] +"\' AND players.last_name = \'" + name[1] + "\'";
            try {
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(query);
                while(result.next()){
                    System.out.println("*************************************************************");
                    System.out.format("%s %s: DOB: %-5s\n",
                            result.getString(3),  //First name
                            result.getString(4),  //Last name
                            result.getString(5));
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Queries and prints specific stats from table
     * @param conn
     * @param inp
     */
    public static void printPlayerStats(Connection conn, String inp){
        String query = "SELECT " + inp + ", first_name, last_name FROM playerstats " +
                "INNER JOIN players ON playerstats.player_id = players.player_id " +
                "ORDER BY playerstats."+ inp +" DESC";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            while(result.next()){
                System.out.println("*************************************");
                if(inp.equals("tot_pts") || inp.equals("tot_mins")){
                    System.out.printf("%s %s: %.2f \n",
                            result.getString(2),
                            result.getString(3),
                            result.getFloat(1));
                }
                else{
                    System.out.printf("%s %s: %d \n",
                            result.getString(2),
                            result.getString(3),
                            result.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Queries and prints specific stats to compare from table
     * @param conn
     * @param inp2
     */
    public static void printPlayerComp(Connection conn, String[] inp2){
        int s = inp2.length;
        ArrayList<ResultSet> results = new ArrayList<>();
        for (int i = 0; i < s; i++) {
            String[] name = inp2[i].split("\\s");
            String query = "SELECT * FROM players " +
                    "INNER JOIN playerstats ON players.player_id = playerstats.player_id "+
                    "INNER JOIN teams ON players.team_id = teams.team_id " +
                    "WHERE players.first_name = \'"+ name[0] +"\' AND players.last_name = \'" + name[1] + "\'";
            try {
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet result = stmt.executeQuery(query);
                results.add(result);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        int pl = results.size();
        System.out.println("*************************************************************");
        System.out.println("Comparison of " + pl + " players:");
        System.out.println("- Stat rank shown in parentheses -");
        System.out.println("*************************************************************");
        for(int j = 0; j < pl; j++){
            try {
                results.get(j).first();
                System.out.format("%-5s %-5s \n" +
                                "GP: %-5d (%d) MIN per Game: %-4.2f (%d) PTS per Game: %-4.2f (%d) FGA: %-5d (%d) " +
                                "FGM: %-5d (%d)\n" +
                                "3PA: %-5d (%d) 3PM: %-5d (%d) FTA: %-5d (%d) FTM: %-5d (%d) OREB: %-5d (%d) " +
                                "DREB: %-5d (%d)\n" +
                                "AST: %-5d (%d) STL: %-5d (%d) BLK: %-5d (%d) TOV: %-5d (%d)\n",

                        results.get(j).getString(3),  //First name
                        results.get(j).getString(4),  //Last name

                        results.get(j).getInt(7),     //GP
                        rank(results, j, 7),
                        results.get(j).getFloat(8)/results.get(j).getFloat(7),     //MIN
                        rank(results, j, 8),
                        results.get(j).getFloat(9)/results.get(j).getFloat(7),     //PTS
                        rank(results, j, 9),
                        results.get(j).getInt(10),    //FGA
                        rank(results, j, 10),
                        results.get(j).getInt(11),    //FG
                        rank(results, j, 11),

                        results.get(j).getInt(12),    //3PA
                        rank(results, j, 12),
                        results.get(j).getInt(13),    //3PM
                        rank(results, j, 13),
                        results.get(j).getInt(14),    //FTA
                        rank(results, j, 14),
                        results.get(j).getInt(15),    //FTM
                        rank(results, j, 15),
                        results.get(j).getInt(16),    //OREB
                        rank(results, j, 16),
                        results.get(j).getInt(17),    //DREB
                        rank(results, j, 17),

                        results.get(j).getInt(18),    //AST
                        rank(results, j, 18),
                        results.get(j).getInt(19),    //STL
                        rank(results, j, 19),
                        results.get(j).getInt(20),    //BLK
                        rank(results, j, 20),
                        results.get(j).getInt(21),     //TOV
                        rank(results, j, 21));
                System.out.println("-----");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Ranks stat
     * @param r
     * @param num_in_list
     * @param stat
     * @return
     */
    public static int rank(ArrayList<ResultSet> r, Integer num_in_list, Integer stat){
        int size = r.size();
        int rank = 1;
        int s1 = 0;
        float s2 = 0;
        try {
            r.get(num_in_list).first();
            if (stat == 8 || stat == 9) {
                s2 = r.get(num_in_list).getFloat(stat);
            } else {
                s1 = r.get(num_in_list).getInt(stat);
            }
            for (int i = 0; i < size; i++) {
                if (i != num_in_list) {
                    r.get(i).first();
                    if (stat == 8 || stat == 9) {
                        if (r.get(i).getFloat(stat) > s2) {
                            rank++;
                        }
                    } else {
                        if (r.get(i).getInt(stat) > s1) {
                            rank++;
                        }
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return rank;
    }
}
