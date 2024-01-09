package com.cgvsu;

import com.cgvsu.affine_transformation.AffineTransformation;
import com.cgvsu.help_window.HelpWindow;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.table_models_cell.ActionButtonTableCell;
import com.cgvsu.table_models_cell.ColumnModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javafx.stage.FileChooser;
import javafx.util.Duration;

import javafx.scene.control.TextField;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import javax.vecmath.Vector3f;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    final private float TRANSLATION = 0.5F;

    @FXML
    AnchorPane anchorPane;
    private ObservableList<ColumnModel> data = FXCollections.observableArrayList();

    @FXML
    TableView<ColumnModel> activeModelTable;
    @FXML
    TableColumn<ColumnModel, Button> activeModelColumn;
    @FXML
    TableColumn<ColumnModel, String> modelsNameColumn;

    @FXML
    private Canvas canvas;
    @FXML
    private TextField scaleX;
    @FXML
    private TextField scaleY;
    @FXML
    private TextField scaleZ;

    @FXML
    private TextField expandX;
    @FXML
    private TextField expandY;
    @FXML
    private TextField expandZ;

    @FXML
    private TextField moveX;
    @FXML
    private TextField moveY;
    @FXML
    private TextField moveZ;

    private Model activeMash = null;
    private ArrayList<Model> meshStorage = new ArrayList<Model>();


    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        modelsNameColumn.setCellValueFactory(new PropertyValueFactory<ColumnModel, String>("modelName"));
        activeModelColumn.setCellFactory(ActionButtonTableCell.<ColumnModel>forTableColumn(" make it active", (ColumnModel p) -> {
            //activeModelTable.getItems().remove(p);
            activeMash = meshStorage.get(p.getModelId());
            return p;
        }));

        activeModelTable.setItems(data);

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(50), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (activeMash != null) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, activeMash, (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());
        try {
            String fileContent = Files.readString(fileName);
            activeMash = ObjReader.read(fileContent);
            meshStorage.add(activeMash);
            String activeMashName = fileName.toString().split("\\\\")[fileName.toString().split("\\\\").length - 1];

            data.add(new ColumnModel(activeMashName, meshStorage.size() - 1));
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
    }

    @FXML
    private void saveTheModel() {
        if (activeMash == null) {
            return;
        }
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Save Model");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("OBJ files (*.obj)", "*.obj");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());//Указываем текущую сцену CodeNote.mainStage
        if (file != null) {
            ObjWriter.write(activeMash, file);
        }
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }

    @FXML
    public void scaleTheModel(ActionEvent actionEvent) {
        if (activeMash != null) {
            try {
                double x = Double.parseDouble(scaleX.getText());
                double y = Double.parseDouble(scaleY.getText());
                double z = Double.parseDouble(scaleZ.getText());
                AffineTransformation.scaleModel(activeMash, x, y, z);
            } catch (NumberFormatException e) {
                // Обработка ошибки парсинга
                System.out.println("Ошибка парсинга вещественного числа из текстового поля: " + e.getMessage());
            }
        }
    }

    @FXML
    public void expandTheModel(ActionEvent actionEvent) {
        if (activeMash != null) {
            try {
                double x = Double.parseDouble(expandX.getText());
                double y = Double.parseDouble(expandY.getText());
                double z = Double.parseDouble(expandZ.getText());
                AffineTransformation.rotateModel(activeMash, x, y, z);
            } catch (NumberFormatException e) {
                // Обработка ошибки парсинга
                System.out.println("Ошибка парсинга вещественного числа из текстового поля: " + e.getMessage());
            }
        }
    }

    public void moveTheModel(ActionEvent actionEvent) {
        if (activeMash != null) {
            try {
                double x = Double.parseDouble(moveX.getText());
                double y = Double.parseDouble(moveY.getText());
                double z = Double.parseDouble(moveZ.getText());
                AffineTransformation.translateModel(activeMash, x, y, z);
            } catch (NumberFormatException e) {
                // Обработка ошибки парсинга
                System.out.println("Ошибка парсинга вещественного числа из текстового поля: " + e.getMessage());
            }
        }
    }
    public void openHelpWindow(ActionEvent event) {
        HelpWindow.newWindow("Help");
    }
}