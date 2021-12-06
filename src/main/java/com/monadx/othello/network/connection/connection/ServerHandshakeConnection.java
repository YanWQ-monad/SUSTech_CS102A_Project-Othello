package com.monadx.othello.network.connection.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

import com.monadx.othello.network.connection.handler.ServerHandshakePacketHandler;
import com.monadx.othello.network.packet.PacketStream;
import com.monadx.othello.network.packet.handshake.ClientboundHelloPacket;
import com.monadx.othello.network.packet.handshake.ServerPacketListener;
import com.monadx.othello.network.packet.handshake.ServerboundHelloPacket;

public class ServerHandshakeConnection {
    private final static Logger LOGGER = LogManager.getLogger(ServerHandshakeConnection.class);

    PacketStream stream;
    Stage stage = Stage.START;

    public ServerHandshakeConnection(PacketStream stream) {
        this.stream = stream;
    }

    public void runUntilComplete() throws IOException {
        ServerHandshakePacketHandler handler = new ServerHandshakePacketHandler(new ServerPacketListener() {
            @Override
            public void handleHello(ServerboundHelloPacket packet) throws IOException {
                LOGGER.debug("receive ServerboundHelloPacket");
                stream.send(new ClientboundHelloPacket());
                stage = Stage.FINISHED;
            }
        });

        while (stage != Stage.FINISHED) {
            handler.handle(stream.read());
        }
    }

    public PacketStream getStream() {
        return stream;
    }

    private enum Stage {
        START,
        FINISHED,
    }
}
