import java.math.BigDecimal;
import java.sql.*;

public class PayslipGenerator {
    private final Connection connection;

    public PayslipGenerator(Connection connection) {
        this.connection = connection;
    }

    public void generatePayslip(int employeeId) {
        Statement stmt = null;
    
        try {
            System.out.println("Creating statement...");
            stmt = connection.createStatement();
    
            String sql = "SELECT e.First_Name, e.Last_Name, e.Email_Address, e.Department_Designation, s.*, " +
                         "ss.Basic_Pay, ss.HRA, ss.Conveyance_Allowances, ss.Special_Allowances, ss.Other_Allowances " +
                         "FROM Employee_Details e " +
                         "INNER JOIN Salary_Details s ON e.Employee_Id = s.Employee_Id " +
                         "INNER JOIN Salary_Structure ss ON e.Department_Designation = ss.Department_Designation " +
                         "WHERE e.Employee_Id = " + employeeId;
            
            ResultSet rs = stmt.executeQuery(sql);
    
            while (rs.next()) {
                String firstName = rs.getString("First_Name");
                String lastName = rs.getString("Last_Name");
                String designationDepartment = rs.getString("Department_Designation");
                // Retrieve salary details
                int month = rs.getInt("Month");
                int year = rs.getInt("Year");
                BigDecimal basicPay = rs.getBigDecimal("Basic_Pay");
                BigDecimal hra = rs.getBigDecimal("HRA");
                BigDecimal conveyanceAllowances = rs.getBigDecimal("Conveyance_Allowances");
                BigDecimal specialAllowances = rs.getBigDecimal("Special_Allowances");
                BigDecimal otherAllowances = rs.getBigDecimal("Other_Allowances");
                BigDecimal deductions = rs.getBigDecimal("Deductions"); // Uncommented
                BigDecimal netSalary = rs.getBigDecimal("Net_Salary"); // Uncommented
    
                // Retrieve other salary details similarly
    
                System.out.println("Employee Name: " + firstName + " " + lastName);
                System.out.println("Designation/Department: " + designationDepartment);
                System.out.println("Month: " + month + ", Year: " + year);
                System.out.println("Basic Pay: " + basicPay);
                System.out.println("HRA: " + hra);
                System.out.println("Conveyance Allowances: " + conveyanceAllowances);
                System.out.println("Special Allowances: " + specialAllowances);
                System.out.println("Other Allowances: " + otherAllowances);
                System.out.println("Deductions: " + deductions); // Uncommented
                System.out.println("Net Salary: " + netSalary); // Uncommented
    
                // Construct email and send
                EmailSender emailSender = new EmailSender("sriharishr105@gmail.com", "czwympvajwawowbh", "smtp.gmail.com", 587);
                String toEmail = rs.getString("Email_Address");
                String subject = "Salary Details";
                String messageText = "Conveyance Allowances: " + conveyanceAllowances + "\n" +
                        "Special Allowances: " + specialAllowances + "\n" +
                        "Other Allowances: " + otherAllowances + "\n" +
                        "Deductions: " + deductions + "\n" +
                        "Net Salary: " + netSalary;
                emailSender.sendEmail(toEmail, subject, messageText);
            }
            rs.close();
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
    }
    
}
