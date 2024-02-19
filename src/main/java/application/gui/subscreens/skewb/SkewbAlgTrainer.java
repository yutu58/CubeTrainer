package application.gui.subscreens.skewb;

import application.controllers.SkewbScreenController;
import application.gui.MainAppWindow;
import application.gui.elements.AlgPoolElement;
import application.gui.elements.AlgSetElement;
import application.gui.screens.HomeScreen;
import cubes.Case;
import cubes.skewb.SkewbState;
import cubes.skewb.data.AlgPool;
import cubes.skewb.data.L2LSet;
import cubes.skewb.data.SkewbL2LReader;
import cubes.skewb.solvers.SkewbScrambler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static Settings.Settings.*;

public class SkewbAlgTrainer extends BorderPane implements Initializable {

    @FXML
    private Label scramble;

    @FXML
    private Label timer;

    @FXML
    private GridPane checkBoxPane;

    @FXML
    private GridPane checkBoxPane2;

    @FXML
    private VBox algVBox;

    @FXML
    private TextField poolTextInput;

    @FXML
    private Button addWithSelectionButton;

    @FXML
    private Button addEmptyButton;

    @FXML
    private ListView<AlgPoolElement> poolList;

    private SkewbScreenController skewbScreenController;

    private boolean timerRunning;

    private long sysTimeMilis;

    private List<Case> selectedCases;

    private List<Label> menus;

    private List<Case> examModeCasesLeft;

    private boolean inExamMode;
    public int lastRandIndex = 0;

    private Map<Integer, CheckBox> setToCheckboxMap;

    public SkewbAlgTrainer(SkewbScreenController skewbScreenController) {
        this.skewbScreenController = skewbScreenController;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/subscreens/skewb/skewbAlgTrainer.fxml"));
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
        inExamMode = false;

        setToCheckboxMap = new HashMap<>();

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

            GridPane gp;
            if (set.getId() <= 37) gp = checkBoxPane;
            else gp = checkBoxPane2;

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

            setToCheckboxMap.put(set.getId(), checkBox);

            gp.add(checkBox, x, y);
            Label setName = new Label();
            setName.setText(set.getName());
            gp.add(setName, x + 1, y);

            x += 2;
            if (x == 6) {
                gp.addRow(1);
                RowConstraints rc = new RowConstraints();
                rc.setPrefHeight(30.0);
                gp.getRowConstraints().add(rc);
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

            //Reset after case 37, switch from L2L to EG2
            if (set.getId() == 37) {
                x = 0;
                y = 1;
            }
        }

        addWithSelectionButton.setOnMouseClicked(mouseEvent -> {
            createAlgPool(this.getSetSelection());
        });

        addEmptyButton.setOnMouseClicked(mouseEvent -> {
            createAlgPool(new ArrayList<>());
        });

        updatePools(true);

//        try {
//            AllSetAlgGenerator asag = new AllSetAlgGenerator(sets, null);
//            Thread t = new Thread(asag);
//            t.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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

        if (!selectedCases.isEmpty()) {
            newScramble = generateNewScramble(selectedCases);
            if (PREVENT_SAME_SCRAMBLE) {
                int tries = 0;              //To prevent infinite loops for some reason
                while (newScramble.equals(scramble.getText()) && tries < 100) {
                    newScramble = generateNewScramble(selectedCases);
                    tries++;
                }
            }
        }
        scramble.setText(newScramble);
    }

    private void createAlgPool(List<Integer> ids) {
        String name = poolTextInput.getText();

        if (name.isEmpty()) {
            return;
        }

        if (poolList.getItems().stream().anyMatch(z -> z.getAlgPool().getName().equals(name))) {
            //Name already exists
            return;
        }

        AlgPool pool = new AlgPool(poolTextInput.getText(), ids);
        AlgPoolElement element = new AlgPoolElement(pool, this);
        poolList.getItems().add(element);

        updatePools(false);

        poolTextInput.setText("");
    }

