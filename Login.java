package TodoApp;


import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Login {
    private final JFrame frame;

    public Login() {
        // Create a frame
        frame = new JFrame("Login Page");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create a panel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(null);

        ImageIcon image = new ImageIcon("logo.png");
        frame.setIconImage(image.getImage());
        // Add components to the panel
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(100, 50, 100, 30);
        panel.add(emailLabel);

        JTextField emailText = new JTextField();
        emailText.setBounds(200, 50, 200, 30);
        panel.add(emailText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 100, 100, 30);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField();
        passwordText.setBounds(200, 100, 200, 30);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(120, 200, 120, 30);
        panel.add(loginButton);

        JButton forgotButton = new JButton("Forgot Password");
        forgotButton.setBounds(250, 200, 150, 30);
        panel.add(forgotButton);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(220, 250, 100, 30);
        panel.add(signUpButton);

        JLabel messageLabel = new JLabel("");
        messageLabel.setBounds(50, 150, 400, 30);
        messageLabel.setForeground(Color.RED);
        panel.add(messageLabel);

        // Add action listener to the login button
        loginButton.addActionListener(e -> {
            String email = emailText.getText();
            String password = new String(passwordText.getPassword());

            if (validateLogin(email, password)) {
                messageLabel.setText("Login successful!");
                messageLabel.setForeground(Color.GREEN);

                // Close the login page and start the main program
                frame.dispose();
                SwingUtilities.invokeLater(TodoApp::new);
            } else {
                messageLabel.setText("Invalid email or password.");
            }
        });

        // Add action listener to the forgot button
        forgotButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Forgot password functionality is under construction!"));

        // Add action listener to the sign-up button
        signUpButton.addActionListener(e -> {
            frame.dispose();
            SwingUtilities.invokeLater(() -> new SignUp());
        });

        // Add the panel to the frame
        frame.add(panel);

        // Set the frame to be visible
        frame.setVisible(true);
    }

    private boolean validateLogin(String email, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:users.db")) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // If a record is found, the login is valid
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            return false;
        }
    }
}
