package nba.fourguysonecode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import nba.fourguysonecode.objects.Team;

/**
 * Class to make and manipulate the team table
 * @author joshuasellers
 * Created by joshuasellers on 4/2/17.
 */
public class TeamTable {
    /**
     * Reads a cvs file for data and adds them to the team table
     *
     * Does not create the table. It must already be created
     *
     * @param conn: database connection to work with
     * @param fileName
     * @throws SQLException
     */

    public static void populateTeamTableFromCSV(Connection conn, String fileName)
            throws SQLException{
        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<Team> teams = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                teams.add(new Team(Integer.getInteger(split[0]),
                        Integer.getInteger(split[1]),
                        split[2],
                        split[3],
                        Integer.getInteger(split[4]),
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
                        Integer.getInteger(split[18])));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Creates the SQL query to do a bulk add of all teams
         * that were read in. This is more efficient then adding one
         * at a time
         */
        String sql = createTeamInsertSQL(teams);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    /**
     * Create the team table with the given attributes
     *
     * @param conn: the database connection to work with
     */
    public static void createTeamTable(Connection conn){
        try {
            String query = "CREATE TABLE IF NOT EXISTS teams("
                    + "TEAM_ID INT PRIMARY KEY,"
                    + "DIV_ID INT FOREIGN KEY,"
                    + "TEAM_NAME VARCHAR(255),"
                    + "LOCATION VARCHAR(255),"
                    + "GAMES_WON INT,"
                    + "GAMES_LOST INT,"
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
     * Adds a single team to the database
     *
     * @param conn
     * @param team_id
     * @param div_id
     * @param team_name
     * @param location
     * @param games_won
     * @param games_lost
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
    public static void addTeam(Connection conn, int team_id, int div_id, String team_name, String location,
                               int games_won, int games_lost, int tot_pts, int fg_att,
                               int fg_made, int three_att, int three_made, int free_att,
                               int free_made, int off_rebound, int def_rebound, int assists,
                               int steals, int blocks, int turnovers){

        /**
         * SQL insert statement
         */
        String query = String.format("INSERT INTO teams "
                        + "VALUES(%d, %d,\'%s\',\'%s\', %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d);",
                team_id, div_id, team_name, location, games_won, games_lost, tot_pts, fg_att, fg_made, three_att,
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
     * This creates an sql statement to do a bulk add of teams
     *
     * @param teams: list of Team objects to add
     *
     * @return
     */
    public static String createTeamInsertSQL(ArrayList<Team> teams){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO teams (TEAM_ID, DIV_ID, TEAM_NAME, LOCATION, GAMES_WON, GAMES_LOST, TOT_PTS," +
                "FG_ATT, FG_MADE, THREE_ATT, THREE_MADE, FREE_ATT, FREE_MADE, OFF_REBOUND, DEF_REBOUND," +
                "ASSISTS, STEALS, BLOCKS, TURNOVERS) VALUES");

        /**
         * For each team append a tuple
         *
         * If it is not the last team add a comma to seperate
         *
         * If it is the last team add a semi-colon to end the statement
         */
        for(int i = 0; i < teams.size(); i++){
            Team t = teams.get(i);
            sb.append(String.format("(%d, %d,\'%s\',\'%s\', %d, %d, %d, %d, %d," +
                            " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d)",
                    t.getTeam_id(), t.getDiv_id(), t.getTeam_name(), t.getLocation(), t.getGames_won(),
                    t.getGames_lost(), t.getTot_pts(), t.getFg_att(), t.getFg_made(), t.getThree_att(),
                    t.getThree_made(), t.getFree_att(), t.getFree_made(), t.getOff_rebound(), t.getDef_rebound(),
                    t.getAssists(), t.getSteals(), t.getBlocks(), t.getTurnovers()));
            if( i != teams.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }

        return sb.toString();
    }

    /**
     * Makes a query to the team table
     * with given columns and conditions
     *
     * @param conn
     * @param columns: columns to return
     * @param whereClauses: conditions to limit query by
     * @return
     */
    public static ResultSet queryTeamTable(Connection conn,
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
        sb.append("FROM teams ");

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
