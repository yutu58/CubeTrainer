package application.gui.subscreens.skewb;

import application.controllers.SkewbScreenController;
import application.gui.elements.OneLookPoolElement;
import cubes.Case;
import cubes.skewb.SkewbNotations;
import cubes.skewb.SkewbState;
import cubes.skewb.data.L2LSet;
import cubes.skewb.data.OneLookPool;
import cubes.skewb.data.SkewbL2LReader;
import cubes.skewb.imageGenerators.SkewbL2LImageGenerator;
import cubes.skewb.imageGenerators.UnknownMoveException;
import cubes.skewb.solvers.SkewbScrambler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static Settings.Settings.DATA_STORAGE_LOCATION;

public class Skewb1lookTrainer extends GridPane implements Initializable {
    @FXML
    private Button applyButton;

    @FXML
    private Label imageErrorLabel;

    @FXML
    private Canvas imageCanvas;

    @FXML
    private TextField setupMoves;

    @FXML
    private Slider setupSlider;

    @FXML
    private Label scramble;

    @FXML
    private CheckBox reverseBox;

    @FXML
    private Button addToPool;

    @FXML
    private TextField newPoolNameField;

    @FXML
    private Button createPool;

    List<Case> allCases;

    private List<String> activeSetups;

    private OneLookPoolElement activeOneLookPoolElement;

    //TODO: Convert to ScrollView
    @FXML
    private ListView<OneLookPoolElement> oneLookPoolList;
    private boolean timerRunning;
    private SkewbScreenController controller;

    public Skewb1lookTrainer(SkewbScreenController controller) {
        this.controller = controller;

        allCases = new ArrayList<>();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/subscreens/skewb/skewb1lookTrainer.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was a problem loading the FXML");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timerRunning = false;
        applyButton.setOnAction(event -> {
            controller.getScreen().requestFocus();

            promptSucces("");

            if (!setupMoves.getText().isEmpty()) {
                activeSetups = new ArrayList<>();
                activeSetups.add(setupMoves.getText());

                display1lookImage(setupMoves.getText());
                scramble.setText(generateNewScramble(setupMoves.getText()));
            }
        });


        //Initialize all cases
        SkewbL2LReader reader = new SkewbL2LReader();
        List<L2LSet> sets = null;
        try {
            sets = reader.read();
            if (sets == null) {
                throw new IOException();
            }
        } catch (IOException e) {
            promptError("No sets could be found");
        }

        if (sets != null) {
            for (L2LSet s : sets) {
                //Only add NS cases (no EG-2)
                //TODO: Add option for EG-2
                if (s.getId() < 38) {
                    allCases.addAll(s.getCases());
                }
            }
        }

        createPool.setOnMouseClicked(mouseEvent -> {
            String name = newPoolNameField.getText();

            if (name.equals("")) {
                return;
            }

            OneLookPoolElement element = new OneLookPoolElement(new OneLookPool(name, new ArrayList<>()), this);
            oneLookPoolList.getItems().add(element);

            updatePools(false);
        });

        addToPool.setOnMouseClicked(mouseEvent -> {
            if (activeOneLookPoolElement == null) {
                return;
            }

            String setup = setupMoves.getText();

            //Try if it's possible to draw an image, if not there's probably an unknown move / wrong notation used
            try {
                SkewbState c = new SkewbState("00000 11111 22222 33333 44444 55555");
                SkewbL2LImageGenerator.drawImageFromSetup(SkewbNotations.notationEnum.RubikSkewbNotation,
                        setup, null, 0, true, c);
            } catch (Exception e) {
                promptError("Couldn't add to pool, wrong notation?");
                return;
            }

            if (activeOneLookPoolElement.getOneLookPool().getSetups().contains(setup)) {
                promptError("Case already in pool");
                return;
            }

            if (!setup.isEmpty()) {
                activeOneLookPoolElement.getOneLookPool().addSetup(setup);
                updatePools(false);
            }

        });

        updatePools(true);
    }

    public void updateScramble() {
        if (activeSetups == null || activeSetups.isEmpty()) return;

        //Generate random
        int index = ThreadLocalRandom.current().nextInt(0, activeSetups.size());
        scramble.setText(generateNewScramble(activeSetups.get(index)));
    }

    public String generateNewScramble(String setup) {
        if(setupMoves.isFocused() || newPoolNameField.isFocused()) {
            return scramble.getText();
        }

        //Get random case
        //This assumes all cases have equal probability, which is NOT true
        //Just easier to code :)

        Case random = allCases.get((int) (Math.random() * allCases.size()));
        SkewbState state = new SkewbState(random.getPattern());

        //Apply random orientation
        int orientations = (int) (Math.random() * 4);
        for (int i = 0; i < orientations; i++) {
            state.applyWCAMoves(SkewbNotations.wcaNotation.get("y"));
        }
        state.applyWCAMoves(SkewbNotations.wcaNotation.get("x"));

        //Apply layer setup
        List<Integer> intMoves = new ArrayList<>();
        String[] moves = setup.trim().split(" ");
        for (String m : moves) {
            if (m.equals("")) {
                continue;
            }
            int[] triedMoves = SkewbNotations.rubikSkewbNotation.get(m);
            if (triedMoves == null) {
                throw new UnknownMoveException(m + " is not a valid move!");
            }
            for (int i : triedMoves) {
                intMoves.add(i);
            }
        }
        int[] arrIntMoves = intMoves.stream().mapToInt(i->i).toArray();
        arrIntMoves = SkewbScrambler.reverseSkewb(arrIntMoves);
        state.applyWCAMoves(arrIntMoves);

        for (int i = 0; i < (int) setupSlider.getValue(); i++) {
            state.applyWCAMoves(new int[]{(int) (Math.random() * SkewbNotations.N_WCA_MOVES_WITHOUT_ROTATIONS)});
        }

        return SkewbScrambler.stateToScrambler(state, (int) (Math.random() * 10) + 1);
    }

