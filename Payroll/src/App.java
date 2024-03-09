import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // URL For connecting the database
        String mysqlJDBCUrl = "jdbc:mysql://localhost:3306/payroll";

        // User Credentials for database
        String userName = "root";
        String passWord = "root";

        Connection connection = null;
        Scanner scan = new Scanner(System.in);

        try {
            connection = DriverManager.getConnection(mysqlJDBCUrl, userName, passWord);

            if (connection != null) {
                // Display welcome message
                System.out.println("==========================================");
                System.out.println("Welcome to the Solartis Payroll System !!!");
                System.out.println("==========================================");

                // Prompt user to choose role
                System.out.println("Choose your role: \n1.Admin \n2.Employee \n3.Exit");
                System.out.print("Enter your choice: ");
                int choiceForRole = scan.nextInt();

                // Process user's role selection
                switch (choiceForRole) {
                    case 1:
                        // Workflow for Admin
                        AdminWorkflow adminWorkflow = new AdminWorkflow(connection);
                        adminWorkflow.processAdminWorkflow();
                        break;
                    case 2:
                        // Workflow for Employee
                        EmployeeWorkflow employeeWorkflow = new EmployeeWorkflow(connection);
                        employeeWorkflow.processEmployeeWorkflow();
                        break;
                    case 3:
                        System.out.println("Shutting down.......");
                        System.out.println("Bye !!!");
                        break;
                    default:
                        System.out.println("Invalid Choice. Please select either 1 or 2");
                        break;
                }
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }
}
