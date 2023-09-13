package application.controllers;

import application.gui.subscreens.tx2.Tx2AlgGenerator;
import application.gui.subscreens.tx2.Tx2AlgTrainer;
import application.gui.subscreens.tx2.Tx2ImageGenerator;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import static Settings.Settings.*;

public class Tx2ScreenController implements ScreenController{
    @FXML
    private Label algTrainerButton;

    @FXML
    private Label imageGeneratorButton;

    @FXML
    private Label algGeneratorButton;

    @FXML
    private BorderPane tx2Screen;

    @FXML
    private HBox menu;

    private Pane subMenu;

    private Tx2AlgGenerator algGenerator;
    private Tx2ImageGenerator imageGenerator;
    private Tx2AlgTrainer algTrainer;

    @FXML
    public void initialize() {
        menu.setBackground(new Background(new BackgroundFill(TX2_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        algTrainer = new Tx2AlgTrainer(this);
        imageGenerator = new Tx2ImageGenerator(this);
        algGenerator = new Tx2AlgGenerator(this);
        setupAlgTrainer();
    }

    @FXML
    private void setupAlgTrainer() {
        algTrainerButton.setBackground(new Background(new BackgroundFill(SELECTED_MENU_BUTTON_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        imageGeneratorButton.setBackground(new Background(new BackgroundFill(TX2_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        algGeneratorButton.setBackground(new Background(new BackgroundFill(TX2_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        subMenu = algTrainer;
        tx2Screen.setCenter(algTrainer);
    }

    @FXML
    private void setupImageGenerator() {
        algTrainerButton.setBackground(new Background(new BackgroundFill(TX2_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        imageGeneratorButton.setBackground(new Background(new BackgroundFill(SELECTED_MENU_BUTTON_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        algGeneratorButton.setBackground(new Background(new BackgroundFill(TX2_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        subMenu = imageGenerator;
        tx2Screen.setCenter(imageGenerator);
    }

    @FXML
    private void setupAlgGenerator() {
        algTrainerButton.setBackground(new Background(new BackgroundFill(TX2_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        imageGeneratorButton.setBackground(new Background(new BackgroundFill(TX2_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        algGeneratorButton.setBackground(new Background(new BackgroundFill(SELECTED_MENU_BUTTON_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        subMenu = algGenerator;
        tx2Screen.setCenter(algGenerator);
    }

    public BorderPane getScreen() {
        return tx2Screen;
    }

    public Pane getSubMenu() {
        return subMenu;
    }
}