package bmt;

import bmt.dao.EmployeeDAO;
import bmt.model.Employee;

import java.util.List;

public class App {
    public static void main(String[] args) {
        EmployeeDAO employeeDAO = new EmployeeDAO();

        // Insert a new employee
        Employee newEmployee = new Employee("Alice Johnson", "Engineer");
        employeeDAO.insertEmployee(newEmployee);

        // Fetch all employees
        List<Employee> employees = employeeDAO.fetchAllEmployees();
        for (Employee employee : employees) {
            System.out.println(employee);
        }

        // Update an existing employee
        if (!employees.isEmpty()) {
            Employee employeeToUpdate = employees.get(0);
            employeeToUpdate.setName("Updated Name");
            employeeToUpdate.setPosition("Updated Position");
            employeeDAO.updateEmployee(employeeToUpdate);
        }

        // Delete an employee by ID
        if (!employees.isEmpty()) {
            int employeeIdToDelete = employees.get(0).getId();
            employeeDAO.deleteEmployee(employeeIdToDelete);
        }

        // Close the database connection
        employeeDAO.closeConnection();
    }
}
