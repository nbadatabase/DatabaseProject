package NBA.db;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by joshuasellers on 4/2/17.
 */
public class NBAmain {

    //The connection to the database
    private Connection conn;

    /**
     * Create a database connection with the given params
     * @param location: path of where to place the database
     * @param user: user name for the owner of the database
     * @param password: password of the database owner
     */

    public void createConnection(String location,
                                 String user,
                                 String password){
        try {

            //This needs to be on the front of your location
            String url = "jdbc:h2:" + location;

            //This tells it to use the h2 driver
            Class.forName("org.h2.Driver");

            //creates the connection
            conn = DriverManager.getConnection(url,
                    user,
                    password);
        } catch (SQLException | ClassNotFoundException e) {
            //You should handle this better
            e.printStackTrace();
        }
    }

    /**
     * just returns the connection
     * @return: returns class level connection
     */
    public Connection getConnection(){
        return conn;
    }

    /**
     * When your database program exits
     * you should close the connection
     */
    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts and runs the database
     * @param args: not used but you can use them
     */
    public static void main(String[] args) {
        NBAmain db = new NBAmain();

        //Hard drive location of the database
        String location = "~/h2demo/h2demo";
        String user = "scj";
        String password = "password";

        //Create the database connections, basically makes the database
        db.createConnection(location, user, password);
    }
}
