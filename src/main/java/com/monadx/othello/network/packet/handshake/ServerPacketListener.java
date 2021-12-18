package com.monadx.othello.network.packet.handshake;

import java.io.IOException;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.packet.PacketListener;

public interface ServerPacketListener extends PacketListener {
    void handleHello(@NotNull ServerboundHelloPacket packet) throws IOException;

    void handlePasswordVerify(@NotNull ServerboundPasswordVerifyPacket packet) throws IOException;
}
