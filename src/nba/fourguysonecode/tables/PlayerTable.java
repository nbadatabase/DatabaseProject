package nba.fourguysonecode.tables;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

import nba.fourguysonecode.objects.Player;

/**
 * Class to make and manipulate the player table
 * @author joshuasellers
 * Created on 4/2/17.
 */
public class PlayerTable extends DatabaseTable
{

    public static final String TableName = "players";

    /**
     * Reads a cvs file for data and adds them to the player table
     *
     * Does not create the table. It must already be created
     *
     * @param conn: database connection to work with
     * @param fileName the name of the csv file to read
     * @throws SQLException
     */
    public static void populatePlayerTableFromCSV(Connection conn,
                                                  String fileName)
            throws SQLException{
        /*
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
        } catch (Exception e){
            e.printStackTrace();
        }

        /*
         * Creates the SQL query to do a bulk add of all players
         * that were read in. This is more efficient then adding one
         * at a time
         */
        String sql = createPlayerInsertSQL(players);

        /*
         * Create and execute a SQL statement
         *
         * execute only returns if it was successful
         */
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (Exception e) {
            // Check if insert error was on duplicate.
            if ( e.getMessage().contains("FN_LN_UNQ")) {
                System.err.println("There is a duplicate player in the file. No players were added.");
            } else {
                e.printStackTrace();
            }
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
                    + "PLAYER_ID INT PRIMARY KEY auto_increment,"
                    + "TEAM_ID INT,"
                    + "FIRST_NAME VARCHAR(255),"
                    + "LAST_NAME VARCHAR(255),"
                    + "DOB VARCHAR(10),"
                    + "FOREIGN KEY (TEAM_ID) REFERENCES teams);"
                    + "CREATE UNIQUE INDEX FN_LN_UNQ ON players(FIRST_NAME, LAST_NAME);";

            /*
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
     * @param conn The database to use
     * @param player_id The ID of the player
     * @param team_id The ID of the team
     * @param first_name The players first name
     * @param last_name The players last name
     * @param dob The date of birth
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
     * @param players list of Player objects to add
     *
     * @return An SQL INSERT string containing all players to insert.
     */
    public static String createPlayerInsertSQL(ArrayList<Player> players){
        StringBuilder sb = new StringBuilder();

        /*
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO players (PLAYER_ID, TEAM_ID, FIRST_NAME, LAST_NAME, DOB) VALUES");

        /*
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
     * @param conn The database to use
     * @param columns: columns to return
     * @param whereClauses: conditions to limit query by
     * @return The result of the query, or NULL on failure
     */
    public static List<Player> queryPlayerTable(Connection conn,
                                             ArrayList<String> columns,
                                             ArrayList<String> whereClauses)
    {
        // Query the database for all matching results.
        ResultSet results = PlayerTable.queryTable(conn, PlayerTable.TableName, columns, whereClauses);

        // Create a list to hold all of the Player objects.
        List<Player> players = new ArrayList<>();

        try
        {
            // Loop through all of the results and create a Player object for each one.
            while (results.next())
            {
                // Create a new Player object and add it to the list.
                players.add(new Player(results));
            }
        }
        catch (SQLException e)
        {
            // An error occurred while processing the results, print the stack trace.
            e.printStackTrace();
        }

        // Return the player list.
        return players;
    }
    /**
     * Queries and print the table
     * @param conn The database to use
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
     * @param conn The database to use
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
     * @param conn The database to use
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
     * @param conn The database to use
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
     * @param conn The database to use
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
     * @param conn The database to use
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

    public static void insertPlayer(Connection conn, ArrayList<String> data){

        String query1 = "INSERT INTO players(team_id, first_name, last_name, dob) VALUES ("+data.get(1)+", \'" + data.get(2)+"\', \'" +
                data.get(3) + "\', \'" + data.get(4) + "\')";
        try {
            // Execute the insert statement for creating hte player row.
            Statement stmt1 = conn.createStatement();
            
            stmt1.executeUpdate(query1);
            // Get the result set of generated primary keys for the player set.
            ResultSet set = stmt1.getGeneratedKeys();
            if (!set.next())
            {
                // There was no primary key generated for the insert, something went wrong.
                System.out.println("Failed to insert new player!");
                return;
            }

            // Get the player id from the result set.
            long playerId = set.getLong(1);

            // Create the player stats row based on the primary key from the player insert.
            String query2 = "INSERT INTO playerstats VALUES ("+ playerId +", "+ data.get(5)+", "+ data.get(6)+", "
                    + data.get(7)+", "+ data.get(8)+", "+ data.get(9)+", "+ data.get(10)+", "+ data.get(11)+", "+ data.get(12)
                    +", "+ data.get(13)+", "+ data.get(14)+", "+ data.get(15)+", "+ data.get(16)+", "+ data.get(17)+", "+
                    data.get(18)+", "+data.get(19)+")";
            Statement stmt2 = conn.createStatement();
            stmt2.executeUpdate(query2);
        }
        catch (SQLException e) {
            System.err.println("There was an error with your player data.");

            // Check if insert error was on duplicate.
            if ( e.getMessage().contains("FN_LN_UNQ")) {
                System.err.println("This player already exists.");
            } else {
                e.printStackTrace();
            }
        }
    }
}
