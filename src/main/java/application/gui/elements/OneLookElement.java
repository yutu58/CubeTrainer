package application.gui.elements;

import application.gui.subscreens.skewb.Skewb1lookTrainer;
import cubes.skewb.SkewbNotations;
import cubes.skewb.SkewbState;
import cubes.skewb.data.OneLookPool;
import cubes.skewb.imageGenerators.SkewbL2LImageGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

import static Settings.Settings.IMAGE_SCALE;

public class OneLookElement extends BorderPane {
    @FXML
    private Label oneLookLayerSolution;

    @FXML
    private Canvas oneLookCaseCanvas;

    @FXML
    private Button setupOneLookCaseButton;

    @FXML
    private Button removeFromPoolButton;

    String setup;

    public OneLookElement(String setup, OneLookPool parentPool, Skewb1lookTrainer trainer) {
        this.setup = setup;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/elements/oneLookElement.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        oneLookLayerSolution.setText(setup);

        drawOneLookCaseCanvas();

        setupOneLookCaseButton.setOnMouseClicked(mouseEvent -> {
            trainer.generateNewScramble(setup);
        });

        removeFromPoolButton.setOnMouseClicked(mouseEvent -> {
            parentPool.deleteSetup(setup);
            trainer.updatePools(false);
        });
    }

    private void drawOneLookCaseCanvas() {
        SkewbState state = new SkewbState("00000 16611 22662 33663 44664 55665");
        String rotatedSetup = "x " + setup + " x' z2";
        //Forced reverse setup
        String pattern = SkewbL2LImageGenerator.drawImageFromSetup(SkewbNotations.notationEnum.RubikSkewbNotation,
                rotatedSetup, oneLookCaseCanvas.getGraphicsContext2D(), IMAGE_SCALE, true, state);
    }
}
