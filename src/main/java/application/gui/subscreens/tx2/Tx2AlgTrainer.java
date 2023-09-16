package application.gui.subscreens.tx2;

import application.controllers.Tx2ScreenController;
import application.gui.MainAppWindow;
import application.gui.screens.HomeScreen;
import cubes.Case;
import cubes.tx2.Tx2State;
import cubes.tx2.data.Tx2CaseReader;
import cubes.tx2.data.Tx2Set;
import cubes.tx2.solvers.Tx2Scrambler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import static Settings.Settings.*;

public class Tx2AlgTrainer extends BorderPane implements Initializable {

    @FXML
    private Label scramble;

    @FXML
    private Label timer;

    @FXML
    private GridPane checkBoxPane;

    @FXML
    private VBox algVBox;

    private Tx2ScreenController tx2ScreenController;

    private boolean timerRunning;

    private long sysTimeMilis;

    private List<Case> selectedCases;

    private List<Label> menus;

    public Tx2AlgTrainer(Tx2ScreenController tx2ScreenController) {
        this.tx2ScreenController = tx2ScreenController;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/subscreens/2x2/2x2AlgTrainer.fxml"));
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

        Tx2CaseReader reader = new Tx2CaseReader();
        List<Tx2Set> sets;
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

        for (Tx2Set set : sets) {
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
                    tx2ScreenController.getScreen().requestFocus();
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
//            AlgSetElement ase = new AlgSetElement(set, tx2ScreenController);
//            if (AUTO_REMOVE_FOCUS) {
//                ase.expandedProperty().addListener(((observable, oldValue, newValue) -> tx2ScreenController.getScreen().requestFocus()));
//            }
//            algVBox.getChildren().add(ase);
//            Platform.runLater(() -> tx2ScreenController.getScreen().requestFocus());


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
        return Tx2Scrambler.stateToScrambler(new Tx2State(selectedCases.get(randomIndex).getPattern()), randomAmount,
                RANDOM_AUF, RANDOM_PRE_AUF);
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
    }


}
