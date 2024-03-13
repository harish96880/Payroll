import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MyPayslipGenerator {
    private final Connection connection;
    private final EmailSender emailSender;

    public MyPayslipGenerator(Connection connection, EmailSender emailSender) {
        this.connection = connection;
        this.emailSender = emailSender;
    }

    public void generatePayslip(int employeeId, int month) {
        Statement stmt = null;

        try {
            stmt = connection.createStatement();
            String sqlV2 = "SELECT " +
            "ED.Employee_Id, " +
            "ED.First_Name, " +
            "ED.Last_Name, " +
            "ED.Department_Designation, " +
            "ED.Email_Address, " +
            "SD.Month, " +
            "SD.Year, " +
            "SD.Basic_Pay, " +
            "SD.HRA, " +
            "SD.Conveyance_Allowances, " +
            "SD.Special_Allowances, " +
            "SD.Other_Allowances, " +
            "SD.Basic_Pay + SD.HRA + SD.Conveyance_Allowances + SD.Special_Allowances + SD.Other_Allowances + (nsa.no_of_days * (SD.Basic_Pay / 60)) AS Total_Addition, " +
            "SS.PF, " +
            "SS.ESI, " +
            "SS.Professional_Tax, " +
            "SS.TDS_TCS, " +
            "nsa.no_of_days * (SD.Basic_Pay / 60) AS Night_Shift_Allowance, " +
            "SS.PF + SS.ESI + SS.Professional_Tax + SS.TDS_TCS + (LR.loss_of_pay * (SD.Basic_Pay / 30)) AS Total_Deduction, " +
            "LR.loss_of_pay * (SD.Basic_Pay / 30) AS Leave_Deduction, " +
            "ROUND((SD.Basic_Pay + SD.HRA + SD.Conveyance_Allowances + SD.Special_Allowances + SD.Other_Allowances) - " +
            "(SS.PF + SS.ESI + SS.Professional_Tax + SS.TDS_TCS + SS.Night_Shift_Allowance) - LR.loss_of_pay * (SD.Basic_Pay / 30) + (nsa.no_of_days * (SD.Basic_Pay / 60)), 2) AS Net_Salary " +
            "FROM " +
            "Employee_Details AS ED " +
            "JOIN " +
            "Payslip AS LR ON ED.Employee_Id = LR.Employee_Id " +
            "JOIN " +
            "Salary_Details AS SD ON ED.Employee_Id = SD.Employee_Id " +
            "JOIN " +
            "Salary_Structure AS SS ON ED.Department_Designation = SS.Department_Designation " +
            "JOIN " +
            "night_shift_allowance AS nsa ON ED.Employee_Id = nsa.Employee_Id " +
            "WHERE " +
            "ED.Employee_Id = " + employeeId +  " AND " +
            "SD.Month = " + month +
            " LIMIT 1;";
            ResultSet rs = stmt.executeQuery(sqlV2);

            String employeeName = null;
            String designation = null;
            String email = null;
            int salaryMonth = 0;
            int year = 0;
            BigDecimal basicPay = null;
            BigDecimal hra = null;
            BigDecimal conveyance = null;
            BigDecimal totalAddition = null;
            BigDecimal pf = null;
            BigDecimal esi = null;
            BigDecimal professionTax = null;
            BigDecimal tsdIt = null;
            BigDecimal totalDeduction = null;
            BigDecimal netSalary = null;

            while (rs.next()) {
                employeeName = rs.getString("First_Name") + rs.getString("Last_Name");
                designation = rs.getString("Department_Designation");
                salaryMonth = rs.getInt("Month");
                year = rs.getInt("Year");
                basicPay = rs.getBigDecimal("Basic_Pay");
                email = rs.getString("Email_Address");
                hra = rs.getBigDecimal("HRA");
                conveyance = rs.getBigDecimal("Conveyance_Allowances").add(rs.getBigDecimal("Special_Allowances")).add(rs.getBigDecimal("Other_Allowances"));
                totalAddition = rs.getBigDecimal("Total_Addition");
                pf = rs.getBigDecimal("PF");
                esi = rs.getBigDecimal("ESI");
                professionTax = rs.getBigDecimal("Professional_Tax");
                tsdIt = rs.getBigDecimal("TDS_TCS");
                totalDeduction = rs.getBigDecimal("Total_Deduction");
                netSalary = rs.getBigDecimal("Net_Salary");
            }

            // Create MimeMessage object
            MimeMessage message = new MimeMessage(emailSender.getSession());

            // Construct message content
            String messageContent = "<html>"
                    + "<head>"
                    + "<style>"
                    + "table {"
                    + " width: 100%;"
                    + " border-collapse: collapse;"
                    + "}"
                    + "table, th, td {"
                    + " border: 1px solid black;"
                    + " padding: 8px;"
                    + "}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<h2>Solartis LLC</h2>"
                    + "<p>Coimbatore</p>"
                    + "<p>Salary Slip</p>"
                    + "<table>"
                    + "<tr><td>Employee Name:</td><td>" + employeeName + "</td></tr>"
                    + "<tr><td>Designation:</td><td>" + designation + "</td></tr>"
                    + "<tr><td>Month & Year:</td><td>" + salaryMonth + " " + year + "</td></tr>"
                    + "<tr><td colspan=\"3\">----------------------------------------------------------------------------------------</td></tr>"
                    + "<tr><td colspan=\"3\"><b>Earnings:-</b></td></tr>"
                    + "<tr><td>Basic & DA:</td><td>" + basicPay + "</td></tr>"
                    + "<tr><td>HRA:</td><td>" + hra + "</td></tr>"
                    + "<tr><td>Conveyance:</td><td>" + conveyance + "</td></tr>"
                    + "<tr><td>Total Addition:</td><td>" + totalAddition + "</td></tr>"
                    + "<tr><td colspan=\"3\"><b>Deductions:-</b></td></tr>"
                    + "<tr><td>Provident Fund:</td><td>" + pf + "</td></tr>"
                    + "<tr><td>E.S.I:</td><td>" + esi + "</td></tr>"
                    + "<tr><td>Loan:</td><td>-</td></tr>"
                    + "<tr><td>Profession Tax:</td><td>" + professionTax + "</td></tr>"
                    + "<tr><td>TSD/IT:</td><td>" + tsdIt + "</td></tr>"
                    + "<tr><td>Total Deduction:</td><td>" + totalDeduction + "</td></tr>"
                    + "<tr><td colspan=\"3\"><b>NET SALARY:</b></td></tr>"
                    + "<tr><td colspan=\"2\">" + netSalary + "</td></tr>"
                    + "</table>"
                    + "</body>"
                    + "</html>";

            // Set message content
            message.setContent(messageContent, "text/html");

            // Set recipient and subject
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Salary Details");

            // Send the email
            emailSender.sendEmail(email, "Salary Details", message);

            rs.close();
            stmt.close();
        } catch (SQLException | MessagingException se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
    }
}
