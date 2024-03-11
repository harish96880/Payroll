import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class AdminWorkflow {
    private final Connection connection;

    public AdminWorkflow(Connection connection) {
        this.connection = connection;
    }

    public void processAdminWorkflow() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("You selected Admin role");
            int adminSecretKey = 12345;

            System.out.print("\nEnter the secret key for admin: ");
            int adminSecretKeyInput = scanner.nextInt();

            if (adminSecretKeyInput == adminSecretKey) {
                System.out.println("Access approved");
                displayAdminMenu(scanner);
            } else {
                System.out.println("Incorrect admin secret key. Access denied");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayAdminMenu(Scanner scanner) throws SQLException {
        boolean isValidChoice = false;
        while (!isValidChoice) {
            try {
                System.out.println("\nChoose an operation to proceed:");
                System.out.println("1. Database Operations");
                System.out.println("2. Generate Payslip");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
    
                int adminPortalChoose = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                switch (adminPortalChoose) {
                    case 1:
                        handleDatabaseOperations(scanner);
                        break;
                    case 2:
                        PayslipGenerator pGenerator = new PayslipGenerator(connection);
                        pGenerator.generatePayslip(1001);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        isValidChoice = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer choice.");
                scanner.nextLine(); // Consume invalid input to prevent infinite loop
            } catch (NoSuchElementException e) {
                System.out.println("No input detected. Please try again.");
                // scanner.nextLine(); // Consume invalid input to prevent infinite loop
                isValidChoice = true;
            }
        }
    }
    

    private void handleDatabaseOperations(Scanner scanner) throws SQLException {

        boolean continueMenu = true;
    while (continueMenu) {
        System.out.println("\nChoose database to proceed:");
        System.out.println("1. Employee Details");
        System.out.println("2. Salary Details");
        System.out.println("3. Salary Structure");
        System.out.println("4. Leave Records");
        System.out.println("5. Exit");
        System.out.print("Enter your Choice: ");
        int dbChooseAdmin = scanner.nextInt();
        switch (dbChooseAdmin) {
            case 1:
                System.out.println("Enter an operation to proceed: \n1.View \n2.Update \n3.Create");
                System.out.print("Enter your choice: ");
                int adminOperationEmployeeDetails = scanner.nextInt();
                if (adminOperationEmployeeDetails == 1) {
                    viewEmployeeDetails();
                } else if (adminOperationEmployeeDetails == 2) {
                    updateEmployeeDetails();
                } else if (adminOperationEmployeeDetails == 3) {
                    createEmployeeDetails();
                }
                break;
            case 5:
                System.out.println("Exiting...");
                continueMenu = false; // Exit the menu loop
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    }

    private void viewEmployeeDetails() throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Employee_Details")) {

            while (resultSet.next()) {
                String employeeId = resultSet.getString("Employee_Id");
                String employeeFirstName = resultSet.getString("First_Name");
                String employeeLastName = resultSet.getString("Last_Name");
                String dateOfBirth = resultSet.getDate("Date_Of_Birth").toString();
                String gender = resultSet.getString("Gender");
                String phoneNumber = resultSet.getString("Phone_Number");
                String emailAddress = resultSet.getString("Email_Address");
                String address = resultSet.getString("Address");
                String joiningDate = resultSet.getString("Joining_Date");
                String departmentDesignation = resultSet.getString("Department_Designation");

                // Display employee details
                System.out.println("Employee ID: " + employeeId);
                System.out.println("First Name: " + employeeFirstName);
                System.out.println("Last Name: " + employeeLastName);
                System.out.println("Date of Birth: " + dateOfBirth);
                System.out.println("Gender: " + gender);
                System.out.println("Phone Number: " + phoneNumber);
                System.out.println("Email Address: " + emailAddress);
                System.out.println("Address: " + address);
                System.out.println("Joining Date: " + joiningDate);
                System.out.println("Department Designation: " + departmentDesignation);
                System.out.println();
            }
        }
    }

    public void updateEmployeeDetails() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the Employee ID whose details you want to update: ");
            int employeeId = scanner.nextInt();
            scanner.nextLine();
            
            // Check if the employee ID exists
            if (!isEmployeeIdExists(employeeId)) {
                System.out.println("Employee ID does not exist in the database.");
                return;
            }

            System.out.println("Enter updated First Name (leave blank to skip): ");
            String firstName = scanner.nextLine();
    
            System.out.println("Enter updated Last Name (leave blank to skip): ");
            String lastName = scanner.nextLine();
    
            System.out.println("Enter updated Date of Birth (YYYY-MM-DD, leave blank to skip): ");
            String dateOfBirth = scanner.nextLine();
    
            System.out.println("Enter updated Gender (Male/Female, leave blank to skip): ");
            String gender = scanner.nextLine();
    
            System.out.println("Enter updated Phone Number (leave blank to skip): ");
            String phoneNumber = scanner.nextLine();
    
            System.out.println("Enter updated Email Address (leave blank to skip): ");
            String emailAddress = scanner.nextLine();
    
            System.out.println("Enter updated Address (leave blank to skip): ");
            String address = scanner.nextLine();
    
            System.out.println("Enter updated Joining Date (YYYY-MM-DD, leave blank to skip): ");
            String joiningDate = scanner.nextLine();
    
            System.out.println("Enter updated Department Designation (leave blank to skip): ");
            String departmentDesignation = scanner.nextLine();
    
            // Update the employee details in the database
            String updateQuery = "UPDATE Employee_Details SET ";
            boolean isFirstField = true;
            if (!firstName.isEmpty()) {
                updateQuery += "First_Name=?, ";
                isFirstField = false;
            }
            if (!lastName.isEmpty()) {
                updateQuery += "Last_Name=?, ";
                isFirstField = false;
            }
            if (!dateOfBirth.isEmpty()) {
                updateQuery += "Date_Of_Birth=?, ";
                isFirstField = false;
            }
            if (!gender.isEmpty()) {
                updateQuery += "Date_Of_Birth=?, ";
                isFirstField = false;
            }
            if (!phoneNumber.isEmpty()) {
                updateQuery += "Date_Of_Birth=?, ";
                isFirstField = false;
            }
            if (!emailAddress.isEmpty()) {
                updateQuery += "Date_Of_Birth=?, ";
                isFirstField = false;
            }
            if (!address.isEmpty()) {
                updateQuery += "Date_Of_Birth=?, ";
                isFirstField = false;
            }
            if (!joiningDate.isEmpty()) {
                updateQuery += "Date_Of_Birth=?, ";
                isFirstField = false;
            }
            if (!departmentDesignation.isEmpty()) {
                updateQuery += "Date_Of_Birth=?, ";
                isFirstField = false;
            }

            if (!isFirstField) {
                // Remove the trailing comma and space
                updateQuery = updateQuery.substring(0, updateQuery.length() - 2);
                updateQuery += " WHERE Employee_Id=?";
                
                // Prepare the statement and set parameters
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                int parameterIndex = 1;
                if (!firstName.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, firstName);
                }
                if (!lastName.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, lastName);
                }
                if (!dateOfBirth.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, dateOfBirth);
                }
                if (!gender.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, dateOfBirth);
                }
                if (!phoneNumber.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, dateOfBirth);
                }
                if (!emailAddress.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, dateOfBirth);
                }
                if (!address.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, dateOfBirth);
                }
                if (!joiningDate.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, dateOfBirth);
                }
                if (!departmentDesignation.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, dateOfBirth);
                }
                // Set parameters for other fields similarly
    
                preparedStatement.setInt(parameterIndex, employeeId);
    
                // Execute update
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Employee details updated successfully!");
                } else {
                    System.out.println("Failed to update employee details.");
                }
            } else {
                System.out.println("No fields to update.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public void createEmployeeDetails() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter the following details for the new employee:");
    
            System.out.print("Employee ID: ");
            int employeeId = scanner.nextInt();
            scanner.nextLine();  // Consume newline character
    
            // Check if the employee ID already exists
            if (isEmployeeIdExists(employeeId)) {
                System.out.println("Employee ID already exists in the database.");
                return;
            }
    
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
    
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
    
            System.out.print("Date of Birth (YYYY-MM-DD): ");
            String dateOfBirth = scanner.nextLine();
    
            System.out.print("Gender (Male/Female): ");
            String gender = scanner.nextLine();
    
            System.out.print("Phone Number: ");
            String phoneNumber = scanner.nextLine();
    
            System.out.print("Email Address: ");
            String emailAddress = scanner.nextLine();
    
            System.out.print("Address: ");
            String address = scanner.nextLine();
    
            System.out.print("Joining Date (YYYY-MM-DD): ");
            String joiningDate = scanner.nextLine();
    
            System.out.print("Department Designation: ");
            String departmentDesignation = scanner.nextLine();
    
            // Insert new employee details into the database
            String insertQuery = "INSERT INTO Employee_Details (Employee_Id, First_Name, Last_Name, Date_Of_Birth, Gender, "
                    + "Phone_Number, Email_Address, Address, Joining_Date, Department_Designation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, dateOfBirth);
            preparedStatement.setString(5, gender);
            preparedStatement.setString(6, phoneNumber);
            preparedStatement.setString(7, emailAddress);
            preparedStatement.setString(8, address);
            preparedStatement.setString(9, joiningDate);
            preparedStatement.setString(10, departmentDesignation);
    
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Employee details added successfully!");
            } else {
                System.out.println("Failed to add employee details.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }
    
    // Method to check if the Employee ID exists in the database
    private boolean isEmployeeIdExists(int employeeId) throws SQLException {
        String query = "SELECT * FROM Employee_Details WHERE Employee_Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, employeeId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
