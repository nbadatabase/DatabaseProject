package nba.fourguysonecode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import nba.fourguysonecode.objects.Conference;

import javax.xml.transform.Result;

/**
 * Class to make and manipulate the conference table
 * @author joshuasellers
 * Created by joshuasellers on 4/2/17.
 */
public class ConferenceTable {

    public static final String TableName = "CONFERENCES";

    /**
     * Reads a cvs file for data and adds them to the conference table
     *
     * Does not create the table. It must already be created
     *
     * @param conn: database connection to work with
     * @param fileName
     * @throws SQLException
     */

    public static void populateConferenceTableFromCSV(Connection conn, String fileName)
            throws SQLException{
        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<Conference> conferences = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                conferences.add(new Conference(split));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Creates the SQL query to do a bulk add of all conferences
         * that were read in. This is more efficient then adding one
         * at a time
         */
        String sql = createConferencesInsertSQL(conferences);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    /**
     * Create the conference table with the given attributes
     *
     * @param conn: the database connection to work with
     */
    public static void createConferenceTable(Connection conn){
        try {
            String query = "CREATE TABLE IF NOT EXISTS conferences("
                    + "CONF_ID INT PRIMARY KEY,"
                    + "CONF_NAME VARCHAR(255),"
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
     * Adds a single conference to the database
     *
     * @param conn
     * @param conf_id
     * @param conf_name
     */
    public static void addConference(Connection conn, int conf_id, String conf_name){

        /**
         * SQL insert statement
         */
        String query = String.format("INSERT INTO conferences "
                        + "VALUES(%d,\'%s\');",
                conf_id, conf_name);
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
     * This creates an sql statement to do a bulk add of conferences
     *
     * @param conferences: list of Conference objects to add
     *
     * @return
     */
    public static String createConferencesInsertSQL(ArrayList<Conference> conferences){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO conferences (CONF_ID, CONF_NAME) VALUES");

        /**
         * For each conference append a tuple
         *
         * If it is not the last conference add a comma to seperate
         *
         * If it is the last conference add a semi-colon to end the statement
         */
        for(int i = 0; i < conferences.size(); i++){
            Conference c = conferences.get(i);
            sb.append(String.format("(%d,\'%s\')",
                    c.getConf_id(), c.getConf_name()));
            if( i != conferences.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }

        return sb.toString();
    }
    /*
     * Makes a query to the conference table
     * with given columns and conditions
     *
     * @param conn
     * @param columns: columns to return
     * @param whereClauses: conditions to limit query by
     * @return
     */

    public static ResultSet queryConferenceTable(Connection conn,
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
        sb.append("FROM conferences ");

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
    public static void printConferenceTable(Connection conn){
        String query = "SELECT * FROM conferences " +
                "INNER JOIN divisions ON divisions.conf_id = conferences.conf_id " +
                "INNER JOIN teams ON teams.div_id = divisions.div_id " +
                "WHERE conferences.conf_name = \'Western\' " +
                "ORDER BY teams.win desc";
        try {

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            int gamesbehind; // gotta get the first place team
            System.out.printf("Western Conference:\n");
            System.out.printf("Team            W   L   PERCENTAGE \n");
            while(result.next()){
                double win_loss = (double)result.getInt(10)/(double)(result.getInt(11)+result.getInt(10));
                System.out.printf("%-15s: %-3d %-3d %-4.2f \n",
                        result.getString(8),
                        result.getInt(10),
                        result.getInt(11),
                        win_loss);
            }
            query = "SELECT * FROM conferences " +
                    "INNER JOIN divisions ON divisions.conf_id = conferences.conf_id " +
                    "INNER JOIN teams ON teams.div_id = divisions.div_id " +
                    "WHERE conferences.conf_name = \'Eastern\' " +
                    "ORDER BY teams.win desc";
            Statement stmt2 = conn.createStatement();
            ResultSet result2 = stmt2.executeQuery(query);
            System.out.printf("Eastern Conference:\n");
            System.out.printf("Team            W    L   PERCENTAGE\n");
            while(result2.next()){
                float win_loss = (float)result2.getInt(10)/(float)(result2.getInt(11)+result2.getInt(10));
                System.out.printf("%-15s: %-3d %-3d %-4.2f \n",
                        result2.getString(8),
                        result2.getInt(10),
                        result2.getInt(11),
                        win_loss);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
