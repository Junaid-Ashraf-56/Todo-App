import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TodoApp extends JFrame {
    private JTextField taskField;
    private DefaultListModel<String> taskModel;
    private JList<String> taskList;
    private ArrayList<String> tasks;

    public TodoApp() {
        // Initialize components
        setTitle("To-Do List Application");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

    ImageIcon image = new  ImageIcon("logo.png");
    setIconImage(image.getImage());

        // Initialize task list and ArrayList to store tasks
        tasks = new ArrayList<>();
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);

        // Text field for adding tasks
        taskField = new JTextField();
        add(taskField, BorderLayout.NORTH);

        // Buttons for adding, deleting, and updating tasks
        JButton addButton = new JButton("Add Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton updateButton = new JButton("Update Task");
       
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Scroll pane for the task list
        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        // Add Action Listeners
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
    }

    private void addTask() {
        String task = taskField.getText().trim();
        if (!task.isEmpty()) {
            tasks.add(task);
            taskModel.addElement(task);
            taskField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Task cannot be empty!");
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex);
            taskModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Select a task to delete!");
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
                JOptionPane.showMessageDialog(this, "Updated task cannot be empty!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a task to update!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TodoApp().setVisible(true);
            }
        });
    }
}
