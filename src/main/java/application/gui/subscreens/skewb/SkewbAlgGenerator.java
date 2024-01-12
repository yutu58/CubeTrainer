package application.gui.subscreens.skewb;

import application.controllers.SkewbScreenController;
import cubes.skewb.SkewbNotations;
import cubes.skewb.SkewbState;
import cubes.skewb.optimizers.AlgRater;
import cubes.skewb.optimizers.EMrater;
import cubes.skewb.optimizers.EMraterV2;
import cubes.skewb.optimizers.LengthRater;
import cubes.skewb.solvers.SkewbSolver;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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

    @FXML
    private HBox rotationExplorer;

    @FXML
    private Label copyAlgButton;

    @FXML
    private RadioButton noOptimizer;

    @FXML
    private RadioButton emRater;

    @FXML
    private RadioButton emRaterv2;

    private SkewbScreenController controller;

    private ToggleGroup notationGroup;

    private ToggleGroup optimizerGroup;

    public SkewbAlgGenerator(SkewbScreenController controller) {
        this.controller = controller;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/subscreens/skewb/skewbAlgGenerator.fxml"));
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

        optimizerGroup = new ToggleGroup();
        noOptimizer.setToggleGroup(optimizerGroup);
        noOptimizer.setSelected(true);
        emRater.setToggleGroup(optimizerGroup);
        emRaterv2.setToggleGroup(optimizerGroup);

        algList.setOnMousePressed(event -> {
            Optional<String> optional = algList.getSelectionModel().getSelectedItems().stream().findFirst();
            if (optional.isEmpty()) return;
            setHighlightedAlg(optional.get());
        });

        algList.setOnKeyPressed(event -> {
            if (event.getCode().isArrowKey()){
                Optional<String> optional = algList.getSelectionModel().getSelectedItems().stream().findFirst();
                if (optional.isEmpty()) return;
                setHighlightedAlg(optional.get());
            }
        });

        Label l = copyAlgButton;
        l.setBorder(new Border(new BorderStroke(SKEWB_MENU_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        l.setOnMouseEntered((e) -> l.setBackground(new Background(new BackgroundFill(SUBMENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY))));
        l.setOnMousePressed((e) -> l.setBackground(new Background(new BackgroundFill(SELECTED_SUBMENU_BUTTON_COLOR, CornerRadii.EMPTY, Insets.EMPTY))));
        l.setOnMouseReleased((e) -> l.setBackground(new Background(new BackgroundFill(SUBMENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY))));
        l.setOnMouseExited((e) -> l.setBackground(null));

        l.setOnMouseClicked((e) -> copyAlgToClipboard());
        l.setVisible(false);
    }

    private void copyAlgToClipboard() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        StringBuilder finalAlg = new StringBuilder();
        for (Node child : rotationExplorer.getChildren()) {
            Label l = (Label) child;
            if (l.getText().equals(" - ")) {
                finalAlg.append(" ");
            } else {
                finalAlg.append(l.getText());
            }
        }
        content.put(DataFormat.PLAIN_TEXT, finalAlg.toString());
        clipboard.setContent(content);
        promptSuccess("Alg copied to clipboard!");
    }

    private void setHighlightedAlg(String alg) {
        copyAlgButton.setVisible(true);
        ObservableList<Node> children = rotationExplorer.getChildren();
        children.remove(0, children.size());

        String[] moves = alg.split(" ");
        //Create z select element
        for (int i = 0; i < moves.length; i++) {
            Label x = new Label(moves[i]);
            x.setFont(new Font(30));
            x.setStyle("-fx-font-weight: Bold");
            children.add(x);

            if (i != moves.length - 1) {
                Label y = new Label(" - ");
                int finalI = i;
                y.setFont(new Font(30));
                children.add(y);
                y.setOnMouseClicked(event -> {
                    ObservableList<Node> childrenLeft = rotationExplorer.getChildren();
                    //Update moves after
                    for (int j = (finalI + 1) * 2; j < childrenLeft.size(); j += 2) {
                        Label l = (Label) childrenLeft.get(j);
                        l.setText(SkewbNotations.nextInZ(l.getText()));

                    }
                    //Update rotation move itself
                    y.setText(SkewbNotations.nextInZ(y.getText()));
                });

                y.setOnMouseEntered(event -> y.setStyle("-fx-underline: true; -fx-text-fill: blue; -fx-cursor: hand"));
                y.setOnMouseExited(event -> y.setStyle("-fx-underline: false; -fx-text-fill: black; -fx-cursor: default"));
            }
        }
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

        AlgRater rater;
        RadioButton optimizer = (RadioButton) optimizerGroup.getSelectedToggle();
        if (optimizer == noOptimizer) {
            rater = new LengthRater();
        } else if (optimizer == emRater) {
            rater = new EMrater();
        } else if (optimizer == emRaterv2) {
            rater = new EMraterV2();
        } else {
            promptError("Select a valid optimizer");
            return;
        }

        try {
            SkewbState s = SkewbState.setupCase(setupMoves.getText(), n, reverseBox.isSelected());

            SkewbSolver solver = new SkewbSolver(s, (int) depthSlider.getValue(), imageErrorLabel, algList, allAngles.isSelected(), rater);

            ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            es.submit(solver);
        } catch (RuntimeException e){
            promptError("Please provide a valid input!");
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
