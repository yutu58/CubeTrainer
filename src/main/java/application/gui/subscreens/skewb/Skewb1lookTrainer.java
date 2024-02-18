package application.gui.subscreens.skewb;

import application.controllers.SkewbScreenController;
import cubes.Case;
import cubes.skewb.SkewbNotations;
import cubes.skewb.SkewbState;
import cubes.skewb.data.L2LSet;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Skewb1lookTrainer extends GridPane implements Initializable {
    @FXML
    private Button applyButton;

    @FXML
    private Label imageErrorLabel;

    @FXML
    private Canvas imageCanvas;

    @FXML
    private RadioButton rubikSkewbButton;

    @FXML
    private RadioButton wcaSkewbButton;

    @FXML
    private RadioButton codeSkewbButton;

    @FXML
    private Label copyImageButton;

    @FXML
    private Label downloadPNGButton;

    @FXML
    private Label copyLithiumCodeButton;

    @FXML
    private TextField setupMoves;

    @FXML
    private Slider setupSlider;

    @FXML
    private Label scramble;

    @FXML
    private CheckBox reverseBox;

    List<Case> allCases;
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

            if (!setupMoves.getText().isEmpty()) {
                display1lookImage();
                scramble.setText(generateNewScramble());
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
                if (s.getId() < 38) {
                    allCases.addAll(s.getCases());
                }
            }
        }
    }

    public void updateScramble() {
        scramble.setText(generateNewScramble());
    }

    //Setupmove slider
    private String generateNewScramble() {
        if(setupMoves.isFocused()) {
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
        String[] moves = setupMoves.getText().trim().split(" ");
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

        return SkewbScrambler.stateToScrambler(state, (int) (Math.random() * 10));
    }

    private void display1lookImage() {
        String setup = setupMoves.getText();
        GraphicsContext gc = imageCanvas.getGraphicsContext2D();
        double scale = 0.9;

        //Forced RubikSkewbNotation for now;
        SkewbNotations.notationEnum n = SkewbNotations.notationEnum.RubikSkewbNotation;
        try {
            SkewbState state = new SkewbState("00000 16611 22662 33663 44664 55665");
            String rotatedSetup = "x " + setup + " x' z2";
            //Forced reverse setup
            String pattern = SkewbL2LImageGenerator.drawImageFromSetup(n, rotatedSetup, gc, scale, true, state);
        } catch (RuntimeException e) {
            promptError(e.getMessage());
        }
    }

    private void promptError(String err) {
        imageErrorLabel.setText(err);
        imageErrorLabel.setTextFill(Color.RED);
    }
}
