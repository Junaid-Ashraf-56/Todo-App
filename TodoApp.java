import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TodoApp {
    private final JFrame frame;
    private final JTextField taskField;
    private final DefaultListModel<String> taskModel;
    private final JList<String> taskList;
    public final ArrayList<String> tasks;

    public TodoApp() {
        // Initialize the Jframe
        frame = new JFrame("To-Do List Application");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Setting the application icon
        ImageIcon image = new ImageIcon("logo.png");
        frame.setIconImage(image.getImage());

        // Initialize task list and ArrayList 
        tasks = new ArrayList<>();
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);


        taskField = new JTextField();
        frame.add(taskField, BorderLayout.NORTH);

        // Buttons for adding, deleting, and updating tasks
        JButton addButton = new JButton("Add Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton updateButton = new JButton("Update Task");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Scroll pane for the task list
        JScrollPane scrollPane = new JScrollPane(taskList);
        frame.add(scrollPane, BorderLayout.CENTER);


        addButton.addActionListener(new ActionListener()  {
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

        // Make the frame visible
        frame.setVisible(true);
    }

    private void addTask() {
        String task = taskField.getText().trim();
        if (!task.isEmpty()) {
            tasks.add(task);
            taskModel.addElement(task);
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
            if (!newTask.isEmpty()) {
                tasks.set(selectedIndex, newTask);
                taskModel.set(selectedIndex, newTask);
                taskField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Updated task cannot be empty!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select a task to update!");
        }
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
