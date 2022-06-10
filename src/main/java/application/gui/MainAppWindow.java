package application.gui;

import application.gui.screens.HomeScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainAppWindow extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        HomeScreen.setup(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return stage;
    }
}
