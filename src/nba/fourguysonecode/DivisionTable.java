package nba.fourguysonecode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import nba.fourguysonecode.objects.Division;

/**
 * Class to make and manipulate the division table
 * @author joshuasellers
 * Created by joshuasellers on 4/2/17.
 */
public class DivisionTable {
    /**
     * Reads a cvs file for data and adds them to the division table
     *
     * Does not create the table. It must already be created
     *
     * @param conn: database connection to work with
     * @param fileName
     * @throws SQLException
     */

    public static void populateDivisionTableFromCSV(Connection conn, String fileName)
            throws SQLException{
        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<Division> divisions = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                divisions.add(new Division(Integer.getInteger(split[0]),
                        Integer.getInteger(split[1]),
                        split[2]));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Creates the SQL query to do a bulk add of all divisions
         * that were read in. This is more efficient then adding one
         * at a time
         */
        String sql = createDivisionsInsertSQL(divisions);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    /**
     * Create the division table with the given attributes
     *
     * @param conn: the database connection to work with
     */
    public static void createDivisionTable(Connection conn){
        try {
            String query = "CREATE TABLE IF NOT EXISTS divisions("
                    + "DIV_ID INT PRIMARY KEY,"
                    + "CONF_ID INT FOREIGN KEY,"
                    + "DIV_NAME VARCHAR(255),"
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
     * Adds a single division to the database
     *
     * @param conn
     * @param div_id
     * @param conf_id
     * @param div_name
     */
    public static void addDivision(Connection conn, int div_id, int conf_id, String div_name){

        /**
         * SQL insert statement
         */
        String query = String.format("INSERT INTO divisions "
                        + "VALUES(%d, %d,\'%s\');",
                div_id, conf_id, div_name);
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
     * This creates an sql statement to do a bulk add of divisions
     *
     * @param divisions: list of Division objects to add
     *
     * @return
     */
    public static String createDivisionsInsertSQL(ArrayList<Division> divisions){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append("INSERT INTO divisions (DIV_ID, CONF_ID, DIV_NAME) VALUES");

        /**
         * For each division append a tuple
         *
         * If it is not the last division add a comma to seperate
         *
         * If it is the last division add a semi-colon to end the statement
         */
        for(int i = 0; i < divisions.size(); i++){
            Division d = divisions.get(i);
            sb.append(String.format("(%d, %d,\'%s\')",
                    d.getDiv_id(), d.getConf_id(), d.getDiv_name()));
            if( i != divisions.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }

        return sb.toString();
    }

    /**
     * Makes a query to the division table
     * with given columns and conditions
     *
     * @param conn
     * @param columns: columns to return
     * @param whereClauses: conditions to limit query by
     * @return
     */
    public static ResultSet queryDivisionTable(Connection conn,
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
        sb.append("FROM divisions ");

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
