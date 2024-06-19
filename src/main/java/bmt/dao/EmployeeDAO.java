package bmt.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import bmt.model.Employee;

public class EmployeeDAO {
    private Connection connection;
    private final Properties sqlQueries;

    public EmployeeDAO() {
        Properties dbProps = new Properties();
        sqlQueries = new Properties();

        try {
            // Load database properties
            InputStream dbPropsStream = getClass().getClassLoader().getResourceAsStream("db.properties");
            if (dbPropsStream != null) {
                dbProps.load(dbPropsStream);
                dbPropsStream.close();
            }

            // Load SQL queries
            InputStream sqlQueriesStream = getClass().getClassLoader().getResourceAsStream("sql-queries.properties");
            if (sqlQueriesStream != null) {
                sqlQueries.load(sqlQueriesStream);
                sqlQueriesStream.close();
            }

            // Establish the connection
            String url = dbProps.getProperty("db.url");
            String user = dbProps.getProperty("db.user");
            String password = dbProps.getProperty("db.password");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully!");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<Employee> fetchAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = sqlQueries.getProperty("fetchAllEmployees");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String position = rs.getString("position");

                Employee employee = new Employee(id, name, position);
                employees.add(employee);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as per your application's error handling strategy
        }

        return employees;
    }

    public void insertEmployee(Employee employee) {
        String query = sqlQueries.getProperty("insertEmployee");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPosition());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Employee inserted successfully.");

                // Retrieve the auto-generated id if needed
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        employee.setId(generatedId);
                    } else {
                        throw new SQLException("Failed to retrieve auto-generated ID.");
                    }
                }
            } else {
                throw new SQLException("Failed to insert employee, no rows affected.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as per your application's error handling strategy
        }
    }

    public void updateEmployee(Employee employee) {
        String query = sqlQueries.getProperty("updateEmployee");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPosition());
            stmt.setInt(3, employee.getId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                throw new SQLException("Failed to update employee, no rows affected.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as per your application's error handling strategy
        }
    }

    public void deleteEmployee(int id) {
        String query = sqlQueries.getProperty("deleteEmployee");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                throw new SQLException("Failed to delete employee, no rows affected.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as per your application's error handling strategy
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}