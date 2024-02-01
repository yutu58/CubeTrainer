package application.gui.elements;

import application.controllers.ScreenController;
import cubes.Case;
import cubes.Alg;
import cubes.skewb.imageGenerators.SkewbL2LImageGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static Settings.Settings.AUTO_REMOVE_FOCUS;
import static Settings.Settings.IMAGE_SCALE;

public class AlgCaseElement extends BorderPane implements Initializable {
    @FXML
    private Label caseName;

    @FXML
    private GridPane algTable;

    @FXML
    private Canvas caseCanvas;

    Case l2LCase;

    public AlgCaseElement(Case l2LCase, ScreenController co) {
        this.l2LCase = l2LCase;
        double scale = IMAGE_SCALE;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/elements/algCaseElement.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.caseName.setText(l2LCase.getId() + ": ");

        SkewbL2LImageGenerator.drawSkewbImage(this.caseCanvas.getGraphicsContext2D(),
                l2LCase.getPattern(), scale, false, true);

        for (Alg a : this.l2LCase.getProvidedAlgs()) {
            RadioButton button = new RadioButton();
            if (AUTO_REMOVE_FOCUS) {
                button.selectedProperty().addListener((observable -> co.getScreen().requestFocus()));
            }
            Label algLabel = new Label();
            algLabel.setFont(new Font(14));
            algLabel.setText(a.getAlgorithm());
            Label time = new Label();
            time.setFont(new Font(14));
            time.setText(String.format("%.2f", a.getRating()));
            algTable.addRow(algTable.getRowCount(), button, algLabel, time);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
