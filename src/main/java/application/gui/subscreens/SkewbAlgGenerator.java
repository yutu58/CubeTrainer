package application.gui.subscreens;

import application.controllers.SkewbScreenController;
import cubes.skewb.SkewbNotations;
import cubes.skewb.SkewbState;
import cubes.skewb.imageGenerators.SkewbL2LImageGenerator;
import cubes.skewb.solvers.SkewbSolver;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SkewbAlgGenerator extends GridPane implements Initializable {
    @FXML
    private Button goButton;

    @FXML
    private Button stopButton;

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
    private TextField setupMoves;

    @FXML
    private Slider scaleSlider;

    @FXML
    private CheckBox reverseBox;

    @FXML
    private CheckBox bottomBox;

    private SkewbScreenController controller;

    private final ExecutorService executorService;

    private SkewbSolver skewbSolver;

    private ToggleGroup notationGroup;

    public SkewbAlgGenerator(SkewbScreenController controller) {
        this.controller = controller;
        this.skewbSolver = null;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/subscreens/skewbAlgGenerator.fxml"));
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
        goButton.setOnAction((e) -> go());
        stopButton.setOnAction((e) -> stop());

    }

    private void go() {
        String pattern = SkewbL2LImageGenerator.drawImageFromSetup(SkewbNotations.notationEnum.RubikSkewbNotation,
                setupMoves.getText(), imageCanvas.getGraphicsContext2D(), 1, false);
        SkewbState s = new SkewbState(pattern);
        SkewbSolver solver = new SkewbSolver(s, 14, imageErrorLabel, true);
        skewbSolver = solver;
        executorService.submit(solver);
    }

    private void stop() {
        try {
            skewbSolver.setCancelled(true);
        } catch (Exception e) {
            e.printStackTrace();
            promptError("Something went wrong when cancelling, please restart the application!");
        }
    }

    private void promptSuccess(String succ) {
        imageErrorLabel.setText(succ);
        imageErrorLabel.setTextFill(Color.GREEN);
    }

    private void promptError(String err) {
        imageErrorLabel.setText(err);
        imageErrorLabel.setTextFill(Color.RED);
    }
}
