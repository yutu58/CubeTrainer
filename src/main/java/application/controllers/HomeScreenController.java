package application.controllers;

import Settings.Settings;
import application.gui.MainAppWindow;
import application.gui.screens.SkewbScreen;
import application.gui.screens.Tx2Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class HomeScreenController {
    @FXML
    public Button skewbButton;

    @FXML
    public Button tx2Button;

    @FXML
    public ImageView homescreenImage;

    @FXML
    public Label versionCopyright;

    @FXML
    public void initialize() {
        homescreenImage.setImage(new Image("/pictures/LithiumLogo.png"));
        versionCopyright.setText("Lithium version " + Settings.VERSION +
                "\n\n\u00A9 Mattheo de Wit\n2022 - 2024");
    }

    public void gotoSkewb() throws IOException {
        SkewbScreen.setup(MainAppWindow.getPrimaryStage());
    }

    public void goto2x2() throws IOException {
        Tx2Screen.setup(MainAppWindow.getPrimaryStage());
    }
}
