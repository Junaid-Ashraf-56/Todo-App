package TodoApp;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SignUp {
    private final JFrame frame;
    private static final String DB_URL = "jdbc:sqlite:todo.db";

    public SignUp() {
        // Create a frame
        frame = new JFrame("Sign Up Page");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create a panel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(null);

        ImageIcon image = new ImageIcon("logo.png");
        frame.setIconImage(image.getImage());

        // Add components to the panel
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(100, 50, 100, 30);
        panel.add(usernameLabel);

        JTextField usernameText = new JTextField();
        usernameText.setBounds(200, 50, 200, 30);
        panel.add(usernameText);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(100, 100, 100, 30);
        panel.add(emailLabel);

        JTextField emailText = new JTextField();
        emailText.setBounds(200, 100, 200, 30);
        panel.add(emailText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 150, 100, 30);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField();
        passwordText.setBounds(200, 150, 200, 30);
        panel.add(passwordText);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(200, 250, 120, 30);
        panel.add(registerButton);

        JLabel messageLabel = new JLabel("");
        messageLabel.setBounds(100, 200, 400, 30);
        messageLabel.setForeground(Color.RED);
        panel.add(messageLabel);

        // Add action listener to the register button
        registerButton.addActionListener(e -> {
            String username = usernameText.getText();
            String email = emailText.getText();
            String password = new String(passwordText.getPassword());

            if (registerUser(username, email, password)) {
                JOptionPane.showMessageDialog(frame, "Registration successful!");
                frame.dispose();
                SwingUtilities.invokeLater(Login::new);
            } else {
                messageLabel.setText("Registration failed. Try again.");
            }
        });

        // Add the panel to the frame
        frame.add(panel);

        // Set the frame to be visible
        frame.setVisible(true);

        // Create the users table if it does not exist
        createUsersTable();
    }

    private boolean registerUser(String username, String email, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.executeUpdate();

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            return false;
        }
    }

    private void createUsersTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username TEXT NOT NULL,"
                    + "email TEXT NOT NULL,"
                    + "password TEXT NOT NULL)";
            stmt.execute(createTableQuery);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }
}
