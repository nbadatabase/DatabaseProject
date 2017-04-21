package nba.fourguysonecode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import nba.fourguysonecode.objects.PlayerStats;

/**
 * Class to make and manipulate the playerstats table
 * @author joshuasellers
 * Created on 4/3/17.
 */
public class PlayerStatsTable {

    public static final String TableName = "PLAYERSTATS";

    /**
     * Reads a cvs file for data and adds them to the playerstats table
     *
     * Does not create the table. It must already be created
     *
     * @param conn: database connection to work with
     * @param fileName
     * @throws SQLException
     */

    public static void populatePlayerStatsTableFromCSV(Connection conn,
                                                  String fileName)
            throws SQLException{
        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<PlayerStats> playerstats = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            // Skip the first line which is just the format specifier for the CSV file.
            String line = br.readLine();
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                playerstats.add(new PlayerStats(split));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Creates the SQL query to do a bulk add of all playerstats
         * that were read in. This is more efficient then adding one
         * at a time
         */
        String sql = createPlayerStatsInsertSQL(playerstats);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    /**
     * Create the playerstats table with the given attributes
     *
     * @param conn: the database connection to work with
     */
    public static void createPlayerStatsTable(Connection conn){
        try {
            String query = "CREATE TABLE IF NOT EXISTS playerstats("
                    + "PLAYER_ID INT PRIMARY KEY auto_increment,"
                    + "GAMES_PLAYED INT,"
                    + "TOT_MINS FLOAT,"
                    + "TOT_PTS FLOAT,"
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
     * Adds a single playerstats to the database
     *
     * @param conn
     * @param player_id
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
    public static void addPlayerStats(Connection conn,
                                 int player_id,
                                 int games_played,
                                 float tot_mins,
                                 float tot_pts,
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
        String query = String.format("INSERT INTO playerstats "
                        + "VALUES(%d, %d, %f, %f, %d, %d, " +
                        "%d, %d, %d, %d, %d, %d, %d, %d, %d, %d);",
                player_id, games_played, tot_mins, tot_pts, fg_att, fg_made,
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
     * This creates an sql statement to do a bulk add of playerstats
     *
     * @param playerstats: list of PlayerStats objects to add
     *
     * @return
     */
    public static String createPlayerStatsInsertSQL(ArrayList<PlayerStats> playerstats){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO playerstats (PLAYER_ID, GAMES_PLAYED, TOT_MINS," +
                "TOT_PTS, FG_ATT, FG_MADE, THREE_ATT, THREE_MADE, FREE_ATT, FREE_MADE, OFF_REBOUND," +
                "DEF_REBOUND, ASSISTS, STEALS, BLOCKS, TURNOVERS) VALUES");

        /**
         * For each playerstats append a tuple
         *
         * If it is not the last playerstats add a comma to seperate
         *
         * If it is the last playerstats add a semi-colon to end the statement
         */
        for(int i = 0; i < playerstats.size(); i++){
            PlayerStats ps = playerstats.get(i);
            sb.append(String.format("(%d, %d, %f, %f, %d, %d, %d, " +
                            "%d, %d, %d, %d, %d, %d, %d, %d, %d)",
                    ps.getPlayer_id(), ps.getGames_played(), ps.getTot_mins(), ps.getTot_pts(), ps.getFg_att(),
                    ps.getFg_made(), ps.getThree_att(), ps.getThree_made(), ps.getFree_att(), ps.getFree_made(),
                    ps.getOff_rebound(), ps.getDef_rebound(), ps.getAssists(), ps.getSteals(), ps.getBlocks(),
                    ps.getTurnovers()));
            if( i != playerstats.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }

        return sb.toString();
    }

    /**
     * Makes a query to the playerstats table
     * with given columns and conditions
     *
     * @param conn
     * @param columns: columns to return
     * @param whereClauses: conditions to limit query by
     * @return
     */
    public static ResultSet queryPlayerStatsTable(Connection conn,
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
        sb.append("FROM playerstats ");

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
    public static void printPlayerStatsTable(Connection conn){
        String query = "SELECT * FROM playerstats;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("Player %d: %d %f %f %d %d %d %d %d %d %d %d %d %d %d %d\n",
                        result.getInt(1),
                        result.getInt(2),
                        result.getFloat(3),
                        result.getFloat(4),
                        result.getInt(5),
                        result.getInt(6),
                        result.getInt(7),
                        result.getInt(8),
                        result.getInt(9),
                        result.getInt(10),
                        result.getInt(11),
                        result.getInt(12),
                        result.getInt(13),
                        result.getInt(14),
                        result.getInt(15),
                        result.getInt(16));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}