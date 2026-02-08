package com.denta.sohan;

import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {
    

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // پرفتن نام کاربری از حافظه سیستم
        Preferences prefs = Preferences.userNodeForPackage(App.class);
        String user = prefs.get("user",null);
        // چگ کردن موجود بودن کاربر
        if(user == null){
            // رفتن به صفحه ورود
            scene = new Scene(loadFXML("login"));
            stage.setScene(scene);
            stage.show();
        }else{
            // رفتن به صفحه اصلی
            scene = new Scene(loadFXML("home"));
            stage.setScene(scene);
            stage.show();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}