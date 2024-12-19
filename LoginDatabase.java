import java.sql.*;
import java.util.ArrayList;

public class LoginDatabase {
    private static final String DB_URL = "jdbc:sqlite:todo.db";

    public LoginDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Create users table if it doesn't exist
            String createUsersTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "email TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL)";
            stmt.execute(createUsersTableQuery);

            // Create tasks table with user_id column (foreign key to users) if it doesn't exist
            String createTasksTableQuery = "CREATE TABLE IF NOT EXISTS tasks (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "task TEXT NOT NULL," +
                    "due_date TEXT NOT NULL," +
                    "user_id INTEGER NOT NULL," +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";
            stmt.execute(createTasksTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a task to the database for a specific user
    public void addTaskForUser(int userId, String task, String dueDate) {
        String insertQuery = "INSERT INTO tasks (task, due_date, user_id) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, task);
            pstmt.setString(2, dueDate);
            pstmt.setInt(3, userId);  // Store the user ID with the task
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all tasks for a specific user
    public ArrayList<String> getTasksForUser(int userId) {
        ArrayList<String> tasks = new ArrayList<>();
        String selectQuery = "SELECT task, due_date FROM tasks WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            pstmt.setInt(1, userId);  // Filter by user ID
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String task = rs.getString("task");
                String dueDate = rs.getString("due_date");
                tasks.add(String.format("%s (Due: %s)", task, dueDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Delete a task for a specific user
    public void deleteTaskForUser(int userId, String task) {
        String deleteQuery = "DELETE FROM tasks WHERE task = ? AND user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setString(1, task);
            pstmt.setInt(2, userId);  // Filter by user ID
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update a task for a specific user
    public void updateTaskForUser(int userId, String oldTask, String newTask, String newDueDate) {
        String updateQuery = "UPDATE tasks SET task = ?, due_date = ? WHERE task = ? AND user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, newTask);
            pstmt.setString(2, newDueDate);
            pstmt.setString(3, oldTask);
            pstmt.setInt(4, userId);  // Filter by user ID
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
