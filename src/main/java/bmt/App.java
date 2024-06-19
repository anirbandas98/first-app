package bmt;

import java.util.List;
import bmt.dao.EmployeeDAO;
import bmt.model.Employee;

public class App {
    public static void main(String[] args) {
        // Create an instance of EmployeeDAO
        EmployeeDAO employeeDAO = new EmployeeDAO();

        // Insert a new employee
        Employee newEmployee = new Employee("Alice Johnson", "Engineer");
        employeeDAO.insertEmployee(newEmployee);

        // Fetch all employees
        List<Employee> employees = employeeDAO.fetchAllEmployees();
        for (Employee employee : employees) {
            System.out.println(employee);
        }

        // Update an existing employee (assuming there are employees in the list)
        if (!employees.isEmpty()) {
            Employee employeeToUpdate = employees.get(0);
            employeeToUpdate.setName("Updated Name");
            employeeToUpdate.setPosition("Updated Position");
            employeeDAO.updateEmployee(employeeToUpdate);
        }

        // Delete an employee by ID (assuming there are employees in the list)
        if (!employees.isEmpty()) {
            int employeeIdToDelete = employees.get(0).getId();
            employeeDAO.deleteEmployee(employeeIdToDelete);
        }

        // Fetch all employees after update and delete
        List<Employee> remainingEmployees = employeeDAO.fetchAllEmployees();
        System.out.println("Employees after update and delete:");
        for (Employee employee : remainingEmployees) {
            System.out.println(employee);
        }

        // Close the database connection
        employeeDAO.closeConnection();
    }
}