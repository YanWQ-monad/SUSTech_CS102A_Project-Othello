package com.monadx.othello.network.packet.game;

import java.io.IOException;

import com.monadx.othello.network.packet.PacketListener;

public interface GamePacketListener extends PacketListener {
    void handleGameStart(GameStartPacket packet) throws IOException;

    void handleChessPlace(ChessPlacePacket packet) throws IOException;
}
