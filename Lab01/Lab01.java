import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Lab01 extends JFrame 
{
    private JTextField txtLat1, txtLon1, txtLat2, txtLon2, txtR, txtResult;
    private JButton btnSolve, btnClear; // Оголошення компонентів GUI

    public Lab01() 
    {
        setTitle("Обчислення відстані (Формула Гаверсинуса)");
        setSize(400, 300); // Налаштування головного вікна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Центрування вікна
        setLayout(new GridLayout(7, 2, 10, 10)); // Сітка 7 рядків, 2 стовпці

        // Ініціалізація міток (JLabels) та полів введення (JTextFields)
        add(new JLabel("Широта 1 (lat1, град):"));
        txtLat1 = new JTextField();
        add(txtLat1);

        add(new JLabel("Довгота 1 (lon1, град):"));
        txtLon1 = new JTextField();
        add(txtLon1);

        add(new JLabel("Широта 2 (lat2, град):"));
        txtLat2 = new JTextField();
        add(txtLat2);

        add(new JLabel("Довгота 2 (lon2, град):"));
        txtLon2 = new JTextField();
        add(txtLon2);

        add(new JLabel("Радіус Землі R (м):"));
        txtR = new JTextField("6371000"); // Задаємо значення за замовчуванням (6371 * 10^3 метрів)
        add(txtR);

        add(new JLabel("Відстань D (м):"));
        txtResult = new JTextField();
        txtResult.setEditable(false); // Поле лише для читання результату
        add(txtResult);

        // Ініціалізація кнопок (JButtons)
        btnSolve = new JButton("Solve");
        btnClear = new JButton("Clear");
        add(btnSolve);
        add(btnClear);

        // Обробник подій для кнопки "Solve"
        btnSolve.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                calculateDistance();
            }
        });

        // Обробник подій для кнопки "Clear"
        btnClear.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                clearFields();
            }
        });
    }

    // Метод для обчислення відстані за формулою
    private void calculateDistance() 
    {
        try 
        {
            // Зчитування даних з полів
            double lat1 = Double.parseDouble(txtLat1.getText());
            double lon1 = Double.parseDouble(txtLon1.getText());
            double lat2 = Double.parseDouble(txtLat2.getText());
            double lon2 = Double.parseDouble(txtLon2.getText());
            double R = Double.parseDouble(txtR.getText());

            // Переведення в радіани та обчислення різниць
            double phi1 = lat1 * (Math.PI / 180.0);
            double phi2 = lat2 * (Math.PI / 180.0);
            double deltaPhi = (lat2 - lat1) * (Math.PI / 180.0);
            double deltaLambda = (lon2 - lon1) * (Math.PI / 180.0);

            // Обчислення a
            double sinSqDeltaPhi = Math.pow(Math.sin(deltaPhi / 2.0), 2);
            double sinSqDeltaLambda = Math.pow(Math.sin(deltaLambda / 2.0), 2);
            double a = sinSqDeltaPhi + Math.cos(phi1) * Math.cos(phi2) * sinSqDeltaLambda;

            // Обчислення c
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            // Підсумкова відстань D
            double D = R * c;

            // Виведення результату
            txtResult.setText(String.format("%.2f", D));

        } 
        catch (NumberFormatException ex) 
        {
            JOptionPane.showMessageDialog(this, 
                "Будь ласка, введіть коректні числові значення!", 
                "Помилка вводу", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для очищення полів
    private void clearFields() 
    {
        txtLat1.setText("");
        txtLon1.setText("");
        txtLat2.setText("");
        txtLon2.setText("");
        txtR.setText("6371000"); // Повертаємо радіус за замовчуванням
        txtResult.setText("");
    }

    // Головний метод для запуску програми
    public static void main(String[] args) 
    {
        // Забезпечення виконання GUI в Event Dispatch Thread (рекомендовано для Swing)
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new Lab01().setVisible(true);
            }
        });
    }
}
