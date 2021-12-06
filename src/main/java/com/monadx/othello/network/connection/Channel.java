package com.monadx.othello.network.connection;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public record Channel(InputStream input, OutputStream output, Socket socket) implements Closeable {
    public static Channel connect(InetAddress address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        return new Channel(socket.getInputStream(), socket.getOutputStream(), socket);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
