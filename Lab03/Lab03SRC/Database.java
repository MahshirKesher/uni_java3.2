package Lab03SRC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static String dbUser = "";
    public static String dbPass = "";
    // Updated to use MySQL protocol and default port 3306
    private static final String URL = "jdbc:mysql://localhost:3306/Lab03_DB"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, dbUser, dbPass);
    }
}
