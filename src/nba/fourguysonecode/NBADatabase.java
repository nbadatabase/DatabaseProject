package nba.fourguysonecode;

import nba.fourguysonecode.tables.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by rmiceli on 4/23/2017.
 */
public class NBADatabase
{
    // Our H2 database connection.
    private final String DatabaseLocation = "./nba.db";
    private final String DatabaseUsername = "scj";
    private final String DatabasePassword = "password";
    private Connection conn = null;

    /**
     * Establishes a connection to the database at the specified location.
     * @return true if the connection was successfully established, false otherwise.
     */
    public boolean createConnection()
    {
        // Create the connection using the default database location.
        return createConnection(DatabaseLocation);
    }

    /**
     * Establishes a connection to the database at the specified location.
     * @param location: path of where to place the database
     * @return true if the connection was successfully established, false otherwise.
     */
    public boolean createConnection(String location)
    {
        try
        {
            // Format the database location specifier.
            String url = "jdbc:h2:" + location;

            // This tells it to use the h2 driver.
            // I don't think this line is actually necessary, it will just throw an exception if
            // it can't find the h2 driver, the specification for using the h2 driver over other
            // database drivers is in the database location specifier.
            Class.forName("org.h2.Driver");

            // Create the connection to the database.
            this.conn = DriverManager.getConnection(url, DatabaseUsername, DatabasePassword);

            // Initialize the database if is has not been initialized yet.
            initializeDatabase();

            // Successfully opened the database connection.
            return true;
        }
        catch (SQLException | ClassNotFoundException e)
        {
            // An error occurred while trying to open/create the database. Just return false
            // and the upper level program will handle displaying an error to the user.
            this.conn = null;
            return false;
        }
    }

    /**
     * Gets the SQL connection object.
     * @return returns class level connection
     */
    public Connection getConnection()
    {
        // Return the sql database connection object.
        return this.conn;
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection()
    {
        try
        {
            // Close the database connection.
            this.conn.close();
        }
        catch (SQLException e)
        {
        }
    }
    
    private void initializeDatabase()
    {
        try 
        {
            // Get a list of table names that already exist in the database.
            ArrayList<String> tableNames = getTablesFromDatabase();

            // Check if the Conferences table exists and if not create it.
            if (!tableNames.contains(ConferenceTable.TableName))
            {
                // Create the conferences table.
                ConferenceTable.createConferenceTable(this.conn);
                ConferenceTable.populateConferenceTableFromCSV(this.conn,
                        "./data/Conference.csv");
            }

            // Check if the Divisions table exists and if not create it.
            if (!tableNames.contains(DivisionTable.TableName))
            {
                // Create the divisions table.
                DivisionTable.createDivisionTable(this.conn);
                DivisionTable.populateDivisionTableFromCSV(this.conn,
                        "./data/Division.csv");
            }

            // Check if the teams table exists and if not create it.
            if (!tableNames.contains(TeamTable.TableName))
            {
                // Create the teams table.
                TeamTable.createTeamTable(this.conn);
                TeamTable.populateTeamTableFromCSV(this.conn,
                        "./data/Team.csv");
            }

            // Check if the team stats table exists and if not create it.
            if (!tableNames.contains(TeamStatsTable.TableName))
            {
                // Create the team stats table.
                TeamStatsTable.createTeamStatsTable(this.conn);
                TeamStatsTable.populateTeamStatsTableFromCSV(this.conn,
                        "./data/TeamStats.csv");
            }

            // Check if the players table exists and if not create it.
            if (!tableNames.contains(PlayerTable.TableName))
            {
                // Create the players table.
                PlayerTable.createPlayerTable(this.conn);
                PlayerTable.populatePlayerTableFromCSV(this.conn,
                        "./data/Player.csv");
            }

            // Check if the player stats table exists and if not create it.
            if (!tableNames.contains(PlayerStatsTable.TableName))
            {
                // Create the player stats table.
                PlayerStatsTable.createPlayerStatsTable(this.conn);
                PlayerStatsTable.populatePlayerStatsTableFromCSV(this.conn,
                        "./data/PlayerStats.csv");
            }

        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of names of the tables that are present in the database.
     * @return ArrayList of Strings containing the names of the tables in the database.
     */
    public ArrayList<String> getTablesFromDatabase()
    {
        // Create a new array list to store the results in.
        ArrayList<String> tableNames = new ArrayList<String>();

        try
        {
            // Get a list of tables that already exist in the database.
            Statement tableExistsStatement = this.conn.createStatement();
            ResultSet tableResults = tableExistsStatement.executeQuery("show tables");

            // Loop through all the results in the result set and add each one to the list.
            while (tableResults.next() == true)
                tableNames.add(tableResults.getString("TABLE_NAME").toLowerCase());

            // Close the SQL objects.
            tableResults.close();
            tableExistsStatement.close();
        }
        catch (SQLException e)
        {
            // Print a message that we failed to get the table names.
            System.out.println("NBADatabase::getTablesFromDatabase(): failed to get table names from database!");
        }

        // Return the list of tables.
        return tableNames;
    }
}
