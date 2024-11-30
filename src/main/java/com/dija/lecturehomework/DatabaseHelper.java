package com.dija.lecturehomework;


import com.dija.lecturehomework.model.Category;
import com.dija.lecturehomework.model.Orders;
import com.dija.lecturehomework.model.Pizza;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String URL = "jdbc:sqlite:pizza.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }


    public static List<Pizza> getPizzas(String pname, String category, Boolean vegetarian) throws SQLException {
        List<Pizza> pizzas = new ArrayList<>();
        String query = "SELECT * FROM pizza WHERE 1=1";

        if (pname != null && !pname.isEmpty()) {
            query += " AND pname LIKE ?";
        }
        if (category != null && !category.isEmpty()) {
            query += " AND categoryname = ?";
        }
        if (vegetarian != null) {
            query += " AND vegetarian = ?";
        }

        try (Connection connection = connect(); PreparedStatement stmt = connection.prepareStatement(query)) {
            int index = 1;
            if (pname != null && !pname.isEmpty()) {
                stmt.setString(index++, "%" + pname + "%");
            }
            if (category != null && !category.isEmpty()) {
                stmt.setString(index++, category);
            }
            if (vegetarian != null) {
                stmt.setBoolean(index, vegetarian);
            }

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Pizza pizza = new Pizza();
                pizza.setPname(resultSet.getString("pname"));
                pizza.setCategoryname(resultSet.getString("categoryname"));
                pizza.setVegetarian(resultSet.getBoolean("vegetarian"));
                pizzas.add(pizza);
            }
        }

        return pizzas;
    }


    public static List<Category> getCategories(String cname, Double price) throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category WHERE 1=1";

        if (cname != null && !cname.isEmpty()) {
            query += " AND cname LIKE ?";
        }
        if (price != null) {
            query += " AND price = ?";
        }

        try (Connection connection = connect(); PreparedStatement stmt = connection.prepareStatement(query)) {
            int index = 1;
            if (cname != null && !cname.isEmpty()) {
                stmt.setString(index++, "%" + cname + "%");
            }
            if (price != null) {
                stmt.setDouble(index++, price);
            }

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Category category = new Category();
                category.setCname(resultSet.getString("cname"));
                category.setPrice(resultSet.getDouble("price"));
                categories.add(category);
            }
        }

        return categories;
    }


    public static List<Orders> getOrders(String pizzaname, Integer amount, Date taken, Date dispatched) throws SQLException {
        List<Orders> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE 1=1";

        if (pizzaname != null && !pizzaname.isEmpty()) {
            query += " AND pizzaname LIKE ?";
        }
        if (amount != null) {
            query += " AND amount = ?";
        }
        if (taken != null) {
            query += " AND taken >= ?";
        }
        if (dispatched != null) {
            query += " AND dispatched <= ?";
        }

        try (Connection connection = connect(); PreparedStatement stmt = connection.prepareStatement(query)) {
            int index = 1;
            if (pizzaname != null && !pizzaname.isEmpty()) {
                stmt.setString(index++, "%" + pizzaname + "%");
            }
            if (amount != null) {
                stmt.setInt(index++, amount);
            }
            if (taken != null) {
                stmt.setDate(index++, taken);
            }
            if (dispatched != null) {
                stmt.setDate(index++, dispatched);
            }

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Orders order = new Orders();
                order.setId(resultSet.getInt("id"));
                order.setPizzaname(resultSet.getString("pizzaname"));
                order.setAmount(resultSet.getInt("amount"));
                order.setTaken(resultSet.getTimestamp("taken").toLocalDateTime());
                order.setDispatched(resultSet.getTimestamp("dispatched").toLocalDateTime());
                orders.add(order);
            }
        }

        return orders;
    }

}
