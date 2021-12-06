package com.monadx.othello.network.packet.handshake;

import java.io.IOException;

import com.monadx.othello.network.packet.PacketListener;

public interface ServerPacketListener extends PacketListener {
    void handleHello(ServerboundHelloPacket packet) throws IOException;
}
