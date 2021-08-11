package application.gui.subscreens;

import application.controllers.SkewbScreenController;
import cubes.skewb.SkewbNotations;
import cubes.skewb.SkewbState;
import cubes.skewb.imageGenerators.SkewbL2LImageGenerator;
import cubes.skewb.solvers.SkewbScrambler;
import cubes.skewb.solvers.SkewbSolver;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Settings.Settings.*;

public class SkewbAlgGenerator extends GridPane implements Initializable {
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
    private Slider scaleSlider;

    @FXML
    private CheckBox reverseBox;

    @FXML
    private CheckBox bottomBox;

    private String pattern;

    private SkewbScreenController controller;

    private ToggleGroup notationGroup;

    public SkewbAlgGenerator(SkewbScreenController controller) {
        this.controller = controller;
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
        applyButton.setOnAction((e) -> go());
    }

    private void go() {
        pattern = SkewbL2LImageGenerator.drawImageFromSetup(SkewbNotations.notationEnum.RubikSkewbNotation,
                setupMoves.getText(), imageCanvas.getGraphicsContext2D(), 1, false);
        SkewbState s = new SkewbState(pattern);
        SkewbSolver solver = new SkewbSolver(s, 15, imageErrorLabel);

        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        es.submit(solver);
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
