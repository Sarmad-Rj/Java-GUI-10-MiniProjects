package P4_To_Do_List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GUI extends JFrame {
    private TaskManager taskManager;
    private JPanel taskListPanel;

    // Define a professional color palette
    private static final Color PRIMARY_COLOR = new Color(76, 175, 80);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color CARD_BACKGROUND_COLOR = Color.WHITE;
    private static final Color DONE_TASK_COLOR = new Color(230, 255, 230);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Color SUBDUED_TEXT_COLOR = new Color(120, 120, 120);
    private static final Color DELETE_BUTTON_COLOR = new Color(220, 50, 50);

    // Define modern fonts
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font TASK_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public GUI() {
        // Initialize
        taskManager = new TaskManager();
        setTitle("To Do List Application");
        setSize(400, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set overall background color
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Header
        JLabel headerLabel = new JLabel("TO DO LIST", SwingConstants.CENTER);
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(new EmptyBorder(20, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Task list panel inside scroll pane
        taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBackground(BACKGROUND_COLOR);
        JScrollPane scrollPane = new JScrollPane(taskListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Add Task button
        JButton addTaskButton = new JButton("ADD TASK");
        addTaskButton.setFont(BUTTON_FONT);
        addTaskButton.setBackground(PRIMARY_COLOR);
        addTaskButton.setForeground(Color.WHITE);
        addTaskButton.setFocusPainted(false);
        addTaskButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        addTaskButton.addActionListener(e -> addTask());
        add(addTaskButton, BorderLayout.SOUTH);

        // Load and display tasks
        refreshTaskList();

        setVisible(true);
    }

    private void refreshTaskList() {
        taskListPanel.removeAll();
        List<Task> tasks = taskManager.getTasks();

        if (tasks.isEmpty()) {
            JLabel noTasksLabel = new JLabel("No tasks yet! Add one below.", SwingConstants.CENTER);
            noTasksLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noTasksLabel.setForeground(SUBDUED_TEXT_COLOR);
            taskListPanel.add(Box.createVerticalGlue()); // used to Push to center vertically
            taskListPanel.add(noTasksLabel);
            taskListPanel.add(Box.createVerticalGlue());
        } else {
            for (Task task : tasks) {
                JPanel row = createTaskRow(task);
                taskListPanel.add(row);
                taskListPanel.add(Box.createVerticalStrut(8)); // add a small vertical space between tasks
            }
        }
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }

    private JPanel createTaskRow(Task task) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Color for done tasks (set here so components can inherit/match)
        if (task.isDone()) {
            panel.setBackground(DONE_TASK_COLOR);
        } else {
            panel.setBackground(CARD_BACKGROUND_COLOR);
        }

        // Checkbox
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(task.isDone());
        checkBox.setBackground(panel.getBackground()); // <--- IMPORTANT: Set checkbox BG to panel BG
        checkBox.addActionListener(e -> {
            taskManager.markTaskDone(task);
            refreshTaskList();
        });
        panel.add(checkBox, BorderLayout.WEST);

        // Title and date label (center)
        String displayTitle = task.getTitle().length() > 30
                ? task.getTitle().substring(0, 27) + "..."
                : task.getTitle();

        JLabel titleLabel = new JLabel();
        if (task.isDone()) {
            titleLabel.setText("<html><strike><b style='font-family:Segoe UI; font-size:14px; color:" +
                    SUBDUED_TEXT_COLOR.getRed() + "," + SUBDUED_TEXT_COLOR.getGreen() + "," + SUBDUED_TEXT_COLOR.getBlue() + ";'>" +
                    displayTitle + "</b></strike><br><span style='color:" +
                    SUBDUED_TEXT_COLOR.getRed() + "," + SUBDUED_TEXT_COLOR.getGreen() + "," + SUBDUED_TEXT_COLOR.getBlue() + ";font-size:10px;font-family:Segoe UI;'>Due: " +
                    task.getDueDate() + "</span></html>");
        } else {
            titleLabel.setText("<html><b style='font-family:Segoe UI; font-size:14px; color:" +
                    TEXT_COLOR.getRed() + "," + TEXT_COLOR.getGreen() + "," + TEXT_COLOR.getBlue() + ";'>" +
                    displayTitle + "</b><br><span style='color:" +
                    SUBDUED_TEXT_COLOR.getRed() + "," + SUBDUED_TEXT_COLOR.getGreen() + "," + SUBDUED_TEXT_COLOR.getBlue() + ";font-size:10px;font-family:Segoe UI;'>Due: " +
                    task.getDueDate() + "</span></html>");
        }
        titleLabel.setToolTipText(task.getTitle());
        panel.add(titleLabel, BorderLayout.CENTER);

        // Delete button (right)
        JButton deleteButton = new JButton("X");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteButton.setForeground(DELETE_BUTTON_COLOR);
        deleteButton.setBackground(panel.getBackground()); // <--- IMPORTANT: Set button BG to panel BG!!!
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Are you sure you want to delete this task?", "Delete Task",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                taskManager.deleteTask(task);
                refreshTaskList();
            }
        });
        panel.add(deleteButton, BorderLayout.EAST);

        return panel;
    }

    private void addTask() {
        JTextField titleField = new JTextField();
        titleField.setFont(TASK_TITLE_FONT);

        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        inputPanel.add(new JLabel("Task Title:"));
        inputPanel.add(titleField);

        // Customize JOptionPane for a cleaner look
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("OptionPane.messageFont", TASK_TITLE_FONT);
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("Button.background", PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add New Task",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Reset UIManager changes to avoid affecting other components
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageFont", null);
        UIManager.put("Button.font", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            if (!title.isEmpty()) {
                String dueDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                Task newTask = new Task(title, "", dueDate);
                taskManager.addTask(newTask);
                refreshTaskList();
            } else {
                JOptionPane.showMessageDialog(this, "Task title cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}