import java.sql.Connection;

public class EmployeeWorkflow {
    private Connection connection;

    public EmployeeWorkflow(Connection connection) {
        this.connection = connection;
    }

    public void processEmployeeWorkflow() {
        System.out.println("You selected Employee role");
        // Add employee-specific tasks here
    }
}
