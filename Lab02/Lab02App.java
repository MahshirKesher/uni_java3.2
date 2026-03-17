import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.sql.*;

public class Lab02App extends JFrame 
{
    // Database credentials
    String url = "jdbc:mysql://localhost:3306/app_db";
    String dbUser = "Lab02_user";
    String dbPassword = "Bruh1488Why_";

    public Lab02App() 
    {
        setTitle("Employee Database Viewer (Editable)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"id", "name", "department", "salary"};
        
        // 1. Create a custom TableModel that makes the ID column read-only
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return column != 0; // Column 0 (ID) cannot be edited
            }
        };
        
        JTable table = new JTable(tableModel);

        // 2. Fetch Initial Data
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
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

        // 3. Add the Listener to catch user edits AFTER initial data is loaded
        tableModel.addTableModelListener(new TableModelListener() 
        {
            @Override
            public void tableChanged(TableModelEvent e) 
            {
                // Only trigger if a specific cell was updated
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() != -1) 
                {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    
                    // Get the new value the user typed, the ID of the row, and the column name
                    Object newValue = tableModel.getValueAt(row, column);
                    int id = (int) tableModel.getValueAt(row, 0); 
                    String columnName = tableModel.getColumnName(column);

                    // Send the update to MySQL
                    updateDatabase(id, columnName, newValue);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    // 4. Method to handle the background SQL UPDATE
    private void updateDatabase(int id, String columnName, Object newValue) 
    {
        // We use a PreparedStatement to safely inject the new value into the SQL query
        String sql = "UPDATE employees SET " + columnName + " = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
             {
            
            // Java usually pulls table edits as Strings, so we handle the salary conversion
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
        SwingUtilities.invokeLater(() -> 
        {
            new Lab02App().setVisible(true);
        });
    }
}
