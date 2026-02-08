package com.denta.sohanadmin;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertSohan {
    // هشدار
    public static void error(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    // خبر
    public static void info(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}