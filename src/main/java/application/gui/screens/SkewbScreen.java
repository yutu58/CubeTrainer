package application.gui.screens;

import application.controllers.SkewbScreenController;
import application.gui.subscreens.SkewbAlgTrainer;
import cubes.skewb.scramblers.SkewbScrambler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
        scene.getStylesheets().add(SkewbScreen.class.getResource("/stylesheets/skewbScreen.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setMaximized(true);

        initEvents(primaryStage.getScene(), loader.getController());
    }

    //Event listeners
    private static void initEvents(Scene scene, SkewbScreenController controller) {
        AtomicBoolean spacePressed = new AtomicBoolean(false);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            Pane subMenu = controller.getSubMenu();

            if (subMenu instanceof SkewbAlgTrainer) {
                SkewbAlgTrainer s = (SkewbAlgTrainer) subMenu;
                if (ke.getCode() == KeyCode.SPACE && !spacePressed.get()) {
                    if (s.isTimerRunning()) {
                        s.stopTimer();
                    }
                    spacePressed.set(true);
                }
            }
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, ke ->{
            Pane subMenu = controller.getSubMenu();

            if (subMenu instanceof SkewbAlgTrainer) {
                if (ke.getCode() == KeyCode.SPACE) {
                    spacePressed.set(false);
                    SkewbAlgTrainer s = (SkewbAlgTrainer) subMenu;
                    if (!s.isTimerRunning()) {
                        s.setTimerRunning(true);
                        s.startTimer();
                    } else {
                        s.setTimerRunning(false);
                    }
                }
            }
        });
    }
}
