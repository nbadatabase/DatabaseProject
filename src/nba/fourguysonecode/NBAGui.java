package nba.fourguysonecode;/**
 * Created by rmiceli on 4/19/2017.
 */

import javafx.application.Application;
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
import nba.fourguysonecode.objects.Conference;
import nba.fourguysonecode.tables.ConferenceTable;

import java.util.List;

public class NBAGui extends Application
{
    // Name of the application, used for the window title and alert box titles.
    private final String ApplicationName = "NBA Database Viewer";

    // Column headers for the table views.
    private final String[][] ConferencesColumnHeaders = new String[][]
            {
                    { "Name", "conf_name" }
            };
    private final String[][] DivisionsColumnHeaders = new String[][]
            {
                    { "Conference", "conf_id" },
                    { "Division", "div_name" }
            };
    private final String[][] PlayersColumnHeaders = new String[][]
            {
                    { "Team", "" },
                    { "First Name", "" },
                    { "Last Name", "" },
                    { "Date of Birth", "" },
                    { "Games Played", "" },
                    { "Total Minutes", "" },
                    { "Total Points", "" },
                    { "Field Goals Att", "" },
                    { "Field Goals Made", "" },
                    { "3P Att", "" },
                    { "3P Made", "" },
                    { "Free Throws Att", "" },
                    { "Free Throws Made", "" },
                    { "Off Rebounds", "" },
                    { "Def Rebounds", "" },
                    { "Assists", "" },
                    { "Steals", "" },
                    { "Blocks", "" },
                    { "Turn Overs", "" }
            };
    private final String[] TeamsColumnHeaders = new String[]
            {
                    "Division", "Team Name", "Location", "Wins", "Loses",
                    "Total Points", "Field Goals Att", "Field Goals Made", "3P Att", "3P Made",
                    "Free Throws Att", "Free Throws Made", "Off Rebounds", "Def Rebounds", "Assists",
                    "Steals", "Blocks", "Turn Overs"
            };

    // TableViews used for displaying the database data in the GUI.
    TableView tblConferences = new TableView();
    TableView tblDivisions = new TableView();
    TableView tblPlayers = new TableView();
    TableView tblTeams = new TableView();

    ObservableList<Conference> conferenceData;

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

        // Get a list of conferences from the Conference table and add them to the table view.
        List<Conference> conferences = ConferenceTable.queryConferenceTable(this.dbConn.getConnection(),
                null, null);
        if (conferences != null)
        {
            // Create an observable collection from the conference data.
            this.conferenceData = FXCollections.observableArrayList(conferences);
            this.tblConferences.setItems(this.conferenceData);
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
        buildTableView(this.tblConferences, ConferencesColumnHeaders);
        //buildTableView(this.tblDivisions, DivisionsColumnHeaders, null);
        //buildTableView(this.tblPlayers, PlayersColumnHeaders, null);
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

    private void buildTableView(TableView table, String[][] columnHeaders)
    {
        // Loop through all of the column headers and add each one to the table.
        for (int i = 0; i < columnHeaders.length; i++)
        {
            // Create a new column header and add it to the table.
            TableColumn column = new TableColumn(columnHeaders[i][0]);
            column.setCellValueFactory(new PropertyValueFactory(columnHeaders[i][1]));
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
}
