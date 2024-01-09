package com.cgvsu.help_window;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class HelpWindow {
    public static void newWindow(String title) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #292929;");

        Text helpText = new Text();
        helpText.setFont(new Font(24));
        helpText.setTextAlignment(TextAlignment.CENTER);
        helpText.setX(50);
        helpText.setY(50);
        helpText.setFill(Color.rgb(222, 222, 222));

        helpText.setText("In this program, you can change 3D models in the format.obj,\n" +
                "as well as save your model changes in this format\n" +
                "GOOOOOOOOOOOOOOOOOD LUCK");
        pane.getChildren().add(helpText);
        Scene scene = new Scene(pane, 750, 400);
        window.setScene(scene);
        window.setTitle(title);
        window.showAndWait();

    }
}
