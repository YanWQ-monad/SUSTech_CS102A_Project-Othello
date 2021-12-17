package com.monadx.othello.network.connection;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Channel(@NotNull InputStream input, @NotNull OutputStream output, @NotNull Socket socket) implements Closeable {
    @NotNull
    @Contract("_, _ -> new")
    public static Channel connect(@NotNull InetAddress address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        return new Channel(socket.getInputStream(), socket.getOutputStream(), socket);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
