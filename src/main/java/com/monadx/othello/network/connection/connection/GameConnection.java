package com.monadx.othello.network.connection.connection;

import java.io.Closeable;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.monadx.othello.network.connection.handler.GamePacketHandler;
import com.monadx.othello.network.connection.handler.GameRequestPacketHandler;
import com.monadx.othello.network.packet.Packet;
import com.monadx.othello.network.packet.PacketListener;
import com.monadx.othello.network.packet.PacketStream;
import com.monadx.othello.network.packet.RawPacket;
import com.monadx.othello.network.packet.game.GamePacketListener;
import com.monadx.othello.network.packet.game.GameRequestPacketListener;

public class GameConnection implements Closeable {
    private final static Logger LOGGER = LogManager.getLogger(GameConnection.class);

    @NotNull private final PacketStream stream;
    @Nullable private Thread listenThread;

    @Nullable private GameRequestPacketHandler requestHandler;

    public GameConnection(@NotNull PacketStream stream) {
        this.stream = stream;
    }

    public void listen(@NotNull GamePacketListener listener) {
        listenThread = new Thread(() -> {
            GamePacketHandler handler = new GamePacketHandler(listener);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    RawPacket rawPacket = stream.read();

                    if (requestHandler != null && requestHandler.handle(rawPacket)) {
                        continue;
                    }
                    handler.handle(rawPacket);
                } catch (IOException e) {
                    LOGGER.error("IOException caught", e);
                    break;
                }
            }
        });

        listenThread.start();
    }

    public void registerRequestListener(@NotNull GameRequestPacketListener listener) {
        requestHandler = new GameRequestPacketHandler(listener);
    }

    public <T extends PacketListener> void send(@NotNull Packet<T> packet) throws IOException {
        stream.send(packet);
    }

    @Override
    public void close() throws IOException {
        stream.close();

        if (listenThread != null) {
            listenThread.interrupt();

            try {
                listenThread.join();
            } catch (InterruptedException ignore) {}
        }
    }
}
