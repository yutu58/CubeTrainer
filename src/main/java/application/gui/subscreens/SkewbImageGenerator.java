package application.gui.subscreens;

import application.controllers.SkewbScreenController;
import cubes.skewb.SkewbNotations;
import cubes.skewb.SkewbState;
import cubes.skewb.imageGenerators.SkewbL2LImageGenerator;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static Settings.Settings.*;

public class SkewbImageGenerator extends GridPane implements Initializable {
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

    private String pattern;

    private SkewbScreenController controller;

    private ToggleGroup notationGroup;

    public SkewbImageGenerator(SkewbScreenController controller) {
        this.controller = controller;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/subscreens/skewbImageGenerator.fxml"));
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
            l.setBorder(new Border(new BorderStroke(MENU_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            l.setOnMouseEntered((e) -> l.setBackground(new Background(new BackgroundFill(SUBMENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY))));
            l.setOnMousePressed((e) -> l.setBackground(new Background(new BackgroundFill(SELECTED_SUBMENU_BUTTON_COLOR, CornerRadii.EMPTY, Insets.EMPTY))));
            l.setOnMouseReleased((e) -> l.setBackground(new Background(new BackgroundFill(SUBMENU_COLOR, CornerRadii.EMPTY, Insets.EMPTY))));
            l.setOnMouseExited((e) -> l.setBackground(null));
        }
        copyImageButton.setOnMouseClicked((e) -> copyImageToClipboard());
        downloadPNGButton.setOnMouseClicked((e) -> downloadPNG());
        copyLithiumCodeButton.setOnMouseClicked((e) -> copyLithiumCode());

        generateImage();
    }

    @FXML
    private void generateImage() {
        SkewbState skewbState = new SkewbState("00000 11111 22222 33333 44444 55555");          //Solved cubestate
        double scale = 2.0;
        //TODO: Remove duplicate code

        RadioButton selected = (RadioButton) notationGroup.getSelectedToggle();

        if (selected == wcaSkewbButton) {
            String[] moves = setupMoves.getText().trim().split(" ");
            for (String m : moves) {
                if (m.equals("")) {
                    continue;
                }
                int[] triedMoves = SkewbNotations.wcaNotation.get(m);
                if (triedMoves == null) {
                    imageErrorLabel.setText(m +  " is not a valid move!");
                    return;
                }
                skewbState.applyWCAMoves(triedMoves);
            }
            pattern = skewbState.toPattern();
        }
        else if (selected == rubikSkewbButton) {
            String[] moves = setupMoves.getText().trim().split(" ");
            for (String m : moves) {
                if (m.equals("")) {
                    continue;
                }
                int[] triedMoves = SkewbNotations.rubikSkewbNotation.get(m);
                if (triedMoves == null) {
                    imageErrorLabel.setText(m +  " is not a valid move!");
                    return;
                }
                skewbState.applyWCAMoves(triedMoves);
            }
            pattern = skewbState.toPattern();
        }
        else if (selected == codeSkewbButton) {
            pattern = setupMoves.getText();
        }
        else {
            return;
        }

        try {
            //This method changes the scale, even if an exception is thrown;
            SkewbL2LImageGenerator.drawSkewbImage(imageCanvas.getGraphicsContext2D(),
                    pattern, scale, false, true);
            imageErrorLabel.setText("");
        } catch(RuntimeException e) {
            imageErrorLabel.setText("The provided / generated code was invalid.");
        }
        imageCanvas.getGraphicsContext2D().scale(1 / scale, 1 / scale);
    }

    private void copyImageToClipboard() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.IMAGE, imageCanvas.snapshot(null, null));
        clipboard.setContent(content);
    }

    private void downloadPNG() {
        WritableImage image = imageCanvas.snapshot(null, null);

        File file = new File(IMAGE_DOWNLOAD_LOCATION + System.currentTimeMillis() + ".png");
        try {
            ImageIO.write(Objects.requireNonNull(SwingFXUtils.fromFXImage(image, null)), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyLithiumCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(pattern);
        clipboard.setContent(content);
    }
}
