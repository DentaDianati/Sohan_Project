package com.denta.sohan;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    // تعریف پارامتر ها
    private Connection connection;
    private PreparedStatement prepard;
    private ResultSet result;
    private int total_order, total_cost = 0;
    List<Product> ps,all_products = new ArrayList<>();
    private String user;
    // تنظیمات اولیه
    @FXML
    public void initialize() throws SQLException {
        // گرفتن نام کاربر از حاقظه
        Preferences prefs = Preferences.userNodeForPackage(App.class);
        user = prefs.get("user",null);
        username.setText(user);
        // وصل کردن ابجکت محصول به ستون های حدول
        o_v_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        o_v_subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        o_v_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        o_v_discription.setCellValueFactory(new PropertyValueFactory<>("discription"));
        o_v_cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        // برقرار ارتباط با سرور با SQL (صفحه محصولات)
        connection = DBConnection.getConnection();
        String req2 = "SELECT * FROM products WHERE num > 0;";
        prepard = connection.prepareStatement(req2);
        result = prepard.executeQuery();
        // پیمایش در محصول ها
        while (result.next()) {
            // ساخت محصول
            Product product = new Product(
                result.getString("cost"),
                result.getString("discription"),
                String.valueOf(result.getInt("id")),
                result.getString("name"),
                result.getString("subject")
            );
            // اضافه کردن به حدول و لیست محصولات
            o_view.getItems().add(product);
            all_products.add(product);
            // اضافه کردن شماره سفارش ها به منو ایتم ها
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
        // برقرار ارتباط با سرور با SQL (شماره سفارشات)
        connection = DBConnection.getConnection();
        String req = "SELECT * FROM orders WHERE username = ?";
        prepard = connection.prepareStatement(req);
        prepard.setString(1, user);
        result = prepard.executeQuery();
        // شماره سفارشات و نوع آن برای صفحه اصلی
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
        // تنظیم شمارش روی لیبل ها
        order.setText(String.valueOf(count));
        order_0.setText(String.valueOf(count_0));
        order_1.setText(String.valueOf(count_1));
        order_2.setText(String.valueOf(count_2));
        // برقرار ارتباط با سرور با SQL (صفحه سفارشات)
        connection = DBConnection.getConnection();
        String req1 = "SELECT * FROM orders WHERE username = ? ORDER BY order_id DESC LIMIT 6;";
        prepard = connection.prepareStatement(req1);
        prepard.setString(1, user);
        result = prepard.executeQuery();
        // اضافه کردن سفارشات به صفحه سفارشات
        int r = 0;
        while (result.next()) {
            String txt = "";
            switch (result.getString("status")) {
                case "0":
                    txt = "محصول شماره " +  result.getString("product_id") + ".- درحال برسی...";
                    break;
                case "1":
                    txt = "محصول شماره " +  result.getString("product_id") + ".- تایید شده";
                    break;
                case "2":
                    txt = "محصول شماره " + result.getString("product_id") + ".- رد شده";
                    break;
                default:
                    break;
            }
            // ساخت ابجکت لیبل و ویژگی های آن
            Label order_label = new Label(txt);
            order_label.setFont(Font.font("Vazir Bold", 26));
            g_page.add(order_label, 0, r++);
            GridPane.setHalignment(order_label, HPos.CENTER);
            GridPane.setMargin(order_label, new Insets(10, 0, 10, 0));
        }
        // اضافه کردن سبد خرید از حافظه
        ps.addAll(ProductManager.loadProducts());
        for (Product product : ps) {
            total_cost += Integer.parseInt(product.getCost().split(" ")[0]);
            total_order++;

        }
        // به روزرسانی فیلد ها از حافظه
        o_order_total.setText(String.valueOf(total_order));
        o_cost_total.setText(String.valueOf(total_cost) + " تومان");
    }
    // تنظیم سلکشن جدول
    public void setFiuld(){
        // مقدار دهی فیلد ها با جدول
        o_id.setText(o_view.getSelectionModel().getSelectedItem().getId());
        o_name.setText(o_view.getSelectionModel().getSelectedItem().getName());
        o_subject.setText(o_view.getSelectionModel().getSelectedItem().getSubject());
        o_discription.setText(o_view.getSelectionModel().getSelectedItem().getDiscription());
        o_cost.setText(o_view.getSelectionModel().getSelectedItem().getCost() + " تومان");
    }
    // اضافه کردن محصول به سبد خرید
    public void addBasket(){
        // گرفتن اطلاعات محصول
        String id = o_id.getText();
        String subject = o_subject.getText();
        String name = o_name.getText();
        String discription = o_discription.getText();
        String cost = o_cost.getText();
        // چک کردن خالی نبودن اطلاعات
        if(id.isEmpty() || name.isEmpty() || discription.isEmpty() || cost.isEmpty()){
            // هشدار
            AlertSohan.error("محصول را انتخاب کنید!");
        }else{
            // گرفتن محصول از لیست سبد خرید
            boolean exist = ps.stream()
                .anyMatch(p -> p.getId().equals(id));
            // چک کردن موجود بودن در سبد خرید
            if(!exist){
                // ساخت محصول و اضافه کردن به سبد
                Product product = new Product(cost, discription, id, name, subject);
                ps.add(product);
                // ذخیره در حافظع
                ProductManager.saveProduct(ps);
                // به روزرسانی پارامتر ها و فیلتد ها
                total_order++;
                total_cost += Integer.parseInt(cost.split(" ")[0]);
                o_order_total.setText(String.valueOf(total_order));
                o_cost_total.setText(String.valueOf(total_cost) + " تومان");
            }else{
                // هشدار
                AlertSohan.error("محصول در سبدخرید موجود است!");
            }
        }

    }
    // حذف کردن محصول از سبد خرید
    public void removeBasket(){
        // گرفتن اطلاعات محصول
        String id = o_id.getText();
        String name = o_name.getText();
        String discription = o_discription.getText();
        String cost = o_cost.getText();
        // چک کردن خالی نبودن اطلاعات
        if(id.isEmpty() || name.isEmpty() || discription.isEmpty() || cost.isEmpty()){
            // هشدار
            AlertSohan.error("محصول را انتخاب کنید!");
        }else{
            // گرفتن محصول از لیست سبد خرید
            boolean exist = ps.stream()
                .anyMatch(p -> p.getId().equals(id));
            // چک کردن موجود بودن در سبد خرید
            if(exist){
                // حذف از لیست سبد
                ps.removeIf(p -> p.getId().equals(id));
                // حذف از حافظه
                ProductManager.saveProduct(ps);
                // به روزرسانی پارامتر ها و فیلتد ها
                total_order--;
                total_cost -= Integer.parseInt(cost.split(" ")[0]);
                o_order_total.setText(String.valueOf(total_order));
                o_cost_total.setText(String.valueOf(total_cost) + " تومان");
            }else{
                // هشدار
                AlertSohan.error("محصول در سبدخرید موجود نیست!");
            }
        }
    }
    // پرداخت
    public void payBasket() throws SQLException{
        // چک کردن پر بودن سبد خرید
        if(ps.isEmpty()){
            // هشدار
            AlertSohan.error("سبد خرید خالی است!");
        }else{
            // ساخت درخواست ها برای ارسال به سرور
            String req = "INSERT INTO orders (order_id, username, product_id, cost) VALUES ";
            for (int i = 0; i < total_order; i++) {
                if (i > 0) req += ", ";
                req += "(null, ?, ?, ?)";
            }
            // ثبت اطلاعات بر درخواست
            prepard = connection.prepareStatement(req);
            int paramIndex = 1;
            for (int i = 0; i < total_order; i++) {
                prepard.setString(paramIndex++, user);
                prepard.setString(paramIndex++, ps.get(i).getId());
                prepard.setString(paramIndex++, ps.get(i).getCost());
            }
            // چک کردن موقثیت درخواست
            if(!prepard.execute()){
                // پیمایش در سبد خرید
                for (Product product1 : ps) {
                    // اضافه کردن سفارش ها به سفارشات
                    String txt = "محصول شماره " +  product1.getId() + ".- درحال برسی..." ;
                    Label order_label = new Label(txt);
                    order_label.setFont(Font.font("Vazir Bold", 26));
                    // همه سفارشات رو یکی بالا بردن
                    for (Node node : g_page.getChildren()) {
                        Integer row = GridPane.getRowIndex(node);
                        GridPane.setRowIndex(node, row == null ? 1 : row + 1);
                    }
                    // اگر از 5 تا رد کرد سفارش حذف میشود
                    g_page.getChildren().removeIf(node -> {
                        Integer row = GridPane.getRowIndex(node);
                        return row != null && row >= 6;
                    });
                    // اضافه کردن سفارش جدید
                    g_page.add(order_label, 0, 0);
                    GridPane.setHalignment(order_label, HPos.CENTER);
                    GridPane.setMargin(order_label, new Insets(10, 0, 10, 0));
                }
                // خالی کردن لیست سبد خرید
                ps.clear();
                // خالی کردن سبد خرید در حافظه
                ProductManager.saveProduct(ps);
                // به روزرسانی پارامتر ها
                order.setText(String.valueOf(Integer.parseInt(order.getText()) + total_order));
                order_0.setText(String.valueOf(Integer.parseInt(order_0.getText()) + total_order));
                total_cost = 0;
                total_order = 0;
                o_cost_total.setText("0 نومان");
                o_order_total.setText("0");
                // خبر
                AlertSohan.info("پرداخت با موفقیت انجام شد!");
            }else{
                // هشدار
                AlertSohan.error("خرید شکست خورد، لطفا دوباره امتحان کنید!");
            }
        }  
    }
    // خروج از حساب کاربری
    public void exit() throws IOException{
        // حذف کاربر از حافظه
        Preferences prefs = Preferences.userNodeForPackage(App.class);
        prefs.remove("user");
        // بستن صفحه اصلی
        ir_exit.getScene().getWindow().hide();
        // رفتن به صفحه ورود
        Parent parent = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene); 
        stage.show();
    }
    //تنظیمات پیمایش بین صفحات
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