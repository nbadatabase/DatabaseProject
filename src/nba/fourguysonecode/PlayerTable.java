package nba.fourguysonecode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
        ArrayList<Player> players = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                players.add(new Player(Integer.getInteger(split[0]),
                        Integer.getInteger(split[1]),
                        split[2],
                        split[3],
                        split[4],
                        Integer.getInteger(split[5]),
                        Integer.getInteger(split[6]),
                        Integer.getInteger(split[7]),
                        Integer.getInteger(split[8]),
                        Integer.getInteger(split[9]),
                        Integer.getInteger(split[10]),
                        Integer.getInteger(split[11]),
                        Integer.getInteger(split[12]),
                        Integer.getInteger(split[13]),
                        Integer.getInteger(split[14]),
                        Integer.getInteger(split[15]),
                        Integer.getInteger(split[16]),
                        Integer.getInteger(split[17]),
                        Integer.getInteger(split[18]),
                        Integer.getInteger(split[19])));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Creates the SQL query to do a bulk add of all players
         * that were read in. This is more efficient then adding one
         * at a time
         */
        String sql = createPlayerInsertSQL(players);

        /**
         * Create and execute an SQL statement
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
                    + "TEAM_ID INT FOREIGN KEY,"
                    + "FIRST_NAME VARCHAR(255),"
                    + "LAST_NAME VARCHAR(255),"
                    + "GAMES_PLAYED INT,"
                    + "DOB DATE"
                    + "TOT_MINS INT,"
                    + "TOT_PTS INT,"
                    + "FG_ATT INT,"
                    + "FG_MADE INT,"
                    + "THREE_ATT INT,"
                    + "THREE_MADE INT,"
                    + "FREE_ATT INT,"
                    + "FREE_MADE INT,"
                    + "OFF_REBOUND INT,"
                    + "DEF_REBOUND INT,"
                    + "ASSISTS INT,"
                    + "STEALS INT,"
                    + "BLOCKS INT,"
                    + "TURNOVERS INT,"
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
     * @param games_played
     * @param tot_mins
     * @param fg_att
     * @param fg_made
     * @param three_att
     * @param three_made
     * @param free_att
     * @param free_made
     * @param off_rebound
     * @param def_rebound
     * @param assists
     * @param steals
     * @param blocks
     * @param turnovers
     */
    public static void addPlayer(Connection conn,
                                 int player_id,
                                 int team_id,
                                 String first_name,
                                 String last_name,
                                 String dob,
                                 int games_played,
                                 int tot_mins,
                                 int tot_pts,
                                 int fg_att,
                                 int fg_made,
                                 int three_att,
                                 int three_made,
                                 int free_att,
                                 int free_made,
                                 int off_rebound,
                                 int def_rebound,
                                 int assists,
                                 int steals,
                                 int blocks,
                                 int turnovers){

        /**
         * SQL insert statement
         */
        String query = String.format("INSERT INTO players "
                        + "VALUES(%d, %d,\'%s\',\'%s\',\'%s\', %d, %d, %d, %d, %d, " +
                        "%d, %d, %d, %d, %d, %d, %d, %d, %d, %d);",
                player_id, team_id, first_name, last_name, dob, games_played, tot_mins, tot_pts, fg_att, fg_made,
                three_att, three_made, free_att, free_made, off_rebound,
                def_rebound, assists, steals, blocks, turnovers);
        try {
            /**
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
        sb.append("INSERT INTO players (PLAYER_ID, TEAM_ID, FIRST_NAME, LAST_NAME, DOB, GAMES_PLAYED, TOT_MINS," +
                "TOT_PTS, FG_ATT, FG_MADE, THREE_ATT, THREE_MADE, FREE_ATT, FREE_MADE, OFF_REBOUND," +
                "DEF_REBOUND, ASSISTS, STEALS, BLOCKS, TURNOVERS) VALUES");

        /**
         * For each player append a tuple
         *
         * If it is not the last player add a comma to seperate
         *
         * If it is the last player add a semi-colon to end the statement
         */
        for(int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            sb.append(String.format("(%d, %d,\'%s\',\'%s\',\'%s\', %d, %d, %d, %d, %d, " +
                            "%d, %d, %d, %d, %d, %d, %d, %d, %d, %d)",
                    p.getPlayer_id(), p.getTeam_id(), p.getFirst_name(), p.getLast_name(), p.getDob(),
                    p.getGames_played(), p.getTot_mins(), p.getTot_pts(), p.getFg_att(), p.getFg_made(),
                    p.getThree_att(), p.getThree_made(), p.getFree_att(), p.getFree_made(), p.getOff_rebound(),
                    p.getDef_rebound(), p.getAssists(), p.getSteals(), p.getBlocks(), p.getTurnovers()));
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
}
