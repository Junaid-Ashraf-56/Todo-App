import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

class DatabaseSetup {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:users.db")) {
            // Drop the existing table if it exists
            String dropTable = "DROP TABLE IF EXISTS users";
            Statement stmt = conn.createStatement();
            stmt.execute(dropTable);

            // Create the new table with the updated schema
            String createTable = "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL)";
            stmt.execute(createTable);

            System.out.println("Database setup complete!");
        } catch (Exception e) {
            System.err.println("Database setup error: " + e.getMessage());
        }
    }
}
