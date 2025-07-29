// ===== Client.java =====

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        System.out.println("Connected to the chat server.");

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        Thread receiveThread = new Thread(() -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server.");
            }
        });
        receiveThread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            writer.println(message);
            System.out.println("You: " + message);
        }

    }
}