    private void display1lookImage(String setup) {
        GraphicsContext gc = imageCanvas.getGraphicsContext2D();
        double scale = 0.9;

        //Forced RubikSkewbNotation for now;
        SkewbNotations.notationEnum n = SkewbNotations.notationEnum.RubikSkewbNotation;
        try {
            SkewbState state = new SkewbState("00000 16611 22662 33663 44664 55665");
            String rotatedSetup = "x " + setup + " x' z2";
            //Forced reverse setup
            String pattern = SkewbL2LImageGenerator.drawImageFromSetup(n, rotatedSetup, gc, scale, true, state);

            //Put pattern on clipboard?
            //TODO: make optional with a button or smth
//            Clipboard clipboard = Clipboard.getSystemClipboard();
//            ClipboardContent content = new ClipboardContent();
//            content.putString(pattern);
//            clipboard.setContent(content);

            //If copy to clipboard button:
            //Moet via event listener en niet hier
        } catch (RuntimeException e) {
            promptError(e.getMessage());
        }
    }

    public void promptError(String err) {
        imageErrorLabel.setText(err);
        imageErrorLabel.setTextFill(Color.RED);
    }

    private void promptSucces(String succes) {
        imageErrorLabel.setText(succes);

        //TODO: Set color back to default, or just use a different label than the image error one
        imageErrorLabel.setTextFill(Color.WHITE);
    }

    public void updatePools(boolean readOnly) {
        if (!readOnly) {
            writeOneLookPoolFile();
        }

        List<OneLookPool> l = readOneLookPoolFile();
        oneLookPoolList.getItems().clear();

        for (OneLookPool pool : l) {
            OneLookPoolElement element = new OneLookPoolElement(pool, this);
            oneLookPoolList.getItems().add(element);
        }

        //Update activePoolElement
        if (activeOneLookPoolElement != null) {
            String activeName = activeOneLookPoolElement.getOneLookPool().getName();
            Optional<OneLookPoolElement> oolpe = oneLookPoolList.getItems().stream()
                    .filter(x -> x.getOneLookPool().getName().equals(activeName)).findFirst();

            if (oolpe.isPresent()) {
                activeOneLookPoolElement = oolpe.get();
                promptSucces("Active pool: " + activeName);
            } else {
                activeOneLookPoolElement = null;
                promptSucces("");
            }
        }

        for (OneLookPoolElement e : oneLookPoolList.getItems()) {
            if (activeOneLookPoolElement != e) {
                e.setExpanded(false);
            }
        }
    }

    public void deletePool(String name) {
        for (int i = 0; i < oneLookPoolList.getItems().size(); i++) {
            if (oneLookPoolList.getItems().get(i).getOneLookPool().getName().equals(name)) {
                oneLookPoolList.getItems().remove(i);
                updatePools(false);
                return;
            }
        }
    }

    private List<OneLookPool> readOneLookPoolFile() {
        try {
            //TODO: Properly check if file exists, otherwhise, create or skip
            FileInputStream fi = new FileInputStream(DATA_STORAGE_LOCATION + "oneLookPools");
            ObjectInputStream oi = new ObjectInputStream(fi);

            List<OneLookPool> oneLookPoolList = (List<OneLookPool>) oi.readObject();

            return oneLookPoolList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void setActiveSetups(List<String> activeSetups) {
        this.activeSetups = activeSetups;
    }

    private void writeOneLookPoolFile() {
        try {
            //TODO: Automatically create data_storage_location directory if it doesn't exist
            File file = new File(DATA_STORAGE_LOCATION + "oneLookPools");
            file.delete();
            file.createNewFile();

            FileOutputStream f = new FileOutputStream(file, false);
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(this.oneLookPoolList.getItems().stream()
                    .map(OneLookPoolElement::getOneLookPool).collect(Collectors.toList()));

            o.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextField getSetupMoves() {
        return setupMoves;
    }

    public TextField getNewPoolNameField() {
        return newPoolNameField;
    }

    public OneLookPoolElement getActiveOneLookPoolElement() {
        return activeOneLookPoolElement;
    }

    public void setActiveOneLookPoolElement(OneLookPoolElement activeOneLookPoolElement) {
        this.activeOneLookPoolElement = activeOneLookPoolElement;

        promptSucces("Active pool: " + activeOneLookPoolElement.getOneLookPool().getName());

    }

    public Canvas getImageCanvas() {
        return imageCanvas;
    }
}
