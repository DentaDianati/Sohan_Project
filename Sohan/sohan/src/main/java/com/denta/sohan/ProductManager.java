package com.denta.sohan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class ProductManager{

    // ذخیره سبد خرید در حافظه
    public static void saveProduct(List<Product> ps) {
        // ساخت یا باز کردن فایل json
        Gson gson = new Gson();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("basket.json"))) {
            // پیمایش در لیست سبد خرید و ذخیره در فایل
            for (Product p : ps) {
                bw.write(gson.toJson(p));
                bw.newLine();
            }
        }catch (IOException e) {
            // هشدار
            AlertSohan.error("مشکلی در خواندن حافظه پیش آمد!");
        }
    }
        // خواندن سبد خرید از حافظه
    public static List<Product> loadProducts() {
        List<Product> list = new ArrayList<>();
        // خواندن فایل
        Gson gson = new Gson();
        try (BufferedReader br = new BufferedReader(new FileReader("basket.json"))) {
            String line;
            // تا زمانی که فایل تمام شود محصولات را به سبد اضافه می کند
            while ((line = br.readLine()) != null) {
                list.add(gson.fromJson(line, Product.class));
            }
        } catch (IOException e) {
            // هشدار
            AlertSohan.error("مشکلی در خواندن حافظه پیش آمد!");
        }
        return list;
    }
}