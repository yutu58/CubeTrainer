package application.gui.subscreens.tx2;

import application.controllers.Tx2ScreenController;
import cubes.skewb.SkewbNotations;
import cubes.skewb.SkewbState;
import cubes.skewb.imageGenerators.SkewbL2LImageGenerator;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static Settings.Settings.*;

public class Tx2ImageGenerator extends GridPane implements Initializable {
    @FXML
    private Button applyButton;

    @FXML
    private Label imageErrorLabel;

    @FXML
    private Canvas imageCanvas;

    @FXML
    private RadioButton rubikSkewbButton;

    @FXML
    private RadioButton wcaSkewbButton;

    @FXML
    private RadioButton codeSkewbButton;

    @FXML
    private Label copyImageButton;

    @FXML
    private Label downloadPNGButton;

    @FXML
    private Label copyLithiumCodeButton;

    @FXML
    private TextField setupMoves;

    @FXML
    private Slider scaleSlider;

    @FXML
    private CheckBox reverseBox;

    @FXML
    private CheckBox bottomBox;

    private String pattern;

    private Tx2ScreenController controller;

    private ToggleGroup notationGroup;

    public Tx2ImageGenerator(Tx2ScreenController controller) {
        this.controller = controller;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/subscreens/skewb/skewbImageGenerator.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was a problem loading the FXML");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notationGroup = new ToggleGroup();

        rubikSkewbButton.setToggleGroup(notationGroup);
        rubikSkewbButton.setSelected(true);
        wcaSkewbButton.setToggleGroup(notationGroup);
        codeSkewbButton.setToggleGroup(notationGroup);

        applyButton.setOnAction((e) -> generateImage());

        for (Label l : new ArrayList<Label>(){{
            add(copyImageButton);
            add(downloadPNGButton);
            add(copyLithiumCodeButton);
        }}) {
            l.setBorder(new Border(new BorderStroke(SKEWB_MENU_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            l.setOnMouseEntered((e) -> l.setBackground(new Background(new BackgroundFill(SUBMENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY))));
            l.setOnMousePressed((e) -> l.setBackground(new Background(new BackgroundFill(SELECTED_SUBMENU_BUTTON_COLOR, CornerRadii.EMPTY, Insets.EMPTY))));
            l.setOnMouseReleased((e) -> l.setBackground(new Background(new BackgroundFill(SUBMENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY))));
            l.setOnMouseExited((e) -> l.setBackground(null));
        }

        scaleSlider.valueChangingProperty().addListener((observable, oldValue, newValue) -> generateImage());

        copyImageButton.setOnMouseClicked((e) -> copyImageToClipboard());
        downloadPNGButton.setOnMouseClicked((e) -> downloadPNG());
        copyLithiumCodeButton.setOnMouseClicked((e) -> copyLithiumCode());

        generateImage();
    }

    @FXML
    private void generateImage() {
        RadioButton selected = (RadioButton) notationGroup.getSelectedToggle();

        SkewbNotations.notationEnum n;

        if (selected == rubikSkewbButton) {
            n = SkewbNotations.notationEnum.RubikSkewbNotation;
        } else if (selected == wcaSkewbButton) {
            n = SkewbNotations.notationEnum.WCASkewbNotation;
        } else {
            n = SkewbNotations.notationEnum.LithiumSkewbCode;
        }

        String setup = setupMoves.getText();
        GraphicsContext gc = imageCanvas.getGraphicsContext2D();
        double scale = scaleSlider.getValue() * 2;
        boolean reverse = reverseBox.isSelected();

        try {
            pattern = SkewbL2LImageGenerator.drawImageFromSetup(n, setup, gc, scale, reverse, new SkewbState("00000 11111 22222 33333 44444 55555")); //Solved cubestate
        } catch (RuntimeException e) {
            promptError(e.getMessage());
        }

    }

    private void copyImageToClipboard() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.IMAGE, imageCanvas.snapshot(null, null));
        clipboard.setContent(content);
        promptSuccess("Image copied to clipboard!");
    }

    private void downloadPNG() {
        WritableImage image = imageCanvas.snapshot(null, null);

        File file = new File(IMAGE_DOWNLOAD_LOCATION + System.currentTimeMillis() + ".png");
        try {
            ImageIO.write(Objects.requireNonNull(SwingFXUtils.fromFXImage(image, null)), "png", file);
            promptSuccess("Image successfully downloaded!");
        } catch (IOException e) {
            promptError("Couldn't download image, unknown path!");
            e.printStackTrace();
        }
    }

    private void copyLithiumCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(pattern);
        clipboard.setContent(content);
        promptSuccess("Code copied to clipboard!");
    }

    private void promptSuccess(String succ) {
        imageErrorLabel.setText(succ);
        imageErrorLabel.setTextFill(Color.GREEN);
    }

    private void promptError(String err) {
        imageErrorLabel.setText(err);
        imageErrorLabel.setTextFill(Color.RED);
    }
}
