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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SignupController {

    @FXML
    private AnchorPane mainPain;

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
    

    
    private Connection connection;
    private PreparedStatement prepard;


    public void SignupUsers() throws SQLException{
        connection = DBConnection.getConnection();
        var req = "INSERT INTO users (username, password) VALUES (?, ?)";

        try {
            
            if(signupUsername.getText().isEmpty() || signupPassword.getText().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("نام کاربری یا رمز عبور خالی است!");
                alert.showAndWait();
            }else{
                if(signupPassword.getText().equals(signupRPassword.getText())){
                    prepard = connection.prepareStatement(req);
                    prepard.setString(1, signupUsername.getText());
                    prepard.setString(2, signupPassword.getText());

                    
                    if(!prepard.execute()){
                        Preferences prefs = Preferences.userNodeForPackage(App.class);
                        prefs.put("user",signupUsername.getText());
                        signupBtn.getScene().getWindow().hide();

                        Parent parent = FXMLLoader.load(getClass().getResource("home.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.show();
                
                    }else{
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setContentText("ثبت نام شکست خورد. لطفا دوباره امتحان کنید!");
                        alert.showAndWait();
                    }
                }else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("رمز عبور با تکرار رمز عبور مطابقت ندارد!");
                    alert.showAndWait();
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public void goLogin() throws IOException{
        signupLogin.getScene().getWindow().hide();

        Parent parent = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

}
