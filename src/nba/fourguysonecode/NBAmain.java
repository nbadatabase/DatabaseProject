package nba.fourguysonecode;

import nba.fourguysonecode.objects.Player;
import nba.fourguysonecode.objects.TeamStats;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.Collections;
import java.util.Scanner;
import java.util.Arrays;

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

    private String playerSearch(String inp, Scanner sc, NBAmain db){
        Boolean go_back = false;
        while (go_back == false) {
            char c = '\n';
            int length = 35;
            char[] chars = new char[length];
            Arrays.fill(chars, c);
            System.out.print(String.valueOf(chars));
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.printf("%27s\n", "PLAYERS");
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.println("Use these commands to explore the players:");
            System.out.println("Precede marked commands [x] with 'b' to just show basic player info");
            System.out.println("   [x]*: Gets all the players' stats");
            System.out.println("   [x]fl: Gets players' stats");
            System.out.println("   pc: Compare players' stats");
            System.out.println("   pi: Insert new player");
            System.out.println("   s: Shows league leaders for stat");
            System.out.println("   q: quit database");
            System.out.println("   h: return home");
            inp = sc.next();
            if(inp.equals("h")){
                go_back = true;
            }
            else if(inp.equals("q")){
                go_back = true;
            }
            else if(inp.equals("pi")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "INSERT");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                ArrayList<String> data = new ArrayList<>();
                System.out.println("");
                System.out.println("In order to insert into the database, give all the relevant data for the player.");
                System.out.println("****************************************************************");
                System.out.println("Player ID:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Team ID:");
                inp = sc.next();
                data.add(inp);
                System.out.println("First Name:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Last Name:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Date of Birth:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Games Played:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Total Minutes:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Total Points:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Field Goals Attempted:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Field Goals Made:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Threes Attempted:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Threes Made:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Free Throws Attempted:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Free Throws Made:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Offensive Rebounds:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Defensive Rebounds:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Assists:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Steals:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Blocks:");
                inp = sc.next();
                data.add(inp);
                System.out.println("Turnovers:");
                inp = sc.next();
                data.add(inp);
                PlayerTable.insertPlayer(db.getConnection(), data);
                System.out.println("\nInput to continue");
                inp = sc.next();
            }
            else if(inp.equals("*") || inp.equals("b*")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "PL_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                if(inp.equals("*")){
                    PlayerTable.printPlayerTable(db.getConnection());
                }
                else{
                    PlayerTable.printPlayerTableBasic(db.getConnection());
                }
                System.out.println("\nInput to continue:");
                inp = sc.next();

            }
            else if(inp.equals("fl") || inp.equals("bfl")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "PL_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                if(inp.equals("fl")){
                    System.out.println("Players (i.e. Isaiah Thomas, Avery Bradley, ...):");
                    inp = sc.nextLine();
                    inp = sc.nextLine();
                    String[] inp2 = inp.split(", ");
                    PlayerTable.printPlayerTableMulti(db.getConnection(), inp2);
                }
                else{
                    System.out.println("Players (i.e. Isaiah Thomas, Avery Bradley, ...):");
                    inp = sc.nextLine();
                    inp = sc.nextLine();
                    String[] inp2 = inp.split(", ");
                    PlayerTable.printPlayerTableMultiBasic(db.getConnection(), inp2);
                }
                System.out.println("\nInput to continue:");
                inp = sc.next();
            }
            else if(inp.equals("pc")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "PL_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("Players (i.e. Isaiah Thomas, Avery Bradley, ...):");
                inp = sc.nextLine();
                inp = sc.nextLine();
                String[] inp2 = inp.split(", ");
                PlayerTable.printPlayerComp(db.getConnection(), inp2);
                System.out.println("\nInput to continue:");
                inp = sc.next();
            }
            else if (inp.equals("s")) {
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "PL_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("If you want a list of the stats: ls");
                System.out.println("If you already know your stat: c");
                inp = sc.next();
                if (inp.equals("ls")){
                    System.out.println("Total Minutes: tot_mins, Points Per Game: tot_pts, " +
                            "Field Goals Attempted: fg_att,\nField Goals Made: fg_made, " +
                            "Three Point Shots Attempted: three_att, Three Point Shots Made: three_made,\n" +
                            "Free Throws Attempted: free_att, Free Throws Made: free_made, " +
                            "Offensive Rebounds: off_rebound,\nDefensive Rebounds: def_rebound, Assists: assists, " +
                            "Steals: steals, Blocks: blocks, Turnovers: turnovers");
                    System.out.println("Stat:");
                    inp = sc.next();
                    PlayerTable.printPlayerStats(db.getConnection(), inp);
                }
                else {
                    System.out.println("Stat:");
                    inp = sc.next();
                    PlayerTable.printPlayerStats(db.getConnection(), inp);
                }
                System.out.println("\nInput to continue:");
                inp = sc.next();
            }
        }
        return inp;
    }

    private String teamSearch(String inp, Scanner sc, NBAmain db){
        Boolean go_back = false;
        while (go_back == false) {
            char c = '\n';
            int length = 35;
            char[] chars = new char[length];
            Arrays.fill(chars, c);
            System.out.print(String.valueOf(chars));
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.printf("%26s\n", "TEAMS");
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.println("Use these commands to explore the team:");
            System.out.println("Precede marked commands [x] with 'b' to just show basic team info");
            System.out.println("   [x]*: Gets all the teams' stats");
            System.out.println("   [x]tn: Gets teams' stats");
            System.out.println("   tc: Compare teams' stats");
            System.out.println("   s: Shows league leaders for stat");
            System.out.println("   q: quit database");
            System.out.println("   h: return home");
            inp = sc.next();
            if(inp.equals("h")){
                go_back = true;
            }
            else if(inp.equals("q")){
                go_back = true;
            }
            else if(inp.equals("*") || inp.equals("b*")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "TM_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                if(inp.equals("*")){
                    TeamTable.printTeamTable(db.getConnection());
                }
                else{
                    TeamTable.printTeamTableBasic(db.getConnection());
                }
                System.out.println("\nInput to continue:");
                inp = sc.next();

            }
            else if(inp.equals("tn") || inp.equals("btn")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "TM_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                if(inp.equals("tn")){
                    System.out.println("Teams to be displayed (i.e. Miami Heat, Boston Celtics, ...):");
                    inp = sc.nextLine();
                    inp = sc.nextLine();
                    String[] inp2 = inp.split(", ");
                    TeamTable.printTeamTableMulti(db.getConnection(), inp2);
                }
                else{
                    System.out.println("Teams to be displayed (i.e. Miami Heat, Boston Celtics, ...):");
                    inp = sc.nextLine();
                    inp = sc.nextLine();
                    String[] inp2 = inp.split(", ");
                    TeamTable.printTeamTableMultiBasic(db.getConnection(), inp2);
                }
                System.out.println("\nInput to continue:");
                inp = sc.next();
            }
            else if(inp.equals("tc")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "TM_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("Teams to be displayed (i.e. Miami Heat, Boston Celtics, ...):");
                inp = sc.nextLine();
                inp = sc.nextLine();
                String[] inp2 = inp.split(", ");
                TeamTable.printTeamComp(db.getConnection(), inp2);
                System.out.println("\nInput to continue:");
                inp = sc.next();
            }
            else if(inp.equals("s")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "TM_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("If you want a list of the stats: ls");
                System.out.println("If you already know your stat: c");
                inp = sc.next();
                if (inp.equals("ls")){
                    System.out.println("Games Won: win, Games Lost: loss, Total Points: tot_pts, " +
                            "Field Goals Attempted: fg_att,\nField Goals Made: fg_made, " +
                            "Three Point Shots Attempted: three_att, Three Point Shots Made: three_made,\n" +
                            "Free Throws Attempted: free_att, Free Throws Made: free_made, " +
                            "Offensive Rebounds: off_rebound,\nDefensive Rebounds: def_rebound, Assists: assists, " +
                            "Steals: steals, Blocks: blocks, Turnovers: turnovers");
                    System.out.println("Stat:");
                    inp = sc.next();
                    TeamTable.printTeamStats(db.getConnection(), inp);
                }
                else {
                    System.out.println("Stat:");
                    inp = sc.next();
                    TeamTable.printTeamStats(db.getConnection(), inp);
                }
                System.out.println("\nInput to continue:");
                inp = sc.next();
            }
        }
        return inp;
    }

    private String divisionSearch(String inp, Scanner sc, NBAmain db){
        Boolean go_back = false;
        while (go_back == false) {
            char c = '\n';
            int length = 35;
            char[] chars = new char[length];
            Arrays.fill(chars, c);
            System.out.print(String.valueOf(chars));
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.printf("%28s\n", "DIVISIONS");
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.println("Use these commands to explore the divisions:");
            System.out.println("   *: Gets all the divisions' info");
            System.out.println("   dd: Gets one division's info");
            System.out.println("   dm: Gets many divisions' info");
            System.out.println("   q: quit database");
            System.out.println("   h: return home");
            inp = sc.next();
            if(inp.equals("h")){
                go_back = true;
            }
            else if(inp.equals("q")){
                go_back = true;
            }
            else if(inp.equals("*")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "DV_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                DivisionTable.printDivisionTable(db.getConnection());
                System.out.println("\nInput to continue:");
                inp = sc.next();
            }
            else if(inp.equals("dd")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "DV_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("Choose division (i.e. Atlantic):");
                inp = sc.next();
                DivisionTable.printDivisionTableSingular(db.getConnection(), inp);
                System.out.println("\nInput to continue:");
                inp = sc.next();
            }
            else if(inp.equals("dm")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "DV_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("Choose divisions (i.e. Atlantic Pacific ...):");
                String np = sc.nextLine();
                inp = sc.nextLine();
                String[] inp2 = inp.split("\\s+");
                DivisionTable.printDivisionTableMulti(db.getConnection(), inp2);
                System.out.println("\nInput to continue:");
                inp = sc.next();
            }
        }
        return inp;
    }

    private String conferenceSearch(String inp, Scanner sc, NBAmain db){
        Boolean go_back = false;
        while (go_back == false) {
            char c = '\n';
            int length = 35;
            char[] chars = new char[length];
            Arrays.fill(chars, c);
            System.out.print(String.valueOf(chars));
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.printf("%32s\n", "CONFERENCES");
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.println("Use these commands to explore the conferences:");
            System.out.println("   *: Gets all the conferences' info");
            System.out.println("   cc: Gets one conference's info");
            System.out.println("   q: quit database");
            System.out.println("   h: return home");
            inp = sc.next();
            if(inp.equals("h")){
                go_back = true;
            }
            else if(inp.equals("q")){
                go_back = true;
            }
            else if(inp.equals("*")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "CN_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                ConferenceTable.printConferenceTable(db.getConnection());
                System.out.println("\nInput to continue:");
                inp = sc.next();
            }
            else if(inp.equals("cc")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "CN_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("Choose conference (i.e. 'Eastern'):");
                inp = sc.next();
                ConferenceTable.printConferenceTableSingular(db.getConnection(), inp);
                System.out.println("\nInput to continue:");
                inp = sc.next();
            }
        }
        return inp;
    }

    public ArrayList<String> getTablesFromDatabase()
    {
        // Create a new array list to store the results in.
        ArrayList<String> tableNames = new ArrayList<String>();

        try
        {
            // Get a list of tables that aready exist in the database.
            Statement tableExistsStatement = this.conn.createStatement();
            ResultSet tableResults = tableExistsStatement.executeQuery("show tables");

            // Loop through all the results in the result set and add each one to the list.
            while (tableResults.next() == true)
                tableNames.add(tableResults.getString("TABLE_NAME"));

            // Close the SQL objects.
            tableResults.close();
            tableExistsStatement.close();
        }
        catch (SQLException e)
        {
            // Print a message that we failed to get the table names.
            System.out.println("NBAmain::getTablesFromDatabase(): failed to get table names from database!");
        }

        // Return the list of tables.
        return tableNames;
    }

    /**
     * Starts and runs the database
     * @param args: not used but you can use them
     */
    public static void main(String[] args) {
        NBAmain db = new NBAmain();
        Boolean running = true;

        //Hard drive location of the database
        String location = "./nba.db";
        String user = "scj";
        String password = "password";

        //Create the database connections, basically makes the database
        db.createConnection(location, user, password);

        try {
            // Get a list of table names that already exist in the database.
            ArrayList<String> tableNames = db.getTablesFromDatabase();

            // Check if the Conferences table exists and if not create it.
            if (tableNames.contains(ConferenceTable.TableName) == false)
            {
                // Create the conferences table.
                ConferenceTable.createConferenceTable(db.getConnection());
                ConferenceTable.populateConferenceTableFromCSV(db.getConnection(),
                        "./data/Conference.csv");
            }

            // Check if the Divisions table exists and if not create it.
            if (tableNames.contains(DivisionTable.TableName) == false)
            {
                // Create the divisions table.
                DivisionTable.createDivisionTable(db.getConnection());
                DivisionTable.populateDivisionTableFromCSV(db.getConnection(),
                        "./data/Division.csv");
            }

            // Check if the teams table exists and if not create it.
            if (tableNames.contains(TeamTable.TableName) == false)
            {
                // Create the teams table.
                TeamTable.createTeamTable(db.getConnection());
                TeamTable.populateTeamTableFromCSV(db.getConnection(),
                        "./data/Team.csv");
            }

            // Check if the team stats table exists and if not create it.
            if (tableNames.contains(TeamStatsTable.TableName) == false)
            {
                // Create the team stats table.
                TeamStatsTable.createTeamStatsTable(db.getConnection());
                TeamStatsTable.populateTeamStatsTableFromCSV(db.getConnection(),
                        "./data/TeamStats.csv");
            }

            // Check if the players table exists and if not create it.
            if (tableNames.contains(PlayerTable.TableName) == false)
            {
                // Create the players table.
                PlayerTable.createPlayerTable(db.getConnection());
                PlayerTable.populatePlayerTableFromCSV(db.getConnection(),
                        "./data/Player.csv");
            }

            // Check if the player stats table exists and if not create it.
            if (tableNames.contains(PlayerStatsTable.TableName) == false)
            {
                // Create the player stats table.
                PlayerStatsTable.createPlayerStatsTable(db.getConnection());
                PlayerStatsTable.populatePlayerStatsTableFromCSV(db.getConnection(),
                        "./data/PlayerStats.csv");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Scanner scan = new Scanner(System.in);
        while(running){
            char c = '\n';
            int length = 35;
            char[] chars = new char[length];
            Arrays.fill(chars, c);
            System.out.print(String.valueOf(chars));
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.printf("%32s", "THE NBA DATABASE");
            System.out.println("");
            System.out.printf("%42s\n","THIS DATABASE CONTAINS NBA STATISTICS");
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.println("Use these commands to run queries on the data:");
            System.out.println("  p: explore player data");
            System.out.println("  t: explore team data");
            System.out.println("  d: explore division data");
            System.out.println("  c: explore conference data");
            System.out.println("  q: quit database");
            String s = scan.next();
            if(s.equals("p")){s = db.playerSearch(s, scan, db);}
            else if(s.equals("t")){s = db.teamSearch(s, scan, db);}
            else if(s.equals("d")){s = db.divisionSearch(s, scan, db);}
            else if(s.equals("c")){s = db.conferenceSearch(s, scan, db);}
            if(s.equals("q")){running = false;}
        }
        db.closeConnection();
    }
}
