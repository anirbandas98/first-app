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

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String position = resultSet.getString("position");
                employees.add(new Employee(id, name, position));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public void insertEmployee(Employee employee) {
        String query = sqlQueries.getProperty("insertEmployee");

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getPosition());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(Employee employee) {
        String query = sqlQueries.getProperty("updateEmployee");

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getPosition());
            preparedStatement.setInt(3, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int id) {
        String query = sqlQueries.getProperty("deleteEmployee");

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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