// ClientHandler.java (Updated)
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            server.addWriter(writer);

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Received from " + socket.getInetAddress() + ": " + message);
                server.broadcast("Other: " + message, writer);
            }
        } catch (SocketException e) {
            System.out.println("Client disconnected: " + socket.getInetAddress());
        } catch (IOException e) {
            System.err.println("Client handler error for " + socket.getInetAddress() + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket for " + socket.getInetAddress() + ": " + e.getMessage());
            }
            server.removeWriter(writer);
            // CORRECTED LINE: Use the public getter method
            System.out.println("Client " + socket.getInetAddress() + " disconnected. Active clients: " + server.getClientCount());
        }
    }
}