    public void startExamMode() {
        this.setInExamMode(true);

        this.examModeCasesLeft = new ArrayList<>(this.selectedCases);
        timer.setText("Cases left: " + examModeCasesLeft.size());
        scramble.setText(generateNewScramble(examModeCasesLeft));
    }

    public void passExamQuestion() {
        if (!examModeCasesLeft.isEmpty()) {
            this.examModeCasesLeft.remove(lastRandIndex);
            timer.setText("Cases left: " + examModeCasesLeft.size());
            scramble.setText(generateNewScramble(examModeCasesLeft));
        }
    }

    public void failExamQuestion() {
        scramble.setText(generateNewScramble(examModeCasesLeft));
    }

    private String generateNewScramble(List<Case> cases) {
        //Select random case
        if (cases.isEmpty()) {
            return "Select a set";
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(0, cases.size());
        int randomAmount = ThreadLocalRandom.current().nextInt(1, AMOUNT_RANDOM_SCRAMBLES + 1);

        lastRandIndex = randomIndex;

        return SkewbScrambler.stateToScrambler(new SkewbState(cases.get(randomIndex).getPattern()), randomAmount);
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
    }

    public boolean isInExamMode() {
        return inExamMode;
    }

    public void setInExamMode(boolean inExamMode) {
        this.inExamMode = inExamMode;
    }

    public void setSelection(List<Integer> ids, boolean deselectAll) {
        if (deselectAll) {
            for (Map.Entry<Integer, CheckBox> s : setToCheckboxMap.entrySet()) {
                s.getValue().setSelected(false);
            }
        }
        for (Map.Entry<Integer, CheckBox> s : setToCheckboxMap.entrySet()) {
            if (ids.contains(s.getKey())) {
                s.getValue().setSelected(true);
            }
        }
    }

    public List<Integer> getSetSelection() {
        return selectedCases.stream().map(x -> readInitialInteger(x.getId()))
                .distinct().collect(Collectors.toList());
    }

    public void updatePools(boolean readOnly) {
        if (!readOnly) {
            writeAlgPoolFile();
        }

        List<AlgPool> l = readAlgPoolFile();
        poolList.getItems().clear();

        for (AlgPool pool : l) {
            AlgPoolElement element = new AlgPoolElement(pool, this);
            poolList.getItems().add(element);
        }

    }

    public void deletePool(String name) {
        for (int i = 0; i < poolList.getItems().size(); i++) {
            if (poolList.getItems().get(i).getAlgPool().getName().equals(name)) {
                poolList.getItems().remove(i);
                updatePools(false);
                return;
            }
        }
    }

    private void writeAlgPoolFile() {
        try {
            //TODO: Automatically create data_storage_location directory if it doesn't exist
            File file = new File(DATA_STORAGE_LOCATION + "algPools");
            file.delete();
            file.createNewFile();

            FileOutputStream f = new FileOutputStream(file, false);
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(this.poolList.getItems().stream().map(AlgPoolElement::getAlgPool).collect(Collectors.toList()));

            o.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<AlgPool> readAlgPoolFile() {
        try {
            //TODO: Properly check if file exists, otherwhise, create or skip
            FileInputStream fi = new FileInputStream(DATA_STORAGE_LOCATION + "algPools");
            ObjectInputStream oi = new ObjectInputStream(fi);

            List<AlgPool> algPoolList = (List<AlgPool>) oi.readObject();

            return algPoolList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //TODO: move to util class
    public static int readInitialInteger(String input) {
        // StringBuilder to accumulate digits
        StringBuilder sb = new StringBuilder();

        // Iterate over each character in the string
        for (char c : input.toCharArray()) {
            // Check if the character is a digit
            if (Character.isDigit(c)) {
                sb.append(c); // Append the digit to the StringBuilder
            } else {
                break; // Stop reading characters when a non-digit is encountered
            }
        }

        // If no digits were found, throw an exception
        if (sb.length() == 0) {
            throw new NumberFormatException("No leading integer found.");
        }

        // Convert the accumulated digits into an integer
        return Integer.parseInt(sb.toString());
    }
}
