import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoApp {
    private final JFrame frame;
    private final JTextField taskField;
    private final DefaultListModel<String> taskModel;
    private final JList<String> taskList;
    private final JSpinner dateSpinner;
    private final Database dbHandler;

    public TodoApp() {
        dbHandler = new Database(); // Database handler

        // Initialize the JFrame
        frame = new JFrame("To-Do List Application");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Set application icon
        ImageIcon image = new ImageIcon("logo.png");
        frame.setIconImage(image.getImage());

        // Task input field
        taskField = new JTextField();
        frame.add(taskField, BorderLayout.NORTH);

        // Date Picker
        JPanel datePanel = new JPanel();
        JLabel dateLabel = new JLabel("Select Date:");
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date());
        datePanel.add(dateLabel);
        datePanel.add(dateSpinner);
        frame.add(datePanel, BorderLayout.WEST);

        // Task list
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);
        loadTasksFromDatabase(); // Load tasks from database
        frame.add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTask());
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(e -> deleteTask());
        buttonPanel.add(deleteButton);

        JButton updateButton = new JButton("Update Task");
        updateButton.addActionListener(e -> updateTask());
        buttonPanel.add(updateButton);

        JButton viewTasksButton = new JButton("View Tasks");
        viewTasksButton.addActionListener(e -> showTasksPanel());
        buttonPanel.add(viewTasksButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Display the frame
        frame.setVisible(true);
    }

    private void addTask() {
        String task = taskField.getText().trim();
        String dueDate = getFormattedDate();

        if (!task.isEmpty()) {
            dbHandler.addTask(task, dueDate); // Add to database
            taskModel.addElement(String.format("%s (Due: %s)", task, dueDate)); // Add to UI
            taskField.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Task cannot be empty!");
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String task = taskModel.getElementAt(selectedIndex).split(" \\(Due:")[0]; // Extract task name
            dbHandler.deleteTask(task); // Remove from database
            taskModel.remove(selectedIndex); // Remove from UI
        } else {
            JOptionPane.showMessageDialog(frame, "Select a task to delete!");
        }
    }

    private void updateTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String newTask = taskField.getText().trim();
            String newDueDate = getFormattedDate();

            if (!newTask.isEmpty()) {
                String oldTask = taskModel.getElementAt(selectedIndex).split(" \\(Due:")[0]; // Extract task name
                dbHandler.updateTask(oldTask, newTask, newDueDate); // Update in database

                String updatedTask = String.format("%s (Due: %s)", newTask, newDueDate);
                taskModel.set(selectedIndex, updatedTask); // Update in UI
                taskField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Updated task cannot be empty!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select a task to update!");
        }
    }

    private void loadTasksFromDatabase() {
        ArrayList<String> tasks = dbHandler.getTasks(); // Fetch tasks from database
        for (String task : tasks) {
            taskModel.addElement(task); // Add each task to the list
        }
    }

    private String getFormattedDate() {
        Date selectedDate = (Date) dateSpinner.getValue();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(selectedDate);
    }

    private void showTasksPanel() {
        // Create a new frame for viewing and modifying tasks
        JFrame tasksFrame = new JFrame("Tasks");
        tasksFrame.setSize(600, 400);
        tasksFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel for tasks
        JPanel tasksPanel = new JPanel();
        tasksPanel.setLayout(new BorderLayout());

        // Task List from Database
        DefaultListModel<String> taskModel = new DefaultListModel<>();
        JList<String> taskList = new JList<>(taskModel);
        ArrayList<String> tasks = dbHandler.getTasks();  // Fetch tasks from database
        for (String task : tasks) {
            taskModel.addElement(task);
        }

        // Task selection panel
        JPanel taskSelectionPanel = new JPanel();
        JButton updateButton = new JButton("Modify Selected Task");
        updateButton.addActionListener(e -> modifySelectedTask(taskList.getSelectedIndex(), taskModel, tasksFrame));
        taskSelectionPanel.add(updateButton);

        // Adding components to the frame
        tasksPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        tasksPanel.add(taskSelectionPanel, BorderLayout.SOUTH);

        tasksFrame.add(tasksPanel);
        tasksFrame.setVisible(true);
    }

    private void modifySelectedTask(int selectedIndex, DefaultListModel<String> taskModel, JFrame tasksFrame) {
        if (selectedIndex != -1) {
            String selectedTask = taskModel.getElementAt(selectedIndex);

            // Open dialog to modify task
            JTextField modifiedTaskField = new JTextField(selectedTask);
            int dialogResult = JOptionPane.showConfirmDialog(tasksFrame, modifiedTaskField, "Modify Task", JOptionPane.OK_CANCEL_OPTION);

            if (dialogResult == JOptionPane.OK_OPTION) {
                String modifiedTask = modifiedTaskField.getText().trim();
                if (!modifiedTask.isEmpty()) {
                    dbHandler.updateTask(selectedTask, modifiedTask, getFormattedDate()); // Update in database
                    taskModel.set(selectedIndex, modifiedTask); // Update UI list
                } else {
                    JOptionPane.showMessageDialog(tasksFrame, "Task cannot be empty!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(tasksFrame, "Select a task to modify!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoApp::new);
    }
}
