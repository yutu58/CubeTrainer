package application.gui.elements;

import application.controllers.ScreenController;
import cubes.Case;
import cubes.Set;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static Settings.Settings.AUTO_REMOVE_FOCUS;

public class AlgSetElement extends TitledPane implements Initializable {
    @FXML
    private VBox caseContainer;

    Set algSet;

    public AlgSetElement(Set algSet, ScreenController co) {
        this.algSet = algSet;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/elements/algSetElement.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (AUTO_REMOVE_FOCUS) {
            caseContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    co.getScreen().requestFocus();
                }
            });
        }

        for (Case c : this.algSet.getCases()) {
            this.caseContainer.getChildren().add(new AlgCaseElement(c, co));
        }

        float bestRating = 0;
        float worstBestRating = 30;
        float averageBestRating = 0;

        List<Case> caseList = this.algSet.getCases();

        for (int i = 0; i < caseList.size(); i++) {
            Case c = caseList.get(i);
            float rating = c.getProvidedAlgs().get(0).getRating(); //Get best alg

            if (rating > bestRating) bestRating = rating;
            if (rating < worstBestRating) worstBestRating = rating;
            averageBestRating += rating / caseList.size();
        }


        this.setText(this.algSet.getId() + ": " + this.algSet.getName() + "        ("
                + String.format("%.2f", bestRating) + " / "
                + String.format("%.2f", worstBestRating) + " / "
                + String.format("%.2f", averageBestRating) + ")");

    }

    public VBox getCaseContainer() {
        return caseContainer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setExpanded(false);
    }


}
