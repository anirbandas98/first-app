package bmt.dao;

import bmt.model.Employee;
import bmt.util.DBUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmployeeDAO {
    private final Properties queries;

    public EmployeeDAO() {
        this.queries = loadQueries();
    }

    private Properties loadQueries() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("queries.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("queries.properties file not found in classpath");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as per your application's error handling strategy
        }
        return properties;
    }

    public void insertEmployee(Employee employee) {
        String sql = queries.getProperty("sql.insertEmployee");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPosition());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Employee inserted successfully.");
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        employee.setId(generatedId);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as per your application's error handling strategy
        }
    }

    public List<Employee> fetchAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = queries.getProperty("sql.selectAllEmployees");

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

    public void updateEmployee(Employee employee) {
        String sql = queries.getProperty("sql.updateEmployee");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPosition());
            stmt.setInt(3, employee.getId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Employee updated successfully.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as per your application's error handling strategy
        }
    }

    public void deleteEmployee(int employeeId) {
        String sql = queries.getProperty("sql.deleteEmployee");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as per your application's error handling strategy
        }
    }

    public void closeConnection() {
        // Implement if needed to close database connection
        DBUtil.closeConnection();
    }
}
