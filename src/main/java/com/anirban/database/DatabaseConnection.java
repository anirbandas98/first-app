package com.anirban.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            // Load properties file
            props.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties"));

            // Get properties
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            // Establish the connection
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully!");

            // Close the connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
