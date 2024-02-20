package application.gui.screens;

import application.controllers.SkewbScreenController;
import application.gui.subscreens.skewb.Skewb1lookTrainer;
import application.gui.subscreens.skewb.SkewbAlgTrainer;
import cubes.skewb.solvers.SkewbScrambler;
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

public class SkewbScreen {
    static Scene scene;

    public static void setup(Stage primaryStage) throws IOException {
        SkewbScrambler.prune();

        FXMLLoader loader = new FXMLLoader();
        URL xmlURL = SkewbScreen.class.getResource("/screens/skewbScreen.fxml");
        loader.setLocation(xmlURL);
        Parent root = loader.load();

        scene = new Scene(root);
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

                if (s.getPoolTextInput().isFocused()) return;

                if (ke.getCode() == KeyCode.SPACE && !spacePressed.get()) {
                    if (s.isTimerRunning()) {
                        s.stopTimer();
                    }
                    spacePressed.set(true);
                    ke.consume();
                }
            } else if (subMenu instanceof Skewb1lookTrainer) {
                Skewb1lookTrainer s = (Skewb1lookTrainer) subMenu;
                if (!s.getSetupMoves().isFocused() && !s.getNewPoolNameField().isFocused()) {
                    s.updateScramble();
                    ke.consume();
                }
            }
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, ke ->{
            Pane subMenu = controller.getSubMenu();

            if (subMenu instanceof SkewbAlgTrainer) {
                if (ke.getCode() == KeyCode.SPACE) {
                    SkewbAlgTrainer s = (SkewbAlgTrainer) subMenu;
                    if (s.getPoolTextInput().isFocused()) return;

                    spacePressed.set(false);

                    if (!s.isTimerRunning()) {
                        s.setTimerRunning(true);
                        s.startTimer();
                    } else {
                        s.setTimerRunning(false);
                    }
                    ke.consume();
                }
            }

        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, ke ->{
            Pane subMenu = controller.getSubMenu();

            if (subMenu instanceof SkewbAlgTrainer) {
                if (ke.getCode() == KeyCode.P) {
                    SkewbAlgTrainer s = (SkewbAlgTrainer) subMenu;

                    if (s.getPoolTextInput().isFocused()) return;


                    if (!s.isInExamMode()) {
                        s.startExamMode();
                    } else {
                        s.setInExamMode(false);
                    }
                    ke.consume();
                }
            }

        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, ke ->{
            Pane subMenu = controller.getSubMenu();

            if (subMenu instanceof SkewbAlgTrainer) {
                if (ke.getCode() == KeyCode.LEFT) {
                    SkewbAlgTrainer s = (SkewbAlgTrainer) subMenu;

                    if (s.isInExamMode()) {
                        s.passExamQuestion();
                    }

                    ke.consume();
                }
            }
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, ke ->{
            Pane subMenu = controller.getSubMenu();

            if (subMenu instanceof SkewbAlgTrainer) {
                if (ke.getCode() == KeyCode.RIGHT) {
                    SkewbAlgTrainer s = (SkewbAlgTrainer) subMenu;

                    if (s.getPoolTextInput().isFocused()) return;

                    if (s.isInExamMode()) {
                        s.failExamQuestion();
                    }
                    ke.consume();
                }
            }
        });
    }
}
