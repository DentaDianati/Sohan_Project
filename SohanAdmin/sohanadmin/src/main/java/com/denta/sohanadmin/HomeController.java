package com.denta.sohanadmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;


public class HomeController {
    @FXML
    private Button b_add;


    @FXML
    private ComboBox<?> b_id;

    @FXML
    private Spinner<?> b_number;

    @FXML
    private AnchorPane b_page;

    @FXML
    private Button b_pay;

    @FXML
    private ComboBox<?> b_subject;

    @FXML
    private Label b_total;

    @FXML
    private TableView<Order> b_v;

    @FXML
    private TableColumn<Order, String> b_v_cost;

    @FXML
    private TableColumn<Order, String> b_v_discription;

    @FXML
    private TableColumn<Order, String> b_v_id;

    @FXML
    private TableColumn<Order, String> b_v_name;

    @FXML
    private TableColumn<Order, String> b_v_subject;

    @FXML
    private AreaChart<Product, String> h_chart;

    @FXML
    private AnchorPane h_page;

    @FXML
    private Label h_total_money;

    @FXML
    private Label h_total_object;

    @FXML
    private Button ir_exit;

    @FXML
    private Button ir_home;

    @FXML
    private Button ir_post;

    @FXML
    private Button ir_shop;

    @FXML
    private Button o_add;

    @FXML
    private Button o_change;

    @FXML
    private Button o_clear;

    @FXML
    private TextField o_cost;

    @FXML
    private Button o_delete;

    @FXML
    private TextField o_discription;

    @FXML
    private TextField o_id;

    @FXML
    private TextField o_name;

    @FXML
    private AnchorPane o_page;

    @FXML
    private MenuItem o_s_p1;

    @FXML
    private MenuItem o_s_p2;

    @FXML
    private MenuItem o_s_p3;

    @FXML
    private MenuButton o_subject;

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
    private TableView<Product> o_view;

    @FXML
    private Label order_total;

    @FXML
    private Label cost_total;
    
    @FXML
    private MenuButton b_order_id;

    @FXML
    private Label b_product_id;

    @FXML
    private Label b_cost;

    // تعریف اولیه متغیر ها
    private Connection connection;
    private PreparedStatement prepard;
    private ResultSet result;
    private int productCount = 0;
    private int ct = 0;
    private int ot = 0;
    List<Order> orders = new ArrayList<>();

