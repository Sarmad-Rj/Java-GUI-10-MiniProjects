// Server.java (Corrected - Removed ServerService typo)
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket; // Correct import
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private static final int PORT = 1234;
    private Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        new Server().startServer();
    }

    public void startServer() {
        System.out.println("Chat Server Started on port " + PORT);
        // CORRECTED LINE: Should be ServerSocket, not ServerService
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket, this).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public void addWriter(PrintWriter writer) {
        synchronized (clientWriters) {
            clientWriters.add(writer);
        }
    }

    public void removeWriter(PrintWriter writer) {
        synchronized (clientWriters) {
            clientWriters.remove(writer);
        }
    }

    public void broadcast(String message, PrintWriter senderWriter) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                if (writer != senderWriter) {
                    writer.println(message);
                }
            }
        }
    }

    public int getClientCount() {
        synchronized (clientWriters) {
            return clientWriters.size();
        }
    }
}