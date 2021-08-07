package application.gui.subscreens;

import application.controllers.SkewbScreenController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SkewbAlgGenerator extends BorderPane implements Initializable {



    public SkewbAlgGenerator(SkewbScreenController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/subscreens/skewbAlgGenerator.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            System.out.println("There was a problem loading the FXML");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
