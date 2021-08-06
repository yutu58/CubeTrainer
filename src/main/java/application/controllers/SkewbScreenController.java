package application.controllers;

import application.gui.MainAppWindow;
import application.gui.elements.AlgSetElement;
import application.gui.screens.HomeScreen;
import cubes.skewb.data.L2LSet;
import cubes.skewb.data.SkewbReader;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class SkewbScreenController {

    @FXML
    private VBox algVBox;

    @FXML
    public void initialize() {
        SkewbReader reader = new SkewbReader();
        List<L2LSet> sets = null;
        try {
            sets = reader.read();
        } catch (IOException e) {
            backToHome();
        }
        assert sets != null;

        for (L2LSet set : sets) {
            //Create checkboxes for the set

            //Create alg window on the right
            algVBox.getChildren().add(new AlgSetElement(set));
        }


    }

    private void backToHome() {
        try {
            HomeScreen.setup(MainAppWindow.getPrimaryStage());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


}
