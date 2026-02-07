package com.denta.sohan;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class LoginController {

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane mainPain;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label loginSignup;
    

    
    private Connection connection;
    private PreparedStatement prepard;
    private ResultSet result;

    public void LoginUsers() throws SQLException{
        connection = DBConnection.getConnection();
        var req = "SELECT * FROM users WHERE username = ? and password = ?";

        try {
            
            if(username.getText().isEmpty() || password.getText().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("نام کاربری یا رمز عبور خالی است!");
                alert.showAndWait();
            }else{
                prepard = connection.prepareStatement(req);
                prepard.setString(1, username.getText());
                prepard.setString(2, password.getText());

                result = prepard.executeQuery();
                if(result.next()){
                    Preferences prefs = Preferences.userNodeForPackage(App.class);
                    prefs.put("user",username.getText());
                    prefs.flush();
                    

                    loginBtn.getScene().getWindow().hide();

                    Parent parent = FXMLLoader.load(getClass().getResource("home.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(parent);
                    stage.setScene(scene);
                    stage.show();
                
                }else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("نام کاربری یا رمز عبور اشتباه است!");
                    alert.showAndWait();
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public void goSignup() throws IOException{
        loginSignup.getScene().getWindow().hide();

        Parent parent = FXMLLoader.load(getClass().getResource("signup.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

}
