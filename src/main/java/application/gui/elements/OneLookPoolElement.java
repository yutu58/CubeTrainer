package application.gui.elements;

import application.gui.subscreens.skewb.Skewb1lookTrainer;
import cubes.skewb.data.OneLookPool;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class OneLookPoolElement extends TitledPane {
    @FXML
    private VBox oneLookElementContainer;

    @FXML
    private Button setActivePool;

    @FXML
    private Button deletePool;

    private OneLookPool oneLookPool;

    private Skewb1lookTrainer parent;

    public OneLookPoolElement(OneLookPool oneLookPool, Skewb1lookTrainer parent) {
        super();

        this.oneLookPool = oneLookPool;
        this.parent = parent;

        this.setText(oneLookPool.getName());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/elements/oneLookPoolElement.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String setup : this.oneLookPool.getSetups()) {
            try {
                this.oneLookElementContainer.getChildren().add(new OneLookElement(setup, oneLookPool, parent));
            } catch (Exception e) {
                parent.promptError("Couldn't create pool element, is your notation correct?");
                return;
            }
        }

        setActivePool.setOnMouseClicked(mouseEvent -> {
            parent.setActiveSetups(oneLookPool.getSetups());
            parent.setActiveOneLookPoolElement(this);

            //Clear possible image
            GraphicsContext gc = parent.getImageCanvas().getGraphicsContext2D();
            gc.clearRect(0, 0, parent.getImageCanvas().getWidth(), parent.getImageCanvas().getHeight());

            //Notify user of active pool
        });

        deletePool.setOnMouseClicked(mouseEvent -> {
            parent.deletePool(oneLookPool.getName());
        });
    }

    public OneLookPool getOneLookPool() {
        return oneLookPool;
    }
}
