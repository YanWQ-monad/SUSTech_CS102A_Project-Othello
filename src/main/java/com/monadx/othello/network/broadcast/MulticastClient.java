package com.monadx.othello.network.broadcast;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class MulticastClient implements Closeable {
    private final static Logger LOGGER = LogManager.getLogger(MulticastClient.class);

    static final int BUFFER_SIZE = 1024;

    @NotNull private final InetAddress address;
    @NotNull private final MulticastSocket socket;

    @NotNull private final Consumer<Packet> onMessage;
    @NotNull private final Thread thread;

    public MulticastClient(@NotNull InetAddress address, int port, @NotNull Consumer<Packet> onMessage) throws IOException {
        this.address = address;
        socket = new MulticastSocket(port);
        socket.joinGroup(address);

        this.onMessage = onMessage;
        thread = new Thread(this::listen);
        thread.setDaemon(true);
        thread.start();
    }

    private void listen() {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

        LOGGER.info("Start listening for multicast messages");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                socket.receive(receivePacket);
            } catch (SocketException e) {
                if (e.getMessage().equals("Socket closed")) {
                    break;
                } else {
                    e.printStackTrace();
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            MulticastMessage message = MulticastMessage.fromBytes(buffer, receivePacket.getOffset(), receivePacket.getLength());
            if (message != null) {
                LOGGER.debug("Received broadcast message: {}, from {}", message, receivePacket.getAddress());
                onMessage.accept(new Packet(
                        message,
                        receivePacket.getAddress()
                ));
            }
        }
    }

    @Override
    public void close() throws IOException {
        LOGGER.info("Closing multicast client");
        thread.interrupt();
        socket.leaveGroup(address);
        socket.close();
        try {
            thread.join();
        } catch (InterruptedException ignored) {}
    }

    public static record Packet(@NotNull MulticastMessage message, @NotNull InetAddress sender) {}
}
