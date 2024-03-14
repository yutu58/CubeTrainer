package application.gui;

import Settings.Settings;
import application.gui.screens.HomeScreen;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainAppWindow extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("pictures/LithiumLogo.png")));
        stage.setTitle("Lithium version " + Settings.VERSION);

        HomeScreen.setup(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return stage;
    }
}
