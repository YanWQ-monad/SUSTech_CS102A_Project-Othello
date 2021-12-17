package com.monadx.othello.network.broadcast;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class MulticastServer implements Closeable {
    private static final Logger LOGGER = LogManager.getLogger(MulticastServer.class);

    @NotNull private final InetAddress address;
    @NotNull private final MulticastSocket socket;
    @NotNull private final DatagramPacket packet;

    @NotNull private final Timer timer;

    public MulticastServer(@NotNull InetAddress address, int port, @NotNull MulticastMessage message) throws IOException {
        this.address = address;
        socket = new MulticastSocket(port);
        socket.joinGroup(address);

        InetSocketAddress group = new InetSocketAddress(address, port);
        byte[] bytes = message.toBytes();
        packet = new DatagramPacket(bytes, bytes.length, group);

        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }, 0, 1000);

        LOGGER.info("Multicast server created");
    }

    private void tick() {
        try {
            socket.send(packet);
            LOGGER.trace("Send broadcast packet: {}", packet);
        } catch (IOException e) {
            LOGGER.error("Failed to send broadcast packet", e);
        }
    }

    @Override
    public void close() throws IOException {
        timer.cancel();
        socket.leaveGroup(address);
        socket.close();

        LOGGER.info("Multicast server closed");
    }
}
