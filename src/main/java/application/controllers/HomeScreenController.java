package application.controllers;

import application.gui.MainAppWindow;
import application.gui.screens.SkewbScreen;
import application.gui.screens.Tx2Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomeScreenController {
    @FXML
    public Button skewbButton;

    @FXML
    public Button tx2Button;

    @FXML
    public void initialize() {

    }

    public void gotoSkewb() throws IOException {
        SkewbScreen.setup(MainAppWindow.getPrimaryStage());
    }

    public void goto2x2() throws IOException {
        Tx2Screen.setup(MainAppWindow.getPrimaryStage());
    }
}