    //راه اندازی اولیه صفحه ادمین
    @FXML
    public void initialize() throws SQLException {
        // وصل کردن ستون ها به ابجت محصول
        o_v_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        o_v_subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        o_v_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        o_v_discription.setCellValueFactory(new PropertyValueFactory<>("discription"));
        o_v_cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        //برقرار ارتباط با سرور و گرفتن محصولات
        connection = DBConnection.getConnection();
        String req = "SELECT * FROM products WHERE num > 0;";
        prepard = connection.prepareStatement(req);
        result = prepard.executeQuery();
        // اضافه کردن محصولات به جدول
        while (result.next()) {
            Product product = new Product(
                result.getString("cost"),
                result.getString("discription"),
                String.valueOf(result.getInt("id")),
                result.getString("name"),
                result.getString("subject")
            );
            o_view.getItems().add(product);
            productCount++;
        }
        //نمایش تعداد کل محصولات
        h_total_object.setText(String.valueOf(productCount));
        //برقرار ارتباط با سرور و گرفتن سفارشات
        connection = DBConnection.getConnection();
        String req2 = "SELECT * FROM orders;";
        prepard = connection.prepareStatement(req2);
        result = prepard.executeQuery();
        // اضافه کردن سفارشات به جدول
        while (result.next()) {
            Order order = new Order(
                // اضافه کردن سفارش
                result.getString("order_id"),
                result.getString("username"),
                result.getString("product_id"),
                result.getString("status"),
                result.getString("cost")
            );
            b_v.getItems().add(order);
            // اضافه کردن شماره سفارش به منو و تنظیم آن
            MenuItem mt = new MenuItem(result.getString("order_id"));
            mt.setOnAction(e -> {
                b_order_id.setText(((MenuItem) e.getSource()).getText());
            });
            b_order_id.getItems().add(mt);
        }
        
    }
    // تنظیم کردن انتقال بین صفحات ادمین
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
    // خروج از برنامه
    public void exit(){
        ir_exit.getScene().getWindow().hide();
    }
    //اضافه کردن محصول به فروشگاه
    public void addPost() throws SQLException{
        // گرفتن اطلاعت وارد شده
        String id = o_id.getText();
        String subject = o_subject.getText();
        String name = o_name.getText();
        String discription = o_discription.getText();
        String cost = o_cost.getText();

        // گرفتن محصول اگر قبلا وارد شده
        boolean exists = o_view.getItems()
            .stream()
            .anyMatch(p -> id.equals(p.getId()));
        // چک کردن اینکه همه فیلد ها پر باشد
        if(id.isEmpty() || name.isEmpty() || discription.isEmpty() || cost.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("همه اطلاعات را وارد کنید!");
            alert.showAndWait();
        }else{
            // چک کردن اینکه محصول تکراری نباشد
            if(!exists){
                //برقرار کردن ارتباط با سرور و درخواست
                connection = DBConnection.getConnection();
                var req = "INSERT INTO products (id, name, subject, discription, cost, num) VALUES (?, ?, ?, ?, ?, ?);";

                prepard = connection.prepareStatement(req);
                prepard.setString(1, id);
                prepard.setString(2, name);
                prepard.setString(3, subject);
                prepard.setString(4, discription);
                prepard.setString(5, cost);
                prepard.setString(6, "100");

                //چک کردن موفقیت درخواست
                if(!prepard.execute()){
                    //ساخت ابجکت محصول و اضافه کردن به جدول
                    Product product = new Product(cost, discription, id, name, subject);
                    o_view.getItems().add(product);
                    //تمیز کردن فیلد ها و اپدیت
                    o_id.setText("");
                    o_subject.setText("پسته");
                    o_name.setText("");
                    o_discription.setText("");
                    o_cost.setText("");
                    productCount++;
                    h_total_object.setText(String.valueOf(productCount));
                    
                }else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("پست کردن شکست خورد. لطفا دوباره امتحان کنید!");
                    alert.showAndWait();
                }
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("شماره محصول موجود می باشد!");
                alert.showAndWait();
            }
            

        }

        
    }
    // اپدیت کردن محصول در فروشگاه
    public void updatePost() throws SQLException{
        // گرفتن اطلاعت وارد شده
        String id = o_id.getText();
        String subject = o_subject.getText();
        String name = o_name.getText();
        String discription = o_discription.getText();
        String cost = o_cost.getText();

        // گرفتن محصول اگر قبلا وارد شده
        boolean exists = o_view.getItems()
            .stream()
            .anyMatch(p -> id.equals(p.getId()));
        // چک کردن محصول انتخاب شده
        if(id.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("شماره محصول را وارد کنید!");
            alert.showAndWait();
        }else{
            // چک کردن موحود بودن محصول برای تغییر
            if(exists){
                //برقرار کردن ارتباط با سرور و درخواست
                connection = DBConnection.getConnection();
                var req = "UPDATE products SET name = ?, subject = ?, discription = ?, cost = ? WHERE products.id = ? ;";
                prepard = connection.prepareStatement(req);
                
                prepard.setString(1, name);
                prepard.setString(2, subject);
                prepard.setString(3, discription);
                prepard.setString(4, cost);
                prepard.setString(5, id);
                // چک کردن موفقیت اموزش بودن فرایند
                if(!prepard.execute()){
                    // اپدیت کردن جدول
                   o_view.getItems().stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .ifPresent(p -> {
                        p.setName(name);
                        p.setSubject(subject);
                        p.setDiscription(discription);
                        p.setCost(cost);
                    }); 
                    // تمیز کردن جدول
                    o_view.refresh();
                    o_id.setText("");
                    o_subject.setText("پسته");
                    o_name.setText("");
                    o_discription.setText("");
                    o_cost.setText("");

                    
                }else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("تغییر پست شکست خورد. لطفا دوباره امتحان کنید!");
                    alert.showAndWait();
                }
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("شماره محصول موجود نمی باشد. لطفا دوباره امتحان کنید!");
                alert.showAndWait();
            }
            

        }


        
    }
    // حذف کردن محصول از فروشگاه
    public void deletePost() throws SQLException{
        // گرفتن ایتم انتخاب شده
        Product selected = o_view.getSelectionModel().getSelectedItem();
        // چک کردن ایتم انتخاب شده
        if(selected != null){
            // برقراری ارتباط و درخواست به سرور
            connection = DBConnection.getConnection();
            var req = "DELETE FROM products WHERE products.id = ?;";
            prepard = connection.prepareStatement(req);
            prepard.setString(1, selected.getId());
            // چک کردن موفقیت درخواست
            if(!prepard.execute()){
                // حدف محصول از جدول و تغییر فیلتد ها
                o_view.getItems().remove(selected);
                productCount--;
                h_total_object.setText(String.valueOf(productCount));
            }else{
               Alert alert = new Alert(AlertType.ERROR);
               alert.setContentText("حذف پست شکست خورد. لطفا دوباره امتحان کنید!");
               alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("پست مورد نظر را انتخاب کنید!");
            alert.showAndWait();
        }
    }
    //تمیز کردن فیلد ها 
    public void clearPost(){
        o_id.setText("");
        o_subject.setText("پسته");
        o_name.setText("");
        o_discription.setText("");
        o_cost.setText("");
    }
    // پر گردن فیلد های محصول از ایتم انتخاب شده
    public void setFiuld(){
        o_id.setText(o_view.getSelectionModel().getSelectedItem().getId());
        o_name.setText(o_view.getSelectionModel().getSelectedItem().getName());
        o_subject.setText(o_view.getSelectionModel().getSelectedItem().getSubject());
        o_discription.setText(o_view.getSelectionModel().getSelectedItem().getDiscription());
        o_cost.setText(o_view.getSelectionModel().getSelectedItem().getCost());
    }
     // پر کردن فیلد های سفارش از ایتم انتخاب شده
    public void setFiuldOrder(){
        b_order_id.setText(b_v.getSelectionModel().getSelectedItem().getOrder_id());
        b_product_id.setText(b_v.getSelectionModel().getSelectedItem().getProduct_id());
        b_cost.setText(b_v.getSelectionModel().getSelectedItem().getCost());

    }
    // تنظیم منو نوع محصول
    public void setMenuP1(){
        o_subject.setText(o_s_p1.getText());
    }
    public void setMenuP2(){
        o_subject.setText(o_s_p2.getText());
        
    }
    public void setMenuP3(){
        o_subject.setText(o_s_p3.getText());
    }
    // اضافه کردن سفارش برای تایید یا رد
    public void addOrder(){
        // گرفتن سفارش انتخاب شده و اضافه کردن به لیست
        Order order = b_v.getSelectionModel().getSelectedItem();
        // چک کردن سفارش انتخاب شده
        if(order != null){
            orders.add(order);
            // اپدیت متغیر ها و فیلد ها
            ct += Integer.getInteger(order.getCost());
            ot++;
            cost_total.setText(String.valueOf(ct));
            order_total.setText(String.valueOf(ot));
        }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("سفارش مورد نظر را انتخاب کنید!");
            alert.showAndWait();
        }

    }
    // تایید سفارش ها
    public void confirOrder() throws SQLException{
        // شک کردن سفارش های انتخاب شده
        if(orders.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("سفارشی رو انتخاب کنید!");
            alert.showAndWait();
        }else{
            for (Order o : orders) {
                // ارتباط با سرور و تایید 
                connection = DBConnection.getConnection();
                var req = "UPDATE orders SET status = 'تایید شده' WHERE products.id = ? ;";
                prepard = connection.prepareStatement(req);
                prepard.setString(1, o.getOrder_id());
                // اپدیت حدول و فیلد ها
                b_v.getItems().stream()
                .filter(p -> p.getOrder_id().equals(o.getOrder_id()))
                .findFirst()
                .ifPresent(p -> {
                    p.setStatus("تایید شده");
                });
                int money_pay = Integer.getInteger(h_total_money.getText()) + ct;
                h_total_money.setText(String.valueOf(money_pay));
                // تمیز کردن لیست سفارشات انتخاب شده
                orders.clear();
            }
        }
    }
    // رد سفارش ها
    public void disOrder() throws SQLException{
        // چک کردن سفارش های انتخاب شده
        if(orders.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("سفارشی رو انتخاب کنید!");
            alert.showAndWait();
        }else{
            for (Order o : orders) {
                // ارتباط با سرور و تایید 
                connection = DBConnection.getConnection();
                var req = "UPDATE orders SET status = 'رد شده' WHERE products.id = ? ;";
                prepard = connection.prepareStatement(req);
                prepard.setString(1, o.getOrder_id());
                // اپدیت حدول
                b_v.getItems().stream()
                .filter(p -> p.getOrder_id().equals(o.getOrder_id()))
                .findFirst()
                .ifPresent(p -> {
                    p.setStatus("رد شده");
                }); 
                // تمیز کردن لیست سفارشات انتخاب شده
                orders.clear();
            }
        }
    }

}  