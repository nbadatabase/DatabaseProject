package nba.fourguysonecode.tables;

import nba.fourguysonecode.objects.Team;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to make and manipulate the team table
 *
 * @author joshuasellers Created by joshuasellers on 4/2/17.
 */
public class TeamTable extends DatabaseTable
{

    public static final String TableName = "teams";

    /**
     * Reads a cvs file for data and adds them to the team table
     * <p>
     * Does not create the table. It must already be created
     *
     * @param conn:    database connection to work with
     * @param fileName The file name of the CSV containing the teams to add
     * @throws SQLException
     */

    public static void populateTeamTableFromCSV(Connection conn,
                                                String fileName)
            throws SQLException
    {
        /*
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         *
         * You can do the reading and adding to the table in one
         * step, I just broke it up for example reasons
         */
        ArrayList<Team> teams = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            // Skip the first line which is just the format specifier for the
            // CSV file.
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                teams.add(new Team(split));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * Creates the SQL query to do a bulk add of all teams
         * that were read in. This is more efficient then adding one
         * at a time
         */
        String sql = createTeamInsertSQL(teams);

        /*
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            if (e.getMessage().contains("TN_UNQ_IDX")) {
                System.err.println(
                        "There is a duplicate team in the file. No teams were added.");
                
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create the team table with the given attributes
     *
     * @param conn: the database connection to work with
     */
    public static void createTeamTable(Connection conn)
    {
        try {
            String query = "CREATE TABLE IF NOT EXISTS teams("
                    + "TEAM_ID INT PRIMARY KEY,"
                    + "DIV_ID INT,"
                    + "TEAM_NAME VARCHAR(255),"
                    + "LOCATION VARCHAR(255),"
                    + "WIN INT,"
                    + "LOSS INT,"
                    + ");"
                    + "CREATE UNIQUE INDEX TN_UNQ_IDX ON teams(TEAM_NAME)";

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
     * Adds a single team to the database
     *
     * @param conn The database to use
     * @param team_id The ID form the new team
     * @param div_id The division ID into which the new team will be placed
     * @param team_name The name of the new team
     * @param location Where the team will be located
     * @param win The teams current win count
     * @param loss The teams current loss count
     */
    public static void addTeam(Connection conn, int team_id, int div_id,
                               String team_name, String location, int win,
                               int loss)
    {

        /*
         * SQL insert statement
         */
        String query = String.format("INSERT INTO teams "
                                             + "VALUES(%d, %d,\'%s\',\'%s\', " +
                                             "\'%d\', \'%d\');",
                                     team_id, div_id, team_name, location, win,
                                     loss);
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
     * This creates an sql statement to do a bulk add of teams
     *
     * @param teams: list of Team objects to add
     * @return An INSERT SQL string containing all value sets from the array
     */
    public static String createTeamInsertSQL(ArrayList<Team> teams)
    {
        StringBuilder sb = new StringBuilder();

        /*
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to ad dit to
         */
        sb.append(
                "INSERT INTO teams (TEAM_ID, DIV_ID, TEAM_NAME, LOCATION, " +
                        "WIN, LOSS) VALUES");

        /*
         * For each team append a tuple
         *
         * If it is not the last team add a comma to seperate
         *
         * If it is the last team add a semi-colon to end the statement
         */
        for (int i = 0; i < teams.size(); i++) {
            Team t = teams.get(i);
            sb.append(String.format("(%d, %d,\'%s\',\'%s\', \'%d\', \'%d\')",
                                    t.getTeam_id(), t.getDiv_id(),
                                    t.getTeam_name(), t.getLocation(),
                                    t.getWin(), t.getLoss()));
            if (i != teams.size() - 1) {
                sb.append(",");
            } else {
                sb.append(";");
            }
        }

        return sb.toString();
    }

    /**
     * Makes a query to the team table
     * with given columns and conditions
     *
     * @param conn          The database to use
     * @param columns:      columns to return
     * @param whereClauses: conditions to limit query by
     * @return The results of the query if successful, otherwise null.
     */
    public static List<Team> queryTeamTable(Connection conn,
                                            ArrayList<String> columns,
                                            ArrayList<String> whereClauses)
    {
        // Query the database for all matching results.
        ResultSet results = TeamTable.queryTable(conn, TeamTable.TableName, columns, whereClauses);

        // Create a list to hold all of the Team objects.
        ArrayList<Team> teams = new ArrayList<>();

        try
        {
            // Loop through all of the results and create a Team object for each one.
            while (results.next())
            {
                // Create a new Team object using the result data.
                teams.add(new Team(results));
            }
        }
        catch (SQLException e)
        {
            // An error occurred while processing the results, print the stack trace.
            e.printStackTrace();
        }

        // Return the team list.
        return teams;
    }

    /**
     * Queries and prints the full team stats
     *
     * @param conn The databse to use
     */
    public static void printTeamTable(Connection conn)
    {
        String query = "SELECT * FROM teams " +
                "INNER JOIN teamstats ON teams.team_id = teamstats.team_id";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                System.out.println(
                        "*************************************************************");
                System.out.format("%-5s \nGW: %-5d GL: %-5d \n" +
                                          "PPG: %-5.2f FGA: %-5d FGM: %-5d " +
                                          "3PA: %-5d 3PM: %-5d\n" +
                                          "FTA: %-5d FTM: %-5d OREB: %-5d " +
                                          "DREB: %-5d AST: %-5d TRN: %-5d\n" +
                                          "STL: %-5d BLK: %-5d\n",

                                  result.getString(3),  //First name
                                  result.getInt(5),    //GW
                                  result.getInt(6), //GL

                                  result.getFloat(8) / (result.getFloat(
                                          5) + result.getFloat(6)),     //PPG
                                  result.getInt(9),     //FGA
                                  result.getInt(10),     //FGM
                                  result.getInt(11),    //TA
                                  result.getInt(12),    //TM

                                  result.getInt(13),    //FA
                                  result.getInt(14),    //FM
                                  result.getInt(15),    //OREB
                                  result.getInt(16),    //DREB
                                  result.getInt(17),    //AST
                                  result.getInt(18),    //TRN

                                  result.getInt(19),    //STL
                                  result.getInt(20)    //BLK
                );


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Queries and print the table
     *
     * @param conn The database to use
     */
    public static void printTeamTableBasic(Connection conn)
    {
        String query = "SELECT * FROM teams";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                System.out.println("*************************************");
                System.out.printf("%s  \nLocation: %s  Wins: %d  Loss: %d\n",
                                  result.getString(3),
                                  result.getString(4),
                                  result.getInt(5),
                                  result.getInt(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Queries and prints specific tables
     *
     * @param conn The database to use
     * @param inp2
     */
    public static void printTeamTableMulti(Connection conn, String[] inp2)
    {
        int s = inp2.length;
        for (int i = 0; i < s; i++) {
            String query = "SELECT * FROM teams " +
                    "INNER JOIN teamstats ON teams.team_id = teamstats" +
                    ".team_id " +
                    "WHERE teams.team_name = \'" + inp2[i] + "\'";
            try {
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(query);
                while (result.next()) {
                    System.out.println(
                            "*************************************************************");
                    System.out.format("%-5s \nGW: %-5d GL: %-5d \n" +
                                              "PPG: %-5.2f FGA: %-5d FGM: " +
                                              "%-5d 3PA: %-5d 3PM: %-5d\n" +
                                              "FTA: %-5d FTM: %-5d OREB: %-5d" +
                                              " DREB: %-5d AST: %-5d TRN: " +
                                              "%-5d\n" +
                                              "STL: %-5d BLK: %-5d\n",

                                      result.getString(3),  //First name
                                      result.getInt(5),    //GW
                                      result.getInt(6), //GL

                                      result.getFloat(8) / (result.getFloat(
                                              5) + result.getFloat(6)),
                                      //PPG
                                      result.getInt(9),     //FGA
                                      result.getInt(10),     //FGM
                                      result.getInt(11),    //TA
                                      result.getInt(12),    //TM

                                      result.getInt(13),    //FA
                                      result.getInt(14),    //FM
                                      result.getInt(15),    //OREB
                                      result.getInt(16),    //DREB
                                      result.getInt(17),    //AST
                                      result.getInt(18),    //TRN

                                      result.getInt(19),    //STL
                                      result.getInt(20)    //BLK
                    );


                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Queries and prints specific table's basic info
     *
     * @param conn The databse to use
     * @param inp2
     */
    public static void printTeamTableMultiBasic(Connection conn, String[] inp2)
    {
        int s = inp2.length;
        for (int i = 0; i < s; i++) {
            String query = "SELECT * FROM teams " +
                    "INNER JOIN teamstats ON teams.team_id = teamstats" +
                    ".team_id " +
                    "WHERE teams.team_name = \'" + inp2[i] + "\'";
            try {
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(query);
                while (result.next()) {
                    System.out.println(
                            "*************************************************************");
                    System.out.format("%-5s \nGW: %-5d GL: %-5d \n",
                                      result.getString(3),  //First name
                                      result.getInt(5),    //GW
                                      result.getInt(6) //GL
                    );
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Queries and prints specific stats from table
     *
     * @param conn The database to use
     * @param inp
     */
    public static void printTeamStats(Connection conn, String inp)
    {
        String query = "SELECT " + inp + ", team_name FROM teamstats " +
                "INNER JOIN teams ON teamstats.team_id = teams.team_id " +
                "ORDER BY teamstats." + inp + " DESC";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                System.out.println("*************************************");
                if (inp.equals("tot_pts")) {
                    System.out.printf("%s: %.2f \n",
                                      result.getString(2),
                                      result.getFloat(1) / 82);
                } else {
                    System.out.printf("%s: %d \n",
                                      result.getString(2),
                                      result.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Queries and prints specific stats to compare from table
     *
     * @param conn The database to use
     * @param inp2
     */
    public static void printTeamComp(Connection conn, String[] inp2)
    {
        int s = inp2.length;
        ArrayList<ResultSet> results = new ArrayList<>();
        for (int i = 0; i < s; i++) {
            String query = "SELECT * FROM teams " +
                    "INNER JOIN teamstats ON teams.team_id = teamstats" +
                    ".team_id " +
                    "WHERE teams.team_name = \'" + inp2[i] + "\'";
            try {
                Statement stmt = conn.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet result = stmt.executeQuery(query);
                results.add(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        int pl = results.size();
        System.out.println(
                "*************************************************************");
        System.out.println("Comparison of " + pl + " teams:");
        System.out.println("- Stat rank shown in parentheses -");
        System.out.println(
                "*************************************************************");
        for (int j = 0; j < pl; j++) {
            try {
                results.get(j).first();
                System.out.format("%-5s \nGW: %-5d (%d) GL: %-5d (%d)\n" +
                                          "PPG: %-5.2f (%d) FGA: %-5d (%d) " +
                                          "FGM: %-5d (%d) 3PA: %-5d (%d) 3PM:" +
                                          " %-5d (%d)\n" +
                                          "FTA: %-5d (%d) FTM: %-5d (%d) " +
                                          "OREB: %-5d (%d) DREB: %-5d (%d) " +
                                          "AST: %-5d (%d) " +
                                          "TRN: %-5d (%d)\n" +
                                          "STL: %-5d (%d) BLK: %-5d (%d)\n",

                                  results.get(j).getString(3),  //First name
                                  results.get(j).getInt(5),    //GW
                                  rank(results, j, 5),
                                  results.get(j).getInt(6), //GL
                                  rank(results, j, 6),

                                  results.get(j).getFloat(8) / (results.get(
                                          j).getFloat(5) + results.get(
                                          j).getFloat(6)),     //PPG
                                  rank(results, j, 8),
                                  results.get(j).getInt(9),     //FGA
                                  rank(results, j, 9),
                                  results.get(j).getInt(10),     //FGM
                                  rank(results, j, 10),
                                  results.get(j).getInt(11),    //TA
                                  rank(results, j, 11),
                                  results.get(j).getInt(12),    //TM
                                  rank(results, j, 12),

                                  results.get(j).getInt(13),    //FA
                                  rank(results, j, 13),
                                  results.get(j).getInt(14),    //FM
                                  rank(results, j, 14),
                                  results.get(j).getInt(15),    //OREB
                                  rank(results, j, 15),
                                  results.get(j).getInt(16),    //DREB
                                  rank(results, j, 16),
                                  results.get(j).getInt(17),    //AST
                                  rank(results, j, 17),
                                  results.get(j).getInt(18),    //TRN
                                  rank(results, j, 18),

                                  results.get(j).getInt(19),    //STL
                                  rank(results, j, 19),
                                  results.get(j).getInt(20),    //BLK
                                  rank(results, j, 20));
                System.out.println("-----");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Ranks stat
     *
     * @param r
     * @param num_in_list
     * @param stat
     * @return
     */
    public static int rank(ArrayList<ResultSet> r, Integer num_in_list,
                           Integer stat)
    {
        int size = r.size();
        int rank = 1;
        int s1 = 0;
        float s2 = 0;
        try {
            r.get(num_in_list).first();
            if (stat == 8) {
                s2 = r.get(num_in_list).getFloat(stat);
            } else {
                s1 = r.get(num_in_list).getInt(stat);
            }
            for (int i = 0; i < size; i++) {
                if (i != num_in_list) {
                    r.get(i).first();
                    if (stat == 8) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rank;
    }
}
