package TodoApp;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Load the JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Establish a connection to the database
        String url = "jdbc:sqlite:/path/to/your/database.db"; // Update the path to your SQLite database file

        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Connection established successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Run the login page first
        SwingUtilities.invokeLater(() -> new Login());
    }
}