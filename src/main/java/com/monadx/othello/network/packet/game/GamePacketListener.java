package com.monadx.othello.network.packet.game;

import java.io.IOException;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.packet.PacketListener;

public interface GamePacketListener extends PacketListener {
    void handleGameStart(@NotNull GameStartPacket packet) throws IOException;

    void handleChessPlace(@NotNull ChessPlacePacket packet) throws IOException;
}
