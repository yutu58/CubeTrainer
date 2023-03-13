package application.gui.subscreens;

import application.controllers.SkewbScreenController;
import cubes.skewb.SkewbNotations;
import cubes.skewb.SkewbState;
import cubes.skewb.solvers.SkewbSolver;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SkewbAlgGenerator extends GridPane implements Initializable {
    @FXML
    private Button applyButton;

    @FXML
    private Label imageErrorLabel;

    @FXML
    private TextField setupMoves;

    @FXML
    private Slider depthSlider;

    @FXML
    private CheckBox reverseBox;

    @FXML
    private CheckBox allAngles;

    @FXML
    private RadioButton rubikSkewbButton;

    @FXML
    private RadioButton wcaSkewbButton;

    @FXML
    private RadioButton codeSkewbButton;

    @FXML
    private ListView<String> algList;

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

        notationGroup = new ToggleGroup();

        rubikSkewbButton.setToggleGroup(notationGroup);
        rubikSkewbButton.setSelected(true);
        wcaSkewbButton.setToggleGroup(notationGroup);
        codeSkewbButton.setToggleGroup(notationGroup);
    }

    private void go() {
        RadioButton selected = (RadioButton) notationGroup.getSelectedToggle();
        SkewbNotations.notationEnum n;

        if (selected == rubikSkewbButton) {
            n = SkewbNotations.notationEnum.RubikSkewbNotation;
        } else if (selected == wcaSkewbButton) {
            n = SkewbNotations.notationEnum.WCASkewbNotation;
        } else if (selected == codeSkewbButton) {
            n = SkewbNotations.notationEnum.LithiumSkewbCode;
        } else {
            promptError("Select a valid input notation");
            return;
        }

        SkewbState s = SkewbState.setupCase(setupMoves.getText(), n, reverseBox.isSelected());

        SkewbSolver solver = new SkewbSolver(s, (int) depthSlider.getValue(), imageErrorLabel, algList, allAngles.isSelected());

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
