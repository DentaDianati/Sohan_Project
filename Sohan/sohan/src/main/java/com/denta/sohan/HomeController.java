package com.denta.sohan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private AnchorPane o_page;

    @FXML
    private AnchorPane h_page;

    @FXML
    private AnchorPane b_page;

    @FXML
    private Button ir_exit;


    @FXML
    private MenuButton o_id;
    
    @FXML
    private Label o_cost;

    @FXML
    private Label o_discription;

    @FXML
    private Label o_name;

    @FXML
    private Label o_subject;

    @FXML
    private Label o_cost_total;

    @FXML
    private Label o_order_total;
    
    @FXML
    private TableView<Product> o_view;

    @FXML
    private Label username;

    @FXML
    private Label order;

    @FXML
    private Label order_0;

    @FXML
    private Label order_1;

    @FXML
    private Label order_2;

        @FXML
    private TableColumn<Product, String> o_v_cost;

    @FXML
    private TableColumn<Product, String> o_v_discription;

    @FXML
    private TableColumn<Product, String> o_v_id;

    @FXML
    private TableColumn<Product, String> o_v_name;

    @FXML
    private TableColumn<Product, String> o_v_subject;
    
    @FXML
    private GridPane g_page;

        
    private Connection connection;
    private PreparedStatement prepard;
    private ResultSet result;
    private int total_order = 0;
    private int total_cost = 0;
    List<Product> ps = new ArrayList<>();
    List<Product> all_products = new ArrayList<>();
    private String user;

    

    @FXML
    public void initialize() throws SQLException {
        connection = DBConnection.getConnection();
        String req1 = "SELECT * FROM orders ORDER BY order_id DESC LIMIT 6;";
        prepard = connection.prepareStatement(req1);
        result = prepard.executeQuery();
        int r = 0;
        while (result.next()) {
            String txt = "";
            if(result.getString("status").equals("0")){
                txt = result.getString("order_id") + ".- درحال برسی...";
            }else if(result.getString("status").equals("1")){
                txt = result.getString("order_id") + ".- تایید شده";
            }else if(result.getString("status").equals("2")){
                txt = result.getString("order_id") + ".- رد شده";
            }
            
            Label order_label = new Label(txt);
            order_label.setFont(Font.font("Vazir Bold", 26));
            g_page.add(order_label, 0, r++);
            GridPane.setHalignment(order_label, HPos.CENTER);
            GridPane.setMargin(order_label, new Insets(10, 0, 10, 0));

        }


        Preferences prefs = Preferences.userNodeForPackage(App.class);
        user = prefs.get("user",null);
        username.setText(user);

        connection = DBConnection.getConnection();
        String req = "SELECT * FROM orders WHERE username = ?";
        prepard = connection.prepareStatement(req);
        prepard.setString(1, user);
        result = prepard.executeQuery();

        int count = 0;
        int count_0 = 0;
        int count_1 = 0;
        int count_2 = 0;

            
        while (result.next()) {
            int s = Integer.parseInt(result.getString("status"));
            switch (s) {
                case 0:
                    count_0++;
                    break;
                case 1:
                    count_1++;
                    break;
                case 2:
                    count_2++;
                    break;
                default:
                    break;
            }
            count++;
 
        }
        order.setText(String.valueOf(count));
        order_0.setText(String.valueOf(count_0));
        order_1.setText(String.valueOf(count_1));
        order_2.setText(String.valueOf(count_2));

        o_v_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        o_v_subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        o_v_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        o_v_discription.setCellValueFactory(new PropertyValueFactory<>("discription"));
        o_v_cost.setCellValueFactory(new PropertyValueFactory<>("cost"));


        connection = DBConnection.getConnection();
        String req2 = "SELECT * FROM products WHERE num > 0;";
        prepard = connection.prepareStatement(req2);
        result = prepard.executeQuery();
        
        while (result.next()) {
            Product product = new Product(
                result.getString("cost"),
                result.getString("discription"),
                String.valueOf(result.getInt("id")),
                result.getString("name"),
                result.getString("subject")
            );
            o_view.getItems().add(product);
            all_products.add(product);
            MenuItem menuItem = new MenuItem(String.valueOf(result.getInt("id")));
            menuItem.setOnAction(e -> {
                String orderId = ((MenuItem) e.getSource()).getText();
                o_id.setText(orderId);
                Product prod = all_products.stream().filter(p -> orderId.equals(p.getId())).findFirst().orElse(null);
                o_name.setText(prod.getName());
                o_subject.setText(prod.getSubject());
                o_discription.setText(prod.getDiscription());
                o_cost.setText(prod.getCost() + " تومان");
            

            });
            o_id.getItems().add(menuItem);

        }
        
        ps.addAll(loadObjects());
        for (Product product : ps) {
            total_cost += Integer.parseInt(product.getCost().split(" ")[0]);
            total_order++;

        }
        o_order_total.setText(String.valueOf(total_order));
        o_cost_total.setText(String.valueOf(total_cost) + " تومان");

        
    }
    public void setFiuld(){
        o_id.setText(o_view.getSelectionModel().getSelectedItem().getId());
        o_name.setText(o_view.getSelectionModel().getSelectedItem().getName());
        o_subject.setText(o_view.getSelectionModel().getSelectedItem().getSubject());
        o_discription.setText(o_view.getSelectionModel().getSelectedItem().getDiscription());
        o_cost.setText(o_view.getSelectionModel().getSelectedItem().getCost());
    }

    Gson gson = new Gson();

    public void saveProduct() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("basket.json"))) {
            for (Product p : ps) {
                bw.write(gson.toJson(p));
                bw.newLine();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Product> loadObjects() {
        List<Product> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("basket.json"))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(gson.fromJson(line, Product.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addBasket(){
        String id = o_id.getText();
        String subject = o_subject.getText();
        String name = o_name.getText();
        String discription = o_discription.getText();
        String cost = o_cost.getText();


        if(id.isEmpty() || name.isEmpty() || discription.isEmpty() || cost.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("محصول را انتخاب کنید!");
            alert.showAndWait();
        }else{
            boolean exists = ps.stream()
                .anyMatch(p -> p.getId().equals(id));
            if(!exists){
                Product product = new Product(cost, discription, id, name, subject);
                ps.add(product);
                saveProduct();
                
                
                

                total_order++;
                total_cost += Integer.parseInt(cost.split(" ")[0]);

                o_order_total.setText(String.valueOf(total_order));
                o_cost_total.setText(String.valueOf(total_cost) + " تومان");
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("محصول در سبدخرید موجود است!");
                alert.showAndWait();
            }
        }

    }
    public void removeBasket(){
        String id = o_id.getText();
        String name = o_name.getText();
        String discription = o_discription.getText();
        String cost = o_cost.getText();


        if(id.isEmpty() || name.isEmpty() || discription.isEmpty() || cost.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("محصول را انتخاب کنید!");
            alert.showAndWait();
        }else{
            boolean exists = ps.stream()
                .anyMatch(p -> p.getId().equals(id));
            if(exists){

                ps.removeIf(p -> p.getId().equals(id));
                saveProduct();
                

                total_order--;
                total_cost -= Integer.parseInt(cost.split(" ")[0]);

                o_order_total.setText(String.valueOf(total_order));
                o_cost_total.setText(String.valueOf(total_cost) + " تومان");

            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("محصول در سبدخرید موجود نیست!");
                alert.showAndWait();
            }

            
            

        }

    }

    public void payBasket() throws SQLException{


        if(ps.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("ُسبد خرید خالی است!");
            alert.showAndWait();
        }else{
                String req = "INSERT INTO orders (order_id, username, product_id, cost) VALUES ";
                for (int i = 0; i < total_order; i++) {
                    if (i > 0) req += ", ";
                    req += "(null, ?, ?, ?)";
                }

                PreparedStatement prepard = connection.prepareStatement(req);
                int paramIndex = 1;
                for (int i = 0; i < total_order; i++) {
                    prepard.setString(paramIndex++, user);
                    prepard.setString(paramIndex++, ps.get(i).getId());
                    prepard.setString(paramIndex++, ps.get(i).getCost());
                }

                
     

                

                if(!prepard.execute()){
                    for (Product product1 : ps) {

                        String txt = product1.getId() + " درحال برسی.." ;
                        Label order_label = new Label(txt);
                        order_label.setFont(Font.font("Vazir Bold", 26));
                        g_page.add(order_label, 0, 0);
                    }
                    ps.clear();
                    saveProduct();
                    order.setText(String.valueOf(Integer.parseInt(order.getText()) + total_order));
                    order_0.setText(String.valueOf(Integer.parseInt(order_0.getText()) + total_order));
                    total_cost = 0;
                    total_order = 0;
                    o_cost_total.setText("0 نومان");
                    o_order_total.setText("0");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setContentText("ُخربد با موفقیت انجام شد!");
                    alert.showAndWait();

                    
                }else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("خرید شکست خورد. لطفا دوباره امتحان کنید!");
                    alert.showAndWait();
                }
            }
            

        

        
    }

    public void exit() throws IOException{
        Preferences prefs = Preferences.userNodeForPackage(App.class);
        prefs.remove("user");

        ir_exit.getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene); 
        stage.show();

       
    }
    public void setPostPage(){
        h_page.setVisible(false);
        o_page.setVisible(true);
        b_page.setVisible(false);
    }
    public void setHomePage(){
        h_page.setVisible(true);
        o_page.setVisible(false);
        b_page.setVisible(false);
    }
    public void setBuyPage(){
        h_page.setVisible(false);
        o_page.setVisible(false);
        b_page.setVisible(true);
    }

    
}