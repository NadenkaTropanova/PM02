package com.salesregister.controller.popup;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PopupWindow {
    public static void openWindow(String title, String message) {
        Stage stage = new Stage();

        Label numberAllSymbols = new Label(message);
        numberAllSymbols.setLayoutX(55);
        numberAllSymbols.setLayoutY(20);

        Group group = new Group(numberAllSymbols);
        Scene scene = new Scene(group);

        stage.setScene(scene);
        stage.setWidth(270);

        if (message.length() > 20) {
            stage.setWidth(message.length() * 10);
        }

        stage.setHeight(100);

        stage.setTitle(title);
        stage.show();
    }
}