package TodoApp;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Run the login page first
        SwingUtilities.invokeLater(() -> new Login());
    }
}