package com.monadx.othello.network.connection;

import java.io.*;
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

    @NotNull
    @Contract(" -> new")
    public Channel withCache() {
        return new Channel(new BufferedInputStream(input), new BufferedOutputStream(output), socket);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
