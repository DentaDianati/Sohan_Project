package com.denta.sohan;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label loginSignup;
    
    // تعریف پارامترها
    private Connection connection;
    private PreparedStatement prepard;
    private ResultSet result;

    public void LoginUsers() throws SQLException{
        // برقراری ارتباط با سرور توسط SQL
        connection = DBConnection.getConnection();
        var req = "SELECT * FROM users WHERE username = ? and password = ?";

        try {
            // چک کردن خالی نبودن فیلد ها
            if(username.getText().isEmpty() || password.getText().isEmpty()){
                // هشدار
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("نام کاربری یا رمز عبور خالی است!");
                alert.showAndWait();
            }else{
                // برقراری ارتباط با سرور
                prepard = connection.prepareStatement(req);
                prepard.setString(1, username.getText());
                prepard.setString(2, password.getText());
                result = prepard.executeQuery();
                // چک کردن درستی کاربر
                if(result.next()){
                    // ثبت نام کاربر در سیستم
                    Preferences prefs = Preferences.userNodeForPackage(App.class);
                    prefs.put("user",username.getText());
                    prefs.flush();
                    // بستن صفحه ورود
                    loginBtn.getScene().getWindow().hide();
                    // باز کردن صفحه اصلی فروشگاه
                    Parent parent = FXMLLoader.load(getClass().getResource("home.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(parent);
                    stage.setScene(scene);
                    stage.show();
                }else{
                    // هشدار
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("نام کاربری یا رمز عبور اشتباه است!");
                    alert.showAndWait();
                }
            }
            connection.close();
        } catch (IOException | SQLException | BackingStoreException e) {
            // هشدار
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("ورود کاربر شکست خورد!");
            alert.showAndWait();
        }
    }
    // رفتن به صفحه ثبت نام
    public void goSignup() throws IOException{
        // بستن صقحه ورود
        loginSignup.getScene().getWindow().hide();
        // رفتن به صفحه ثبت نام
        Parent parent = FXMLLoader.load(getClass().getResource("signup.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
}
