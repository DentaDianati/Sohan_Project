package com.denta.sohan;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

public class SignupController {
    @FXML
    private Button signupBtn;

    @FXML
    private Label signupLogin;

    @FXML
    private PasswordField signupPassword;

    @FXML
    private PasswordField signupRPassword;

    @FXML
    private TextField signupUsername;
    

    // تعریف پارامترها
    private Connection connection;
    private PreparedStatement prepard;


    public void SignupUsers() throws SQLException{
        // برقراری ارتباط با سرور توسط SQL
        connection = DBConnection.getConnection();
        var req = "INSERT INTO users (username, password) VALUES (?, ?)";

        try {
            // چک کردن خالی نبودن فیلد ها
            if(signupUsername.getText().isEmpty() || signupPassword.getText().isEmpty()){
                // هشدار
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("نام کاربری یا رمز عبور خالی است!");
                alert.showAndWait();
            }else{
                // چک کردن تکرار پسورد
                if(signupPassword.getText().equals(signupRPassword.getText())){
                    // برقراری ارتباط و اضافه کردن کاربر
                    prepard = connection.prepareStatement(req);
                    prepard.setString(1, signupUsername.getText());
                    prepard.setString(2, signupPassword.getText());
                    // چک کردن موفقیت درخواست
                    if(!prepard.execute()){
                        // ثبت نام کاربر در سیستم
                        Preferences prefs = Preferences.userNodeForPackage(App.class);
                        prefs.put("user",signupUsername.getText());
                        // بستن صفحه ثبت نام
                        signupBtn.getScene().getWindow().hide();
                        // باز کردن صفحه اصلی فروشگاه
                        Parent parent = FXMLLoader.load(getClass().getResource("home.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.show();
                    }else{
                        // هشدار
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setContentText("ثبت نام شکست خورد. لطفا دوباره امتحان کنید!");
                        alert.showAndWait();
                    }
                }else{
                    // هشدار
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("رمز عبور با تکرار رمز عبور مطابقت ندارد!");
                    alert.showAndWait();
                }
            }
            // بستن ارتباط
            connection.close();
        } catch (IOException | SQLException e) {
            // هشدار
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("ثبت نام شکست خورد. لطفا دوباره امتحان کنید!");
            alert.showAndWait();
        }
    }
    // رفتن به صفحه ورود
    public void goLogin() throws IOException{
        // بستن صفحه ثبت نام
        signupLogin.getScene().getWindow().hide();
        // رفتن به صفحه ورود
        Parent parent = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

}
