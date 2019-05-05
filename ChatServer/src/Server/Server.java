package Server;

import Network.TCPConnection;
import Network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements TCPConnectionListener {

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private Server() {
        System.out.println("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(7891)) {
            while (true) {
                try {
                    new TCPConnection(serverSocket.accept(), this);
                } catch (IOException e) {
                    System.out.println("TCPConnection" + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


    @Override
    public synchronized void onConnectionReady(TCPConnection connection) {
        connections.add(connection);
        sendToAllConections("Client connected: " + connection.toString());
    }

    @Override
    public synchronized void onReceiveReady(TCPConnection connection, String msg) {
        sendToAllConections(msg);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection connection) {
        connections.remove(connection);
        sendToAllConections("Client disconnected: " + connection.toString());
    }

    @Override
    public synchronized void onException(TCPConnection connection, Exception e) {
        System.out.println("TCPConnection exception" + e);
    }

    private void sendToAllConections(String value) {
        System.out.println(value);
        connections.forEach(connection -> connection.sendString(value));
    }

    public static void main(String[] args) {
        new Server();
    }
}
