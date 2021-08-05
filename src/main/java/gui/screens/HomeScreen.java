package gui.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HomeScreen {
    public static void setup(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlURL = HomeScreen.class.getResource("/screens/homeScreen.fxml");
        loader.setLocation(xmlURL);
        Parent root = loader.load();

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
