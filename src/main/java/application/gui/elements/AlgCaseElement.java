package application.gui.elements;

import cubes.skewb.data.Alg;
import cubes.skewb.data.L2LCase;
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

public class AlgCaseElement extends BorderPane implements Initializable {
    @FXML
    private Label caseName;

    @FXML
    private GridPane algTable;

    @FXML
    private Canvas caseCanvas;

    L2LCase l2LCase;

    public AlgCaseElement(L2LCase l2LCase) {
        this.l2LCase = l2LCase;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/elements/algCaseElement.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.caseName.setText(l2LCase.getId() + ": ");
        SkewbL2LImageGenerator.drawSkewbImage(this.caseCanvas.getGraphicsContext2D(), l2LCase.getPattern(), false);
        for (Alg a : this.l2LCase.getProvidedAlgs()) {
            RadioButton button = new RadioButton();
            Label algLabel = new Label();
            algLabel.setFont(new Font(14));
            algLabel.setText(a.getAlgorithm());
            Label time = new Label();
            time.setFont(new Font(14));
            if (a.getTimesTimed() == 0) {
                time.setText("-.--");
            } else {
                time.setText(String.format("%.2f", (double) a.getAverage() / 100));
            }
            algTable.addRow(algTable.getRowCount(), button, algLabel, time);
        }

        //Provided algs should not be able to be deleted, self made algs should
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
