package com.monadx.othello.network.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server implements Closeable {
    private final static Logger LOGGER = LogManager.getLogger(Server.class);

    ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        LOGGER.info("Server started");
    }

    public Channel waitForConnection() throws IOException {
        LOGGER.info("Server listening for connection...");

        Socket socket;
        try {
            socket = serverSocket.accept();
        } catch (SocketException e) {
            if (e.getMessage().equals("Socket closed")) {
                return null;
            }
            throw e;
        }

        LOGGER.info("Connection accepted from {}", socket.getInetAddress());
        return new Channel(socket.getInputStream(), socket.getOutputStream(), socket);
    }

    public boolean isClosed() {
        return serverSocket.isClosed();
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
        LOGGER.info("Server closed");
    }
}
