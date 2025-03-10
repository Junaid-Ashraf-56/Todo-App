package TodoApp;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:todo.db";

    public Database() {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                // Create the tasks table if it does not exist
                String createTasksTableQuery = "CREATE TABLE IF NOT EXISTS tasks ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "task TEXT NOT NULL,"
                        + "due_date TEXT NOT NULL)";
                stmt.execute(createTasksTableQuery);

                // Create the users table if it does not exist
                String createUsersTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "username TEXT NOT NULL,"
                        + "email TEXT NOT NULL,"
                        + "password TEXT NOT NULL)";
                stmt.execute(createUsersTableQuery);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a task to the database
    public void addTask(String task, String dueDate) {
        String insertQuery = "INSERT INTO tasks (task, due_date) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, task);
            pstmt.setString(2, dueDate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all tasks from the database
    public ArrayList<String> getTasks() {
        ArrayList<String> tasks = new ArrayList<>();
        String selectQuery = "SELECT task, due_date FROM tasks";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {
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

    // Delete a task from the database
    public void deleteTask(String task) {
        String deleteQuery = "DELETE FROM tasks WHERE task = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setString(1, task);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an existing task in the database
    public void updateTask(String oldTask, String newTask, String newDueDate) {
        String updateQuery = "UPDATE tasks SET task = ?, due_date = ? WHERE task = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, newTask);
            pstmt.setString(2, newDueDate);
            pstmt.setString(3, oldTask);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}