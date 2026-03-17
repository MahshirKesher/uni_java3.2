import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Lab02 
{
    public static void main(String[] args) 
    {
        // The URL points to your specific database
        String url = "jdbc:mysql://localhost:3306/app_db";
        String user = "Lab02_user";
        String password = "Bruh1488Why_";

        // The try-with-resources block ensures the connection is closed automatically
        try (Connection conn = DriverManager.getConnection(url, user, password)) 
        {
            System.out.println("Success! Java is connected to MySQL.");
        } 
        catch (SQLException e) 
        {
            System.out.println("Connection failed. Check your URL, username, or password.");
            e.printStackTrace();
        }
    }
}
