import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.Console;

public class DatabaseTest 
{
    public static void main(String[] args) 
    {
        // 1. Set up the Scanner for standard text input
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter MySQL username: ");
        String user = scanner.nextLine();
        
        // 2. Set up the Console for secure password input
        Console console = System.console();
        String password;
        
        if (console != null) 
        {
            // This hides the typing on the terminal
            char[] passwordArray = console.readPassword("Enter MySQL password: ");
            password = new String(passwordArray);
        } 
        else 
        {
            // Fallback just in case the terminal environment doesn't support Console
            System.out.print("Enter MySQL password: ");
            password = scanner.nextLine();
        }

        // The database URL remains hardcoded since it rarely changes
        String url = "jdbc:mysql://localhost:3306/app_db";

        System.out.println("Attempting to connect...");

        // 3. Pass the user-provided variables into the connection attempt
        try (Connection conn = DriverManager.getConnection(url, user, password)) 
        {
            System.out.println("Success! Java is connected to MySQL as '" + user + "'.");
        } 
        catch (SQLException e) 
        {
            System.out.println("Connection failed. Access denied for user '" + user + "'.");
        } 
        finally 
        {
            // Always close your scanner to prevent resource leaks
            scanner.close();
        }
    }
}
