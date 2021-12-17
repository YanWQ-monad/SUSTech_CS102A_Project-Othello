package com.monadx.othello.network.packet.handshake;

import java.io.IOException;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.packet.PacketListener;

public interface ClientPacketListener extends PacketListener {
    void handleHello(@NotNull ClientboundHelloPacket packet) throws IOException;
}
