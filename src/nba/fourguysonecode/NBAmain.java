package nba.fourguysonecode;

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

    public static String playerSearch(String inp, Scanner sc){
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
            System.out.println("   [x]fl: Gets one player's stats");
            System.out.println("   [x]pc: Compares players' stats");
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
                System.out.printf("%27s\n", "PL_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                if(inp.equals("*")){
                    //TODO run query
                }
                else{
                    //TODO run query
                }
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();

            }
            else if(inp.equals("fl") || inp.equals("bfl")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "PL_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                if(inp.equals("fl")){
                    //TODO run query
                }
                else{
                    //TODO run query
                }
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
            else if(inp.equals("pc") || inp.equals("bpc")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "PL_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                if(inp.equals("pc")){
                    //TODO run query
                }
                else{
                    //TODO run query
                }
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
            else {
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "PL_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("Stat(uppercase):");
                inp = sc.next();
                //TODO run query
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
        }
        return inp;
    }

    public static String teamSearch(String inp, Scanner sc){
        Boolean go_back = false;
        while (go_back == false) {
            char c = '\n';
            int length = 35;
            char[] chars = new char[length];
            Arrays.fill(chars, c);
            System.out.print(String.valueOf(chars));
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.printf("%26s", "TEAMS");
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.println("Use these commands to explore the team:");
            System.out.println("Precede marked commands [x] with 'b' to just show basic team info");
            System.out.println("   [x]*: Gets all the teams' stats");
            System.out.println("   [x]fl: Gets one team's stats");
            System.out.println("   [x]pc: Compares teams' stats");
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
                    //TODO run query
                }
                else{
                    //TODO run query
                }
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();

            }
            else if(inp.equals("fl") || inp.equals("bfl")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "TM_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                if(inp.equals("fl")){
                    //TODO run query
                }
                else{
                    //TODO run query
                }
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
            else if(inp.equals("pc") || inp.equals("bpc")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "TM_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                if(inp.equals("pc")){
                    //TODO run query
                }
                else{
                    //TODO run query
                }
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
            else {
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "TM_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("Stat(uppercase):");
                inp = sc.next();
                //TODO run query
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
        }
        return inp;
    }

    public static String divisionSearch(String inp, Scanner sc){
        Boolean go_back = false;
        while (go_back == false) {
            char c = '\n';
            int length = 35;
            char[] chars = new char[length];
            Arrays.fill(chars, c);
            System.out.print(String.valueOf(chars));
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.printf("%32s", "DIVISIONS");
            System.out.println(String.join("", Collections.nCopies(50, "*")));
            System.out.println("Use these commands to explore the divisions:");
            System.out.println("   dr: Displays records of specific division");
            System.out.println("   *: Gets all the divisions' info");
            System.out.println("   dd: Gets one division's info");
            System.out.println("   q: quit database");
            System.out.println("   h: return home");
            inp = sc.next();
            if(inp.equals("h")){
                go_back = true;
            }
            else if(inp.equals("q")){
                go_back = true;
            }
            else if(inp.equals("dr")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "DV_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("Chose Division:");
                inp = sc.next();
                //TODO run query
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
            else if(inp.equals("*")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "DV_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                //TODO run query
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
            else if(inp.equals("dd")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "DV_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("Choose division:");
                inp = sc.next();
                //TODO run query
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
        }
        return inp;
    }

    public static String conferenceSearch(String inp, Scanner sc){
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
            System.out.println("   cr: Displays records of specific conferences");
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
            else if(inp.equals("cr")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "CN_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("Chose Conference:");
                inp = sc.next();
                //TODO run query
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
            else if(inp.equals("*")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "CN_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                //TODO run query
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
            else if(inp.equals("cc")){
                Arrays.fill(chars, c);
                System.out.print(String.valueOf(chars));
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.printf("%27s\n", "CN_DATA");
                System.out.println(String.join("", Collections.nCopies(50, "*")));
                System.out.println("Choose conference:");
                inp = sc.next();
                //TODO run query
                System.out.println("\nInput 'cont' to continue:");
                inp = sc.next();
            }
        }
        return inp;
    }

    /**
     * Starts and runs the database
     * @param args: not used but you can use them
     */
    public static void main(String[] args) {
        NBAmain db = new NBAmain();
        Boolean running = true;

        //Hard drive location of the database
        String location = "~/Dropbox/RIT_3rd_Year/SEMESTER2/NBA_DB";
        String user = "scj";
        String password = "password";

        //Create the database connections, basically makes the database
        db.createConnection(location, user, password);
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
            if(s.equals("p")){s = playerSearch(s, scan);}
            else if(s.equals("t")){s = teamSearch(s, scan);}
            else if(s.equals("d")){s = divisionSearch(s, scan);}
            else if(s.equals("c")){s = conferenceSearch(s, scan);}
            if(s.equals("q")){running = false;}
        }
        db.closeConnection();
    }
}
