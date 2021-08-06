package application.gui.screens;

import application.controllers.SkewbScreenController;
import cubes.skewb.scramblers.SkewbScrambler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class SkewbScreen {
    public static void setup(Stage primaryStage) throws IOException {
        SkewbScrambler.prune();

        FXMLLoader loader = new FXMLLoader();
        URL xmlURL = SkewbScreen.class.getResource("/screens/skewbScreen.fxml");
        loader.setLocation(xmlURL);
        Parent root = loader.load();

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        initEvents(primaryStage.getScene(), loader.getController());

        primaryStage.setMaximized(true);
    }

    //Event listeners
    private static void initEvents(Scene scene, SkewbScreenController controller) {
        AtomicBoolean pressed = new AtomicBoolean(false);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode() == KeyCode.SPACE && !pressed.get()) {
                if (controller.isTimerRunning()) {
                    controller.stopTimer();
                }
                pressed.set(true);
            }
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, ke ->{
            pressed.set(false);
            if (ke.getCode() == KeyCode.SPACE) {
                if (!controller.isTimerRunning()) {
                    controller.setTimerRunning(true);
                    controller.startTimer();
                } else {
                    controller.setTimerRunning(false);
                }
            }
        });
    }
}
