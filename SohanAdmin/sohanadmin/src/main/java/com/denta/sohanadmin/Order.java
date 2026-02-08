package com.denta.sohanadmin;

public class Order {
    // تعریف پارامتر های سفارش
    private String order_id,username,product_id,status,cost;
    // تعریف کنارستراکتور سفارش
    public Order(String cost, String order_id, String product_id, String status, String username) {
        this.cost = cost;
        this.order_id = order_id;
        this.product_id = product_id;
        this.status = status;
        this.username = username;
    }
    //تعریف دسترسی به سفارش
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getProduct_id() {
        return product_id;
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
}