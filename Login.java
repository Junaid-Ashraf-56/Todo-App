package TodoApp;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    private final JFrame frame;
    private static final String DB_URL = "jdbc:sqlite:todo.db";

    public Login() {
        // Create a frame
        frame = new JFrame("Login Page");
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

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 100, 100, 30);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField();
        passwordText.setBounds(200, 100, 200, 30);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(200, 200, 120, 30);
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(100, 250, 120, 30);
        panel.add(registerButton);

        JButton forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.setBounds(300, 250, 150, 30);
        panel.add(forgotPasswordButton);

        JLabel messageLabel = new JLabel("");
        messageLabel.setBounds(100, 150, 400, 30);
        messageLabel.setForeground(Color.RED);
        panel.add(messageLabel);

        // Add action listener to the login button
        loginButton.addActionListener(e -> {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());

            if (authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                frame.dispose();
                SwingUtilities.invokeLater(TodoApp::new); // Navigate to Todo application
            } else {
                messageLabel.setText("Invalid username or password. Try again.");
            }
        });

        // Add action listener to the register button
        registerButton.addActionListener(e -> {
            frame.dispose();
            SwingUtilities.invokeLater(SignUp::new);
        });

        // Add action listener to the forgot password button
        forgotPasswordButton.addActionListener(e -> {
            // Implement forgot password functionality here
            JOptionPane.showMessageDialog(frame, "Forgot Password functionality not implemented yet.");
        });

        // Add the panel to the frame
        frame.add(panel);

        // Set the frame to be visible
        frame.setVisible(true);

        // Create the users table if it does not exist
        createUsersTable();
    }

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            return false;
        }
    }

    private void createUsersTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS users ("
                             + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                             + "username TEXT NOT NULL,"
                             + "email TEXT NOT NULL,"
                             + "password TEXT NOT NULL)")) {
            stmt.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }
}
