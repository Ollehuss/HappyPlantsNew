package se.myhappyplants.client.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import se.myhappyplants.client.model.RootName;
import se.myhappyplants.client.view.ConfirmationBox;

import java.io.IOException;
import java.util.Objects;

/**
 * JavaFX Application Main class
 * If javaFX not installed, execute maven goals:
 * 1.  mvn javafx:compile
 * 2.  mvn javafx:run
 * Created by: Christopher O'Driscoll, Eric Simonsson
 * Updated by: Linn Borgström, 2021-05-13
 */
public class StartClient extends Application {

    private static Scene scene;
    private static Stage window;

    public static Stage getStage() {
        return window;
    }

    public static RootName getRoot() {
        return RootName.valueOf(scene.getRoot().getId());
    }


    /**
     * Starts the application by opening window. Method handles close on request.
     *
     * @param stage instance of Stage to start
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        window.setResizable(false);
        window.initStyle(StageStyle.DECORATED);
        window.setOnCloseRequest(action -> {
            action.consume();
            close();
        });
        scene = new Scene(loadFXML(RootName.loginPane.toString()), 1010, 640);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Stylesheet.css")).toExternalForm());
        window.setScene(scene);
        window.show();
    }

    /**
     * Method handles close on request.
     */
    private String close() {
        if (ConfirmationBox.display("Exit", "Are you sure?")) {
            window.close();
        }
        return "Window application closed";
    }

    /**
     * Method to set the root
     * @param fxml to set
     * @throws IOException
     */
    public static String setRoot(String fxml) throws IOException {
        if (scene == null) {
            scene = new Scene(loadFXML(fxml));
        } else {
            scene.setRoot(loadFXML(fxml));
        }
        return "Root set to ";
    }

    /**
     * Method to load the right fxml-file
     * @param fxml to load
     * @return
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Alternative run method (not needed)
     * @param args
     **/
    public static void main(String[] args) {
        launch();
    }
}