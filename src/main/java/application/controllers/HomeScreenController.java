package application.controllers;

import application.gui.MainAppWindow;
import application.gui.screens.SkewbScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomeScreenController {
    @FXML
    public Button skewbButton;

    @FXML
    public void initialize() {

    }

    public void gotoSkewb() throws IOException {
        SkewbScreen.setup(MainAppWindow.getPrimaryStage());
    }
}
