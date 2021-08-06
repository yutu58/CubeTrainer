package application.gui.elements;

import cubes.skewb.data.L2LCase;
import cubes.skewb.data.L2LSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AlgSetElement extends TitledPane implements Initializable {
    @FXML
    private VBox caseContainer;

    L2LSet algSet;

    public AlgSetElement(L2LSet algSet) {
        this.algSet = algSet;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/elements/algSetElement.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setText(this.algSet.getId() + ": " + this.algSet.getName());
        for (L2LCase c : this.algSet.getCases()) {
            this.caseContainer.getChildren().add(new AlgCaseElement(c));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setExpanded(false);
    }


}
