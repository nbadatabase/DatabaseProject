package nba.fourguysonecode;/**
 * Created by rmiceli on 4/19/2017.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NBAGui extends Application
{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        // Set the title of the window.
        primaryStage.setTitle("NBA Database Viewer");

        // Create a boarder pane and setup each panel we will be using.
        BorderPane borderPane = new BorderPane();

        // Create a new Scene which will be the root for all of the Gui elements.
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);

        // Display the window to the user.
        primaryStage.show();
    }

    private MenuBar buildMenu()
    {
        // Initialize the about menu.
        Menu aboutMenu = new Menu("About");
        aboutMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        // Create a new MenuBar and initialize the menus.
        MenuBar menuBar = new MenuBar();
        return menuBar;
    }
}
