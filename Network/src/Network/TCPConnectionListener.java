package Network;

public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection connection);

    void onReceiveReady(TCPConnection connection, String msg);

    void onDisconnect(TCPConnection connection);

    void onException(TCPConnection connection, Exception e);
}
