package Client;

import Network.TCPConnection;
import Network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Client extends JFrame implements TCPConnectionListener {

    //~~~~~~~~~~~~~~Variables~~~~~~~~~~~~~~~~~~~
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 7891;
    public static final int WIDTH = 700;
    public static final int HEIGHT = 400;
    private TCPConnection connection;
    private final JTextArea log = new JTextArea(250, 350);
    private final JTextField nickname = new JTextField("Enter your nickname");
    private final JTextField fieldInput = new JTextField();
    private final JButton buttonToConnect = new JButton("><");
    private final JButton buttonToDisconnect = new JButton("<>");
    private int k = 0;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private Client() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setTitle("ChatClient");

        log.setEditable(false);
        log.setLineWrap(true);
        log.setBackground(Color.BLACK);

        buttonToConnect.setEnabled(true);
        buttonToConnect.setSize(10, 10);

        add(log, BorderLayout.CENTER);
        add(nickname, BorderLayout.NORTH);
        add(buttonToConnect, BorderLayout.EAST);
        add(buttonToDisconnect, BorderLayout.WEST);

        fieldInput.addActionListener(e -> {
            String msg = fieldInput.getText();
            if (msg.equals("")) return;
            fieldInput.setText(null);
            connection.sendString(nickname.getText() + ": " + msg);
        });
        add(fieldInput, BorderLayout.SOUTH);


        buttonToDisconnect.addActionListener(e -> {
            if (k == 1) {
                connection.disconnect();
                k = 0;
            } else {
                log.append("Connect to server...");
            }
        });

        buttonToConnect.addActionListener(e -> {
            try {
                k = 1;
                connection = new TCPConnection(this, IP_ADDRESS, PORT);

            } catch (IOException exc) {
                printMsg("Connection exception: " + exc);
            }
        });
        setVisible(true);

    }

    private synchronized void printMsg(String value) {
        SwingUtilities.invokeLater(() -> {
            log.append(value + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }

    @Override
    public void onConnectionReady(TCPConnection connection) {
        printMsg("Connection ready");
    }

    @Override
    public void onReceiveReady(TCPConnection connection, String msg) {
        printMsg(msg);
    }

    @Override
    public void onDisconnect(TCPConnection connection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConnection connection, Exception e) {
        printMsg("Connection exception: " + e);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client());
    }


}
