import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.sql.*;
import java.util.Scanner;
import java.io.Console;

public class Lab02App extends JFrame 
{
    private String url;
    private String dbUser;
    private String dbPassword;

    public Lab02App(String url, String dbUser, String dbPassword) 
    {
        this.url = url;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;

        setTitle("Employee Database Viewer (Editable)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"id", "name", "department", "salary"};
        
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return column != 0;
            }
        };
        
        JTable table = new JTable(tableModel);

        try (Connection conn = DriverManager.getConnection(this.url, this.dbUser, this.dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) 
        {
            while (rs.next()) 
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String department = rs.getString("department");
                double salary = rs.getDouble("salary");
                tableModel.addRow(new Object[]{id, name, department, salary});
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load data.");
        }

        tableModel.addTableModelListener(new TableModelListener() 
        {
            @Override
            public void tableChanged(TableModelEvent e) 
            {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() != -1) 
                {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    
                    Object newValue = tableModel.getValueAt(row, column);
                    int id = (int) tableModel.getValueAt(row, 0); 
                    String columnName = tableModel.getColumnName(column);

                    updateDatabase(id, columnName, newValue);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateDatabase(int id, String columnName, Object newValue) 
    {
        String sql = "UPDATE employees SET " + columnName + " = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            
            if (columnName.equals("salary")) 
            {
                pstmt.setDouble(1, Double.parseDouble(newValue.toString()));
            } 
            else 
            {
                pstmt.setString(1, newValue.toString());
            }
            
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Successfully updated ID " + id + " in database.");
            
        } 
        catch (SQLException | NumberFormatException ex) 
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update database! Ensure salary is a number.", 
                                          "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter MySQL username: ");
        String user = scanner.nextLine();
        
        Console console = System.console();
        String password;
        
        if (console != null) 
        {
            char[] passwordArray = console.readPassword("Enter MySQL password: ");
            password = new String(passwordArray);
        } 
        else 
        {
            System.out.print("Enter MySQL password: ");
            password = scanner.nextLine();
        }

        String url = "jdbc:mysql://localhost:3306/app_db";

        System.out.println("Attempting to connect...");

        try (Connection conn = DriverManager.getConnection(url, user, password)) 
        {
            System.out.println("Success! Java is connected to MySQL as '" + user + "'.");
            
            SwingUtilities.invokeLater(() -> 
            {
                new Lab02App(url, user, password).setVisible(true);
            });
        } 
        catch (SQLException e) 
        {
            System.out.println("Connection failed. Access denied for user '" + user + "'.");
        } 
        finally 
        {
            scanner.close();
        }
    }
}
