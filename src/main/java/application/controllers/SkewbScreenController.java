package application.controllers;


import application.gui.subscreens.skewb.Skewb1lookTrainer;
import application.gui.subscreens.skewb.SkewbAlgGenerator;
import application.gui.subscreens.skewb.SkewbAlgTrainer;
import application.gui.subscreens.skewb.SkewbImageGenerator;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import static Settings.Settings.*;

public class SkewbScreenController implements ScreenController{
    public Label onelookButton;
    @FXML
    private Label algTrainerButton;

    @FXML
    private Label imageGeneratorButton;

    @FXML
    private Label algGeneratorButton;

    @FXML
    private BorderPane skewbScreen;

    @FXML
    private HBox menu;

    private Pane subMenu;

    private SkewbAlgGenerator algGenerator;
    private SkewbImageGenerator imageGenerator;
    private SkewbAlgTrainer algTrainer;

    private Skewb1lookTrainer oneLookTrainer;

    @FXML
    public void initialize() {
        menu.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        algTrainer = new SkewbAlgTrainer(this);
        imageGenerator = new SkewbImageGenerator(this);
        algGenerator = new SkewbAlgGenerator(this);
        oneLookTrainer = new Skewb1lookTrainer(this);
        setupAlgTrainer();
    }

    @FXML
    private void setupAlgTrainer() {
        algTrainerButton.setBackground(new Background(new BackgroundFill(SELECTED_MENU_BUTTON_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        imageGeneratorButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        algGeneratorButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        onelookButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        subMenu = algTrainer;
        skewbScreen.setCenter(algTrainer);
    }

    @FXML
    private void setupImageGenerator() {
        algTrainerButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        imageGeneratorButton.setBackground(new Background(new BackgroundFill(SELECTED_MENU_BUTTON_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        algGeneratorButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        onelookButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        subMenu = imageGenerator;
        skewbScreen.setCenter(imageGenerator);
    }

    @FXML
    private void setupAlgGenerator() {
        algTrainerButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        imageGeneratorButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        algGeneratorButton.setBackground(new Background(new BackgroundFill(SELECTED_MENU_BUTTON_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        onelookButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        subMenu = algGenerator;
        skewbScreen.setCenter(algGenerator);
    }

    @FXML
    private void setup1lookTrainer() {
        algTrainerButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        imageGeneratorButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        algGeneratorButton.setBackground(new Background(new BackgroundFill(SKEWB_MENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        onelookButton.setBackground(new Background(new BackgroundFill(SELECTED_MENU_BUTTON_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        subMenu = oneLookTrainer;
        skewbScreen.setCenter(oneLookTrainer);
    }

    public BorderPane getScreen() {
        return skewbScreen;
    }

    public Pane getSubMenu() {
        return subMenu;
    }
}
