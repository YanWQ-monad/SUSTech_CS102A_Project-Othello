package com.monadx.othello.network.packet.handshake;

import java.io.IOException;

import com.monadx.othello.network.packet.PacketListener;

public interface ClientPacketListener extends PacketListener {
    void handleHello(ClientboundHelloPacket packet) throws IOException;
}
