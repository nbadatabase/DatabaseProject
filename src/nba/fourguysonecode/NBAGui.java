package nba.fourguysonecode;/**
 * Created by rmiceli on 4/19/2017.
 */

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import nba.fourguysonecode.objects.*;
import nba.fourguysonecode.tables.*;

import java.util.ArrayList;
import java.util.List;

public class NBAGui extends Application
{
    // Name of the application, used for the window title and alert box titles.
    private final String ApplicationName = "NBA Database Viewer";

    // Column headers for the table views.
    private final String[] ConferencesColumnHeaders = new String[]
            {
                    "Name"
            };
    private final String[] DivisionsColumnHeaders = new String[]
            {
                    "Conference", "Division"
            };
    private final String[] PlayersColumnHeaders = new String[]
            {
                    "Team", "First Name", "Last Name", "Date of Birth", "Games Played",
                    "Total Minutes", "Total Points", "Field Goals Att", "Field Goals Made",
                    "3P Att", "3P Made", "Free Throws Att", "Free Throws Made", "Off Rebounds",
                    "Def Rebounds", "Assists", "Steals", "Blocks", "Turn Overs"
            };
    private final String[] TeamsColumnHeaders = new String[]
            {
                    "Division", "Team Name", "Location", "Wins", "Loses",
                    "Total Points", "Field Goals Att", "Field Goals Made", "3P Att", "3P Made",
                    "Free Throws Att", "Free Throws Made", "Off Rebounds", "Def Rebounds", "Assists",
                    "Steals", "Blocks", "Turn Overs"
            };

    // TableViews used for displaying the database data in the GUI.
    TableView<List<String>> tblConferences = new TableView();
    TableView<List<String>> tblDivisions = new TableView();
    TableView<List<String>> tblPlayers = new TableView();
    TableView<List<String>> tblTeams = new TableView();

    // Observable collections for displaying data in the table view.
    ObservableList<List<String>> conferenceData;
    ObservableList<List<String>> divisionData;
    ObservableList<List<String>> playerData;
    ObservableList<List<String>> teamData;

    // Boolean indicating if the database has been successfully opened.
    private boolean bDatabaseOpened = false;

    // Our H2 database connection.
    private NBADatabase dbConn = new NBADatabase();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        // Set the title of the window.
        primaryStage.setTitle(ApplicationName);

        // Create a boarder pane and setup each panel we will be using.
        BorderPane borderPane = new BorderPane();

        // Set the top pane to the menu bar.
        borderPane.setTop(buildMenuBar());

        // Set the center pane to the tab control.
        borderPane.setCenter(buildTabPane());

