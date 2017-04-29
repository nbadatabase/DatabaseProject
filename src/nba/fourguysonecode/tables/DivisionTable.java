package nba.fourguysonecode.tables;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import nba.fourguysonecode.objects.Division;

/**
 * Class to make and manipulate the division table
 * @author joshuasellers
 * Created by joshuasellers on 4/2/17.
 */
public class DivisionTable extends DatabaseTable
{

    public static final String TableName = "divisions";

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

            // Skip the first line which is just the format specifier for the CSV file.
            String line = br.readLine();
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                divisions.add(new Division(split));
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
                    + "CONF_ID INT,"
                    + "DIV_NAME VARCHAR(255),"
                    + "FOREIGN KEY (CONF_ID) REFERENCES conferences);" ;

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
    public static List<Division> queryDivisionTable(Connection conn,
                                                    ArrayList<String> columns,
                                                    ArrayList<String> whereClauses)
    {
        // Query the database and get all the results.
        ResultSet results = DivisionTable.queryTable(conn, DivisionTable.TableName, columns, whereClauses);

        // Create a list to hold all of the division objects.
        List<Division> divisions = new ArrayList<>();

        try
        {
            // Loop through all of the results and create a new Division object for each one.
            while (results.next())
            {
                // Create a new Division object from the result data and add it to the list.
                divisions.add(new Division(results));
            }
        }
        catch (SQLException e)
        {
            // An error occurred while processing the results, print the stack trace.
            e.printStackTrace();
        }

        // Return the list of Division objects.
        return divisions;
    }
    /**
     * Queries and print the table
     * @param conn
     */
    public static void printDivisionTable(Connection conn){
        String query = "SELECT * FROM divisions " +
                "INNER JOIN teams ON teams.div_id = divisions.div_id " +
                "WHERE divisions.div_name = \'Atlantic Division\' " +
                "ORDER BY teams.win desc";
        try {

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.printf("Atlantic Division:\n");
            System.out.printf("Team            W    L   PERCENTAGE \n");
            while(result.next()){
                float win_loss = (float)result.getInt(8)/(float)(result.getInt(8)+result.getInt(9));
                System.out.printf("%-15s: %-3d %-3d %-4.2f \n",
                        result.getString(6),
                        result.getInt(8),
                        result.getInt(9),
                        win_loss);
                System.out.println("------------------------------");
            }
            query = "SELECT * FROM divisions " +
                    "INNER JOIN teams ON teams.div_id = divisions.div_id " +
                    "WHERE divisions.div_name = \'Central Division\' " +
                    "ORDER BY teams.win desc";
            Statement stmt2 = conn.createStatement();
            ResultSet result2 = stmt2.executeQuery(query);
            System.out.printf("Central Division:\n");
            System.out.printf("Team            W    L   PERCENTAGE\n");
            while(result2.next()){
                float win_loss = (float)result2.getInt(8)/(float)(result2.getInt(8)+result2.getInt(9));
                System.out.printf("%-15s: %-3d %-3d %-4.2f \n",
                        result2.getString(6),
                        result2.getInt(8),
                        result2.getInt(9),
                        win_loss);
                System.out.println("------------------------------");
            }
            query = "SELECT * FROM divisions " +
                    "INNER JOIN teams ON teams.div_id = divisions.div_id " +
                    "WHERE divisions.div_name = \'Southeast Division\' " +
                    "ORDER BY teams.win desc";
            Statement stmt3 = conn.createStatement();
            ResultSet result3 = stmt3.executeQuery(query);
            System.out.printf("Southeast Division:\n");
            System.out.printf("Team            W    L   PERCENTAGE\n");
            while(result3.next()){
                float win_loss = (float)result3.getInt(8)/(float)(result3.getInt(8)+result3.getInt(9));
                System.out.printf("%-15s: %-3d %-3d %-4.2f \n",
                        result3.getString(6),
                        result3.getInt(8),
                        result3.getInt(9),
                        win_loss);
                System.out.println("------------------------------");
            }
            query = "SELECT * FROM divisions " +
                    "INNER JOIN teams ON teams.div_id = divisions.div_id " +
                    "WHERE divisions.div_name = \'Northwest Division\' " +
                    "ORDER BY teams.win desc";
            Statement stmt4 = conn.createStatement();
            ResultSet result4 = stmt4.executeQuery(query);
            System.out.printf("Northwest Division:\n");
            System.out.printf("Team            W    L   PERCENTAGE\n");
            while(result.next()){
                float win_loss = (float)result4.getInt(8)/(float)(result4.getInt(8)+result4.getInt(9));
                System.out.printf("%-15s: %-3d %-3d %-4.2f \n",
                        result4.getString(6),
                        result4.getInt(8),
                        result4.getInt(9),
                        win_loss);
                System.out.println("------------------------------");
            }
            query = "SELECT * FROM divisions " +
                    "INNER JOIN teams ON teams.div_id = divisions.div_id " +
                    "where divisions.div_name = \'Pacific Division\' " +
                    "ORDER BY teams.win desc";
            Statement stmt5 = conn.createStatement();
            ResultSet result5 = stmt5.executeQuery(query);
            System.out.printf("Pacific Division:\n");
            System.out.printf("Team            W    L   PERCENTAGE\n");
            while(result5.next()){
                float win_loss = (float)result5.getInt(8)/(float)(result5.getInt(8)+result5.getInt(9));
                System.out.printf("%-15s: %-3d %-3d %-4.2f \n",
                        result5.getString(6),
                        result5.getInt(8),
                        result5.getInt(9),
                        win_loss);
                System.out.println("------------------------------");
            }
            query = "SELECT * FROM divisions " +
                    "INNER JOIN teams ON teams.div_id = divisions.div_id " +
                    "WHERE divisions.div_name = \'Southwest Division\' " +
                    "ORDER BY teams.win desc";
            Statement stmt6 = conn.createStatement();
            ResultSet result6 = stmt6.executeQuery(query);
            System.out.printf("Southwest Division:\n");
            System.out.printf("Team            W    L   PERCENTAGE\n");
            while(result6.next()){
                float win_loss = (float)result6.getInt(8)/(float)(result6.getInt(8)+result6.getInt(9));
                System.out.printf("%-15s: %-3d %-3d %-4.2f \n",
                        result6.getString(6),
                        result6.getInt(8),
                        result6.getInt(9),
                        win_loss);
                System.out.println("------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Queries and print one table
     * @param conn
     * @param inp
     */
    public static void printDivisionTableSingular(Connection conn, String inp){
        String query = "SELECT * FROM divisions " +
                "INNER JOIN teams ON teams.div_id = divisions.div_id " +
                "WHERE divisions.div_name = \'"+inp+"\' " +
                "ORDER BY teams.win DESC";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.printf(inp+":\n");
            System.out.printf("Team            W    L   PERCENTAGE \n");
            while(result.next()){
                float win_loss = (float)result.getInt(8)/(float)(result.getInt(8)+result.getInt(9));
                System.out.printf("%-15s: %-3d %-3d %-4.2f \n",
                        result.getString(6),
                        result.getInt(8),
                        result.getInt(9),
                        win_loss);
                System.out.println("------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * Queries and print many tables
     * @param conn
     * @param inp2
     */
    public static void printDivisionTableMulti(Connection conn, String[] inp2) {
        int s = inp2.length;
        for (int i = 0; i < s; i++) {
            String query = "SELECT * FROM divisions " +
                    "INNER JOIN teams ON teams.div_id = divisions.div_id " +
                    "WHERE divisions.div_name = \'" + inp2[i] + "\' " +
                    "ORDER BY teams.win desc";
            try {
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(query);
                System.out.printf(inp2[i] + ":\n");
                System.out.printf("Team            W    L   PERCENTAGE \n");
                while (result.next()) {
                    float win_loss = (float) result.getInt(8) / (float) (result.getInt(8) + result.getInt(9));
                    System.out.printf("%-15s: %-3d %-3d %-4.2f \n",
                            result.getString(6),
                            result.getInt(8),
                            result.getInt(9),
                            win_loss);
                    System.out.println("------------------------------");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
