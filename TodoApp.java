import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoApp {
    private final JFrame frame;
    private final JTextField taskField;
    private final DefaultListModel<String> taskModel;
    private final JList<String> taskList;
    public final ArrayList<String> tasks;
    private final JSpinner dateSpinner;

    public TodoApp() {
        // Initialize the JFrame
        frame = new JFrame("To-Do List Application");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Setting the application icon
        ImageIcon image = new ImageIcon("logo.png");
        frame.setIconImage(image.getImage());

        // Initialize task list and ArrayList
        tasks = new ArrayList<>();
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);

        // Task input field
        taskField = new JTextField();
        frame.add(taskField, BorderLayout.NORTH);

        // Date Picker
        JLabel dateLabel = new JLabel("Select Date:");
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date()); 

        JPanel datePanel = new JPanel();
        datePanel.add(dateLabel);
        datePanel.add(dateSpinner);

        frame.add(datePanel, BorderLayout.WEST);

        // Buttons for adding, deleting, and updating tasks
        JButton addButton = new JButton("Add Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton updateButton = new JButton("Update Task");
        JButton dateButton = new JButton("Select Date");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(dateButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Scroll pane for the task list
        JScrollPane scrollPane = new JScrollPane(taskList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Button Action Listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTask();
            }
        });

        dateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Selected Date: " + getFormattedDate());
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    private void addTask() {
        String task = taskField.getText().trim();
        Date selectedDate = (Date) dateSpinner.getValue();
        if (!task.isEmpty()) {
            String formattedTask = String.format("%s (Due: %s)", task, getFormattedDate(selectedDate));
            tasks.add(formattedTask);
            taskModel.addElement(formattedTask);
            taskField.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Task cannot be empty!");
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex);
            taskModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(frame, "Select a task to delete!");
        }
    }

    private void updateTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String newTask = taskField.getText().trim();
            Date selectedDate = (Date) dateSpinner.getValue();
            if (!newTask.isEmpty()) {
                String formattedTask = String.format("%s (Due: %s)", newTask, getFormattedDate(selectedDate));
                tasks.set(selectedIndex, formattedTask);
                taskModel.set(selectedIndex, formattedTask);
                taskField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Updated task cannot be empty!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select a task to update!");
        }
    }

    private String getFormattedDate() {
        Date selectedDate = (Date) dateSpinner.getValue();
        return getFormattedDate(selectedDate);
    }

    private String getFormattedDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TodoApp();
            }
        });
    }
}
