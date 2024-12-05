package com.dija.lecturehomework;


import java.sql.*;

public class DatabaseHelper {

    private static final String URL = "jdbc:sqlite:waste.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