        // Create a new Scene which will be the root for all of the Gui elements.
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);

        // Set the size of the stage.
        primaryStage.setMinWidth(900.0);
        primaryStage.setMaxWidth(900.0);
        primaryStage.setMinHeight(500.0);
        primaryStage.setMaxHeight(500.0);

        // Display the window to the user.
        primaryStage.show();
    }

    private void openDatabase()
    {
        // Check if we have already opened the database.
        if (this.bDatabaseOpened == true)
        {
            // The database has already been opened, display an error to the user.
            displayAlert(Alert.AlertType.ERROR,
                    "The database is already open! Please close it before trying to open it again.");
            return;
        }

        // Try to open the database.
        if (dbConn.createConnection() == false)
        {
            // Failed to open or create the database.
            return;
        }

        // Get all of the database data into lists we can use to create the table bindings.
        List<Conference> conferences = ConferenceTable.queryConferenceTable(this.dbConn.getConnection(),
                null, null);
        List<Division> divisions = DivisionTable.queryDivisionTable(this.dbConn.getConnection(),
                null, null);
        List<Player> players = PlayerTable.queryPlayerTable(this.dbConn.getConnection(),
                null, null);
        List<PlayerStats> playerStats = PlayerStatsTable.queryPlayerStatsTable(this.dbConn.getConnection(),
                null, null);
        List<Team> teams = TeamTable.queryTeamTable(this.dbConn.getConnection(),
                null, null);
        List<TeamStats> teamStats = TeamStatsTable.queryTeamStatsTable(this.dbConn.getConnection(),
                null, null);

        // Get a list of players from the Player table and add it to the table view.
        if (buildPlayerDataList(players, teams, playerStats) == false)
        {
            // Failed to initialize the table data.
            displayAlert(Alert.AlertType.ERROR, "Failed to build player data for table view!");
            
            // Close the connection to the database.
            this.dbConn.closeConnection();
            return;
        }

        // Successfully opened the database.
        this.bDatabaseOpened = true;
    }

    private void closeDatabase()
    {
        // Check if the database is already open.
        if (this.bDatabaseOpened == true)
        {
            // TODO
        }
    }

    private MenuBar buildMenuBar()
    {
        // Create the Open menu item which will be used to open the database.
        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openDatabase();
            }
        });

        // Create the Close menu item which will be used to close the database.
        MenuItem closeItem = new MenuItem("Close");
        closeItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeDatabase();
            }
        });

        // Create the Exit menu item which will be used to exit the application.
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        // Initialize the file menu.
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(openItem, closeItem, new SeparatorMenuItem(), exitItem);

        /*
            Since java does not support having a menu with no children with an on action event
            handler, we have to do some ghetto hacks to make the about menu work. The work around
            consists of creating a label with a mouse click event handler. Then we add the label
            to the about menu, and add the about menu to the menu bar. The result is that when
            you click the about menu the mouse click for the label is fired and gives the illusion
            that the about menu does something when you click on it.
         */
        Label hackyAboutLabel = new Label("About");
        hackyAboutLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Display a message box with the about info.
                displayAlert(Alert.AlertType.INFORMATION, "Created for CSCI-320");
            }
        });

        // Initialize the about menu.
        Menu aboutMenu = new Menu();
        aboutMenu.setGraphic(hackyAboutLabel);

        // Create a new MenuBar and initialize the menus.
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, aboutMenu);

        // Return the menubar object.
        return menuBar;
    }

    private TabPane buildTabPane()
    {
        // Initialize the list views that will be used to display all of the data.
        //buildTableView(this.tblConferences, ConferencesColumnHeaders);
        //buildTableView(this.tblDivisions, DivisionsColumnHeaders);
        buildTableView(this.tblPlayers, PlayersColumnHeaders);
        //buildTableView(this.tblTeams, TeamsColumnHeaders, null);

        // Create a new tab page for each of the tables we will be displaying.
        Tab conferencesTab = new Tab("Conferences");
        conferencesTab.setContent(this.tblConferences);

        Tab divisionsTab = new Tab("Divisions");
        divisionsTab.setContent(this.tblDivisions);

        Tab playersTab = new Tab("Players");
        playersTab.setContent(this.tblPlayers);

        Tab teamsTab = new Tab("Teams");
        teamsTab.setContent(this.tblTeams);

        // Create a new TabPane and add all of the tab pages to it.
        TabPane pane = new TabPane();
        pane.getTabs().addAll(conferencesTab, divisionsTab, playersTab, teamsTab);

        // Disable the ability to close the tab pages.
        pane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Add scroll bars to the tab pane so the cell contents can fit.
        //pane.

        // Return the tab pane.
        return pane;
    }

    private void buildTableView(TableView table, String[] columnHeaders)
    {
        // Setup the table.
        table.setColumnResizePolicy(param -> true);

        // Loop through all of the column headers and add each one to the table.
        for (int i = 0; i < columnHeaders.length; i++)
        {
            // Create a new constant for the index of this column.
            final int ColumnIndex = i;

            // Create a new column header and add it to the table.
            TableColumn<List<String>, String> column = new TableColumn(columnHeaders[i]);
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>()
            {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> param)
                {
                    return new SimpleStringProperty(param.getValue().get(ColumnIndex));
                }
            });

            // Add the column header to the table.
            table.getColumns().add(column);
        }
    }

    private void displayAlert(Alert.AlertType type, String text)
    {
        // Create a new alert using the info provided.
        Alert msg = new Alert(type, text);
        msg.setTitle(ApplicationName);
        msg.setHeaderText("");

        // Display the alert to the user.
        msg.show();
    }

    private boolean buildPlayerDataList(List<Player> players, List<Team> teams, List<PlayerStats> playerStats)
    {
        // Check that the arguments are valid.
        if (players == null || players.size() == 0 || teams == null || 
                teams.size() == 0 || playerStats == null || playerStats.size() == 0)
        {
            // There is not enough data to process.
            return false;
        }

        // Initialize the list to hold the player data.
        this.playerData = FXCollections.observableArrayList(new ArrayList<List<String>>());

        // Loop through all of the players and create lists of strings based on their information.
        for (int i = 0; i < players.size(); i++)
        {
            // Create a new list to hold this players data.
            List<String> data = new ArrayList<String>();

            // Get the team and player stats object associated with this player.
            Player player = players.get(i);
            Team playerTeam = teams.stream().filter(
                    team -> team.getTeam_id() == player.getTeam_id()).findFirst().orElse(null);
            PlayerStats stats = playerStats.stream().filter(
                    stat -> stat.getPlayer_id() == player.getPlayer_id()).findFirst().orElse(null);

            // Check to make sure the team and player stats are valid.
            if (playerTeam == null || stats == null)
            {
                // There is a broken foreign key in the database.
                displayAlert(Alert.AlertType.ERROR, "There is a broken foreign key in the database for player_id: "
                        + player.getPlayer_id());
                return false;
            }

            // Add the players data to the list.
            data.add(playerTeam.getTeam_name());
            data.add(player.getFirst_name());
            data.add(player.getLast_name());
            data.add(player.getDob());
            data.add(String.valueOf(stats.getGames_played()));
            data.add(String.valueOf(stats.getTot_mins()));
            data.add(String.valueOf(stats.getTot_pts()));
            data.add(String.valueOf(stats.getFg_att()));
            data.add(String.valueOf(stats.getFg_made()));
            data.add(String.valueOf(stats.getThree_att()));
            data.add(String.valueOf(stats.getThree_made()));
            data.add(String.valueOf(stats.getFree_att()));
            data.add(String.valueOf(stats.getFree_made()));
            data.add(String.valueOf(stats.getOff_rebound()));
            data.add(String.valueOf(stats.getDef_rebound()));
            data.add(String.valueOf(stats.getAssists()));
            data.add(String.valueOf(stats.getSteals()));
            data.add(String.valueOf(stats.getBlocks()));
            data.add(String.valueOf(stats.getTurnovers()));

            // Add the current player's data to the players list.
            playerData.add(data);
        }

        // Set the table view to use the data we just created.
        this.tblPlayers.setItems(this.playerData);
        return true;
    }
}
