package nba.fourguysonecode;/**
 * Created by rmiceli on 4/19/2017.
 */

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import nba.fourguysonecode.objects.*;
import nba.fourguysonecode.tables.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NBAGui extends Application
{
    // Name of the application, used for the window title and alert box titles.
    private final String ApplicationName = "NBA Database Viewer";

    // Column headers for the table views.
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
    TableView<List<String>> tblInfo;

    // Observable collections for displaying data in the table view.
    ObservableList<List<String>> tableData = FXCollections.observableArrayList();

    // Combo boxes for filter criteria.
    ComboBox<String> cmbConference;
    ComboBox<String> cmbDivision;
    ComboBox<String> cmbTeam;

    // Reset button for filter criteria.
    Button resetButton;

    // Observable lists for the search criteria options. These allow us to just update the list
    // contents without having to touch the combobox control.
    ObservableList<String> lstConferenceOptions = FXCollections.observableArrayList();
    ObservableList<String> lstDivisionOptions = FXCollections.observableArrayList();
    ObservableList<String> lstTeamOptions = FXCollections.observableArrayList();

    // Boolean indicating if the database has been successfully opened.
    private boolean bDatabaseOpened = false;

    // Our H2 database connection.
    private NBADatabase dbConn = new NBADatabase();

    // Lists of data retrieved from the database.
    List<Conference> dbConferences;
    List<Division> dbDivisions;
    List<Player> dbPlayers;
    List<PlayerStats> dbPlayerStats;
    List<Team> dbTeams;
    List<TeamStats> dbTeamStats;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        // Set the title of the window.
        primaryStage.setTitle(ApplicationName);

        // Create a new VBox to hold the main GUI elements.
        VBox vBox = new VBox();

        // Set the top element to the menu bar.
        vBox.getChildren().add(buildMenuBar());

        // Set the next element to be the filter box.
        vBox.getChildren().add(buildFilterBox());

        // Initialize the table view.
        this.tblInfo = new TableView();
        this.tblInfo.setColumnResizePolicy(param -> true);

        // Set the next element to be the data view box.
        vBox.getChildren().add(this.tblInfo);

        // Create a new Scene which will be the root for all of the Gui elements.
        Scene scene = new Scene(vBox);
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
        this.dbConferences = ConferenceTable.queryConferenceTable(this.dbConn.getConnection(),
                null, null);
        this.dbDivisions = DivisionTable.queryDivisionTable(this.dbConn.getConnection(),
                null, null);
        this.dbPlayers = PlayerTable.queryPlayerTable(this.dbConn.getConnection(),
                null, null);
        this.dbPlayerStats = PlayerStatsTable.queryPlayerStatsTable(this.dbConn.getConnection(),
                null, null);
        this.dbTeams = TeamTable.queryTeamTable(this.dbConn.getConnection(),
                null, null);
        this.dbTeamStats = TeamStatsTable.queryTeamStatsTable(this.dbConn.getConnection(),
                null, null);

        // Build the filter lists.
        buildFilterLists();

        // Enable the filter boxes.
        this.cmbConference.setDisable(false);
        this.cmbDivision.setDisable(false);
        this.cmbTeam.setDisable(false);
        this.resetButton.setDisable(false);

        // Successfully opened the database.
        this.bDatabaseOpened = true;
    }

    private void closeDatabase()
    {
        // Check if the database is already open.
        if (this.bDatabaseOpened == true)
        {
            // Disable all of the filter combo boxes.
            this.cmbConference.setDisable(true);
            this.cmbDivision.setDisable(true);
            this.cmbTeam.setDisable(true);
            this.resetButton.setDisable(true);

            // Close the database.
            this.dbConn.closeConnection();

            // Mark the database as being closed.
            this.bDatabaseOpened = false;
        }
    }

    private MenuBar buildMenuBar()
    {
        // Create the Open menu item which will be used to open the database.
        MenuItem openItem = new MenuItem("Open Database");
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openDatabase();
            }
        });

        // Create the Close menu item which will be used to close the database.
        MenuItem closeItem = new MenuItem("Close Database");
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
        hackyAboutLabel.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
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

    private void buildFilterLists()
    {
        // Reset the filter lists.
        this.lstConferenceOptions.clear();
        this.lstDivisionOptions.clear();
        this.lstTeamOptions.clear();

        // Loop through all of the conferences in the database and add each one to the observable list.
        for (int i = 0; i < this.dbConferences.size(); i++)
        {
            // Add the conference to the observable list.
            this.lstConferenceOptions.add(this.dbConferences.get(i).getConf_name());
        }

        // Loop through all of the divisions in the database and add each one to the observable list.
        for (int i = 0; i < this.dbDivisions.size(); i++)
        {
            // Add the division to the observable list.
            this.lstDivisionOptions.add(this.dbDivisions.get(i).getDiv_name());
        }

        // Loop through all of the teams in the database and add each one to the observable list.
        for (int i = 0; i < this.dbTeams.size(); i++)
        {
            // Add the teams to the observable list.
            this.lstTeamOptions.add(this.dbTeams.get(i).getTeam_name());
        }

        // Sort the lists alphabetically.
        Collections.sort(this.lstConferenceOptions);
        Collections.sort(this.lstDivisionOptions);
        Collections.sort(this.lstTeamOptions);
    }

    private GridPane buildFilterBox()
    {
        // Create a new FlowPane to hold the filter criteria controls.
        GridPane gridPane = new GridPane();

        // Setup the row constraints.
        gridPane.getRowConstraints().add(new RowConstraints(50));

        // Setup the column constraints.
        ColumnConstraints colConst = new ColumnConstraints(200);
        colConst.setHalignment(HPos.CENTER);
        gridPane.getColumnConstraints().addAll(colConst, colConst, colConst);

        // Create the conference filter.
        this.cmbConference = new ComboBox<>(this.lstConferenceOptions);
        this.cmbConference.setPromptText("Conference");
        this.cmbConference.setDisable(true);
        this.cmbConference.setMinWidth(150);
        this.cmbConference.valueProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                System.out.println("Conference index changed");

                // Check to make sure a new filter was actually set.
                if (oldValue == null || newValue != null || oldValue.equals(newValue) == false)
                {
                    // Apply the new conference filter.
                    setConferenceFilter(newValue);
                }
            }
        });

        // Create the division filter.
        this.cmbDivision = new ComboBox<>(this.lstDivisionOptions);
        this.cmbDivision.setPromptText("Division");
        this.cmbDivision.setDisable(true);
        this.cmbDivision.setMinWidth(150);
        this.cmbDivision.valueProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                System.out.println("Division index changed");

                // Check to make sure the value has actually changed.
                if (oldValue == null || newValue != null || oldValue.equals(newValue) == false)
                {
                    // Apply the new division filter.
                    setDivisionFilter(newValue);
                }
            }
        });

        // Create the team filter.
        this.cmbTeam = new ComboBox<>(this.lstTeamOptions);
        this.cmbTeam.setPromptText("Team");
        this.cmbTeam.setDisable(true);
        this.cmbTeam.setMinWidth(150);
        this.cmbTeam.valueProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                System.out.println("Team index changed");

                // Check to make sure the value has actually changed.
                if (oldValue == null || newValue != null || oldValue.equals(newValue) == false)
                {
                    // Apply the new team filter.
                    setTeamFilter(newValue);
                }
            }
        });

        // Create a button that will reset the filter criteria.
        this.resetButton = new Button("Reset");
        this.resetButton.setDisable(true);
        this.resetButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                // Reset all of the filter options.
                buildFilterLists();

                // Clear all of the items and column headers from the table view.
                tblInfo.getColumns().clear();
                tblInfo.getItems().clear();
            }
        });

        // Add the filter comboboxes to the grid pane.
        gridPane.add(this.cmbConference, 0, 0);
        gridPane.add(this.cmbDivision, 1, 0);
        gridPane.add(this.cmbTeam, 2, 0);
        gridPane.add(this.resetButton, 3, 0);

        // Return the newly constructed grid pane object.
        return gridPane;
    }

    private void setTableViewColumns(String[] columnHeaders)
    {
        // Clear any existing column headers.
        this.tblInfo.getColumns().clear();

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
            this.tblInfo.getColumns().add(column);
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

    private void updateDataView()
    {
        // Get the selections for the filter combo boxes.
        String confName = this.cmbConference.getValue();
        String divName = this.cmbDivision.getValue();
        String teamName = this.cmbTeam.getValue();

        // Clear the items from the table view.
        this.tblInfo.getItems().clear();
        this.tableData.clear();

        // Clear the column headers from the table view.
        this.tblInfo.getColumns().clear();

        // If the team name is set we can ignore all the other filter criteria.
        if (teamName != null)
        {
            // Set the column headers to the player stats column headers.
            setTableViewColumns(PlayersColumnHeaders);

            // Get the team set by the team filter.
            Team team = this.dbTeams.stream().filter(
                    t -> t.getTeam_name().equals(teamName) == true).findFirst().orElse(null);

            // Build a list of players that belong to the specified team.
            Player[] players = this.dbPlayers.stream().filter(
                    p -> p.getTeam_id() == team.getTeam_id()).toArray(size -> new Player[size]);

            // Loop through all of the players and add each one to the table view data list.
            for (int i = 0; i < players.length; i++)
            {
                // Get the PlayerStats object for this player.
                final int index = i;
                PlayerStats stats = this.dbPlayerStats.stream().filter(
                        s -> s.getPlayer_id() == players[index].getPlayer_id()).findFirst().orElse(null);
                if (stats == null)
                {
                    // There is a broken foreign key in the database.
                    displayAlert(Alert.AlertType.ERROR, "There is a broken foreign key in the database for player_id: "
                            + players[i].getPlayer_id());
                    return;
                }

                // Add the players data to the list.
                List<String> data = new ArrayList<String>();
                data.add(team.getTeam_name());
                data.add(players[i].getFirst_name());
                data.add(players[i].getLast_name());
                data.add(players[i].getDob());
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

                // Add the data to the list.
                this.tableData.add(data);
            }
        }
        else if (confName != null || divName != null)
        {
            // Set the column headers for the table view.
            setTableViewColumns(TeamsColumnHeaders);

            // Build a list of teams to display based on the filter criteria that was used.
            Team[] teams = null;
            if (divName != null)
            {
                // Get division set by the division filter.
                Division division = this.dbDivisions.stream().filter(
                        d -> d.getDiv_name().equals(divName) == true).findFirst().orElse(null);

                // Get a list of teams that belong to this division.
                teams = this.dbTeams.stream().filter(
                        t -> t.getDiv_id() == division.getDiv_id()).toArray(size -> new Team[size]);
            }
            else
            {
                // Get the conference set by the conference filter.
                Conference conf = this.dbConferences.stream().filter(
                        c -> c.getConf_name().equals(confName) == true).findFirst().orElse(null);

                // Get a list of divisions that belong to this conference.
                Division[] divisions = this.dbDivisions.stream().filter(
                        d -> d.getConf_id() == conf.getConf_id()).toArray(size -> new Division[size]);

                // Loop of all of the divisions and get a list of teams for each one.
                List<Team> teamList = new ArrayList<Team>();
                for (int i = 0; i < divisions.length; i++)
                {
                    // Get a list of teams that belong to this divisions.
                    final int index = i;
                    Team[] tmpTeams = this.dbTeams.stream().filter(
                            t -> t.getDiv_id() == divisions[index].getDiv_id()).toArray(size -> new Team[size]);

                    // Add the teams to the team list.
                    for (int x = 0; x < tmpTeams.length; x++)
                        teamList.add(tmpTeams[x]);
                }

                // Set the team array.
                teams = teamList.toArray(new Team[0]); // Why is this a thing? And how does this even work???
            }

            // Loop through all of the teams in the teams list and add each one to the data view.
            for (int i = 0; i < teams.length; i++)
            {
                // Get the team stats object for this team.
                final Team team = teams[i];
                TeamStats stats = this.dbTeamStats.stream().filter(
                        s -> s.getTeam_id() == team.getTeam_id()).findFirst().orElse(null);
                Division division = this.dbDivisions.stream().filter(
                        d -> d.getDiv_id() == team.getDiv_id()).findFirst().orElse(null);

                // Create a new list for the team data.
                List<String> data = new ArrayList<String>();
                data.add(division.getDiv_name());
                data.add(team.getTeam_name());
                data.add(team.getLocation());
                data.add(String.valueOf(team.getWin()));
                data.add(String.valueOf(team.getLoss()));
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

                // Add the data to the list.
                this.tableData.add(data);
            }
        }

        // Add the data list to the table view.
        this.tblInfo.getItems().addAll(this.tableData);
    }

    private void setConferenceFilter(String filter)
    {
        // Clear the items in the other two filters.
        this.lstDivisionOptions.clear();
        this.lstTeamOptions.clear();

        // Get the conference object associated with the selected item.
        Conference conf = this.dbConferences.stream().filter(
                c -> c.getConf_name().equals(filter)).findFirst().orElse(null);
        if (conf == null)
            return;

        // Build a list of divisions for this conference.
        Division[] divisions = this.dbDivisions.stream().filter(
                d -> d.getConf_id() == conf.getConf_id()).toArray(size -> new Division[size]);

        // Loop through all of the divisions and add each one to the combo-box.
        for (int i = 0; i < divisions.length; i++)
        {
            // Add the current division to the divisions combo box.
            this.lstDivisionOptions.add(divisions[i].getDiv_name());

            // Build a list of all the teams for this division.
            final int index = i;
            Team[] teams = this.dbTeams.stream().filter(
                    t -> t.getDiv_id() == divisions[index].getDiv_id()).toArray(size -> new Team[size]);

            // Loop through all of the teams and add each one to the teams list.
            for (int x = 0; x < teams.length; x++)
                this.lstTeamOptions.add(teams[x].getTeam_name());
        }

        // Sort both lists.
        Collections.sort(this.lstDivisionOptions);
        Collections.sort(this.lstTeamOptions);

        // Update the data view.
        updateDataView();
    }

    private void setDivisionFilter(String filter)
    {
        // Clear the teams filter list.
        this.lstTeamOptions.clear();

        // Get the division object associated with this filter.
        Division division = this.dbDivisions.stream().filter(
                d -> d.getDiv_name().equals(filter) == true).findFirst().orElse(null);
        if (division == null)
            return;

        // Get a list of teams that belong to this division.
        Team[] teams = this.dbTeams.stream().filter(
                t -> t.getDiv_id() == division.getDiv_id()).toArray(size -> new Team[size]);

        // Loop through all of the teams and add each one to the filter list.
        for (int i = 0; i < teams.length; i++)
        {
            // Add the team to the filter list.
            this.lstTeamOptions.add(teams[i].getTeam_name());
        }

        // Sort the list.
        Collections.sort(this.lstTeamOptions);

        // Update the data view.
        updateDataView();
    }

    private void setTeamFilter(String filter)
    {
        // Update the data view.
        updateDataView();
    }

    /*private boolean buildPlayerDataList(List<Player> players, List<Team> teams, List<PlayerStats> playerStats)
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
    }*/
}
