import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientGUI2 {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1234;

    private JFrame frame;
    private JPanel chatPanel;
    private JTextField inputField;
    private PrintWriter writer;
    private JScrollPane scrollPane;

    public ClientGUI2() {
        frame = new JFrame("Chat App");
        frame.setSize(400, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Header
        JLabel header = new JLabel("   Chat Room", SwingConstants.LEFT);
        header.setOpaque(true);
        header.setBackground(new Color(7, 94, 84));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setPreferredSize(new Dimension(400, 50));
        frame.add(header, BorderLayout.NORTH);

        // Chat Panel
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(230, 221, 213));

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Input
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton sendButton = new JButton("Send");
        sendButton.setBackground(new Color(37, 211, 102));
        sendButton.setForeground(Color.WHITE);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        frame.setVisible(true);

        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                String message;
                try {
                    while ((message = reader.readLine()) != null) {
                        addMessage(message, false);
                    }
                } catch (IOException ignored) {}
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to connect to server.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty() && writer != null) {
            writer.println(message);
            addMessage(message, true);
            inputField.setText("");
        }
    }

    private void addMessage(String message, boolean isSender) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String htmlMessage = String.format(
                "<html><body style='width: 200px; padding: 5px;'>%s<br><div style='text-align: right; font-size: 9px; color: gray;'>%s</div></body></html>",
                message, time);

        JLabel messageLabel = new JLabel(htmlMessage);
        messageLabel.setOpaque(true);
        messageLabel.setBackground(isSender ? new Color(220, 248, 198) : Color.WHITE);
        messageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel wrapper = new JPanel(new FlowLayout(isSender ? FlowLayout.RIGHT : FlowLayout.LEFT));
        wrapper.setOpaque(false);
        wrapper.add(messageLabel);

        SwingUtilities.invokeLater(() -> {
            chatPanel.add(wrapper);
            chatPanel.revalidate();
            JScrollBar sb = scrollPane.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientGUI2::new);
    }
}
