package application.gui.subscreens;

import application.controllers.SkewbScreenController;
import application.gui.MainAppWindow;
import application.gui.elements.AlgSetElement;
import application.gui.screens.HomeScreen;
import cubes.skewb.SkewbState;
import cubes.skewb.data.L2LCase;
import cubes.skewb.data.L2LSet;
import cubes.skewb.data.SkewbL2LReader;
import cubes.skewb.solvers.SkewbScrambler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import static Settings.Settings.*;

public class SkewbAlgTrainer extends BorderPane implements Initializable {

    @FXML
    private Label scramble;

    @FXML
    private Label timer;

    @FXML
    private GridPane checkBoxPane;

    @FXML
    private VBox algVBox;

    private SkewbScreenController skewbScreenController;

    private boolean timerRunning;

    private long sysTimeMilis;

    private List<L2LCase> selectedCases;

    private List<Label> menus;

    public SkewbAlgTrainer(SkewbScreenController skewbScreenController) {
        this.skewbScreenController = skewbScreenController;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/subscreens/skewbAlgTrainer.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            System.out.println("There was a problem loading the FXML");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedCases = new ArrayList<>();
        timerRunning = false;

        SkewbL2LReader reader = new SkewbL2LReader();
        List<L2LSet> sets;
        try {
            sets = reader.read();
            if (sets == null) {
                throw new IOException();
            }
        } catch (IOException e) {
            backToHome();
            System.out.println("No sets could be found");
            return;
        }

        int x = 0;
        int y = 1; //y=0 is reserved for the labels

        for (L2LSet set : sets) {
            //Create checkboxes for the set
            CheckBox checkBox = new CheckBox();

//            if (set.wasSelected()) {
//                checkBox.setSelected(true);
//            }

            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    selectedCases.addAll(set.getCases());
                } else {
                    selectedCases.removeAll(set.getCases());
                }
                if (AUTO_REMOVE_FOCUS) {
                    skewbScreenController.getScreen().requestFocus();
                }
            });
            checkBoxPane.add(checkBox, x, y);
            Label setName = new Label();
            setName.setText(set.getName());
            checkBoxPane.add(setName, x + 1, y);

            x += 2;
            if (x == 6) {
                checkBoxPane.addRow(1);
                RowConstraints rc = new RowConstraints();
                rc.setPrefHeight(30.0);
                checkBoxPane.getRowConstraints().add(rc);
                y += 1;
                x = 0;
            }

            //Create alg window on the right
            AlgSetElement ase = new AlgSetElement(set, skewbScreenController);
            if (AUTO_REMOVE_FOCUS) {
                ase.expandedProperty().addListener(((observable, oldValue, newValue) -> skewbScreenController.getScreen().requestFocus()));
            }
            algVBox.getChildren().add(ase);
            Platform.runLater(() -> skewbScreenController.getScreen().requestFocus());


        }
    }

    private void backToHome() {
        try {
            HomeScreen.setup(MainAppWindow.getPrimaryStage());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void startTimer() {
        timer.setText("Running");
//        timerRunning = true; //Now changed in the eventListener
        sysTimeMilis = System.currentTimeMillis();
    }

    public void stopTimer() {
//        timerRunning = false; //Now changed in the eventListener
        long time = System.currentTimeMillis() - sysTimeMilis;
        timer.setText(String.format("%.2f", (double) time / 1000));

        //Add time to average of the alg using
        String newScramble = "Select a set";

        if (selectedCases.size() > 0) {
            newScramble = generateNewScramble();
            if (PREVENT_SAME_SCRAMBLE) {
                int tries = 0;              //To prevent infinite loops for some reason
                while (newScramble.equals(scramble.getText()) && tries < 100) {
                    newScramble = generateNewScramble();
                    tries++;
                }
            }
        }
        scramble.setText(newScramble);
    }

    private String generateNewScramble() {
        //Select random case
        if (selectedCases.size() == 0) {
            return "Select a set";
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(0, selectedCases.size());
        int randomAmount = ThreadLocalRandom.current().nextInt(1, AMOUNT_RANDOM_SCRAMBLES + 1);
        return SkewbScrambler.stateToScrambler(new SkewbState(selectedCases.get(randomIndex).getPattern()), randomAmount);
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
    }


}
