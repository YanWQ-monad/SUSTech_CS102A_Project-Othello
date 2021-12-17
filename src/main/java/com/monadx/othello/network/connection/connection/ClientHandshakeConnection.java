package com.monadx.othello.network.connection.connection;

import java.io.IOException;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.connection.handler.ClientHandshakePacketHandler;
import com.monadx.othello.network.packet.PacketStream;
import com.monadx.othello.network.packet.handshake.ClientboundHelloPacket;
import com.monadx.othello.network.packet.handshake.ClientPacketListener;
import com.monadx.othello.network.packet.handshake.ServerboundHelloPacket;

public class ClientHandshakeConnection {
    @NotNull private final PacketStream stream;
    @NotNull private Stage stage = Stage.START;

    public ClientHandshakeConnection(@NotNull PacketStream stream) {
        this.stream = stream;
    }

    public void runUntilComplete() throws IOException {
        ClientHandshakePacketHandler handler = new ClientHandshakePacketHandler(new ClientPacketListener() {
            @Override
            public void handleHello(@NotNull ClientboundHelloPacket packet) {
                stage = Stage.FINISHED;
            }
        });

        stream.send(new ServerboundHelloPacket());
        while (stage != Stage.FINISHED) {
            handler.handle(stream.read());
        }
    }

    @NotNull
    public PacketStream getStream() {
        return stream;
    }

    private enum Stage {
        START,
        FINISHED,
    }
}
