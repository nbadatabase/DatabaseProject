package nba.fourguysonecode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import nba.fourguysonecode.objects.TeamStats;

/**
 * Class to make and manipulate the teamstats table
 * @author joshuasellers
 * Created by joshuasellers on 4/3/17.
 */
public class TeamStatsTable {
    /**
     * Reads a cvs file for data and adds them to the teamstats table
     *
     * Does not create the table. It must already be created
     *
     * @param conn: database connection to work with
     * @param fileName
     * @throws SQLException
     */

    public static void populateTeamStatsTableFromCSV(Connection conn, String fileName)
            throws SQLException{
        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<TeamStats> teamstats = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                teamstats.add(new TeamStats(split));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Creates the SQL query to do a bulk add of all teamstats
         * that were read in. This is more efficient then adding one
         * at a time
         */
        String sql = createTeamStatsInsertSQL(teamstats);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    /**
     * Create the teamstats table with the given attributes
     *
     * @param conn: the database connection to work with
     */
    public static void createTeamStatsTable(Connection conn){
        try {
            String query = "CREATE TABLE IF NOT EXISTS teamstats("
                    + "TEAM_ID INT PRIMARY KEY,"
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
     * Adds a single teamstats to the database
     *
     * @param conn
     * @param team_id
     * @param tot_pts
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
    public static void addTeamStats(Connection conn, int team_id, float tot_pts, int fg_att,
                               int fg_made, int three_att, int three_made, int free_att, int free_made, int off_rebound,
                               int def_rebound, int assists, int steals, int blocks, int turnovers){

        /**
         * SQL insert statement
         */
        String query = String.format("INSERT INTO teamstats "
                        + "VALUES(%d, %f, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d);",
                team_id, tot_pts, fg_att, fg_made, three_att,
                three_made, free_att, free_made, off_rebound, def_rebound, assists, steals, blocks, turnovers);
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
     * This creates an sql statement to do a bulk add of teamstats
     *
     * @param teamstats: list of TeamStats objects to add
     *
     * @return
     */
    public static String createTeamStatsInsertSQL(ArrayList<TeamStats> teamstats){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO teamstats (TEAM_ID, TOT_PTS," +
                "FG_ATT, FG_MADE, THREE_ATT, THREE_MADE, FREE_ATT, FREE_MADE, OFF_REBOUND, DEF_REBOUND," +
                "ASSISTS, STEALS, BLOCKS, TURNOVERS) VALUES");

        /**
         * For each teamstats append a tuple
         *
         * If it is not the last teamstats add a comma to seperate
         *
         * If it is the last teamstats add a semi-colon to end the statement
         */
        for(int i = 0; i < teamstats.size(); i++){
            TeamStats ts = teamstats.get(i);
            sb.append(String.format("(%d, %f, %d, %d, %d, %d, %d," +
                            " %d, %d, %d, %d, %d, %d, %d)",
                    ts.getTeam_id(), ts.getTot_pts(), ts.getFg_att(),
                    ts.getFg_made(), ts.getThree_att(), ts.getThree_made(), ts.getFree_att(), ts.getFree_made(),
                    ts.getOff_rebound(), ts.getDef_rebound(), ts.getAssists(), ts.getSteals(), ts.getBlocks(),
                    ts.getTurnovers()));
            if( i != teamstats.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }

        return sb.toString();
    }

    /**
     * Makes a query to the teamstats table
     * with given columns and conditions
     *
     * @param conn
     * @param columns: columns to return
     * @param whereClauses: conditions to limit query by
     * @return
     */
    public static ResultSet queryTeamStatsTable(Connection conn,
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
        sb.append("FROM teamstats ");

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
    public static void printTeamStatsTable(Connection conn){
        String query = "SELECT * FROM teamstats;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("TeamStats %d: %f %d %d %d %d %d %d %d %d %d %d %d %d\n",
                        result.getInt(1),
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
