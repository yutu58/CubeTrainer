package application.gui.screens;

import application.controllers.Tx2ScreenController;
import application.gui.subscreens.tx2.Tx2AlgTrainer;
import cubes.tx2.solvers.Tx2Scrambler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class Tx2Screen {
    public static void setup(Stage primaryStage) throws IOException {
        Tx2Scrambler.prune();

        FXMLLoader loader = new FXMLLoader();
        URL xmlURL = SkewbScreen.class.getResource("/screens/2x2Screen.fxml");
        loader.setLocation(xmlURL);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(SkewbScreen.class.getResource("/stylesheets/2x2Screen.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setMaximized(true);

        initEvents(primaryStage.getScene(), loader.getController());
    }

    //Event listeners
    private static void initEvents(Scene scene, Tx2ScreenController controller) {
        AtomicBoolean spacePressed = new AtomicBoolean(false);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            Pane subMenu = controller.getSubMenu();

            if (subMenu instanceof Tx2AlgTrainer) {
                Tx2AlgTrainer s = (Tx2AlgTrainer) subMenu;
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

            if (subMenu instanceof Tx2AlgTrainer) {
                if (ke.getCode() == KeyCode.SPACE) {
                    spacePressed.set(false);
                    Tx2AlgTrainer s = (Tx2AlgTrainer) subMenu;
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
