package application.gui.elements;

import application.gui.subscreens.skewb.SkewbAlgTrainer;
import cubes.skewb.data.AlgPool;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class AlgPoolElement extends HBox {
    @FXML
    private Label poolName;

    @FXML
    private Label numberSets;

    @FXML
    private Label numberCases;

    @FXML
    private Button setActivePool;

    @FXML
    private Button updatePoolSelection;

    @FXML
    private Button runPoolAnalytics;

    @FXML
    private Button deletePool;

    AlgPool algPool;

    SkewbAlgTrainer parent;

    public AlgPoolElement(AlgPool algPool, SkewbAlgTrainer parent) {
        super();

        this.algPool = algPool;
        this.parent = parent;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/elements/algPoolElement.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.poolName.setText(algPool.getName());
        this.numberSets.setText(String.valueOf(algPool.getSetIds().size()));

        //This assumes the parent.getCases() (Gets all loaded cases) are ordered by ID
        this.numberCases.setText(String.valueOf(algPool.getSetIds().
                stream().map(x -> parent.getAllSets().get(x-1).getCases().size()).reduce(0, Integer::sum)));

        setActivePool.setOnMouseClicked(mouseEvent -> {
            parent.setSelection(algPool.getSetIds(), true);
        });

        updatePoolSelection.setOnMouseClicked(mouseEvent -> algPool.setSetIds(parent.getSetSelection()));

        runPoolAnalytics.setOnMouseClicked(mouseEvent -> {
            //TBI
        });

        deletePool.setOnMouseClicked(mouseEvent -> parent.deletePool(this.algPool.getName()));
    }

    public AlgPool getAlgPool() {
        return algPool;
    }
}
