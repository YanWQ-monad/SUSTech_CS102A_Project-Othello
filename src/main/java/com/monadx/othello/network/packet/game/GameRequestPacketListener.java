package com.monadx.othello.network.packet.game;

import com.monadx.othello.network.packet.PacketListener;

import java.io.IOException;

public interface GameRequestPacketListener extends PacketListener {
    void onUndoRequest(UndoRequestPacket packet) throws IOException;

    void onUndoResponse(UndoResponsePacket packet) throws IOException;

    void onRestartRequest(RestartRequestPacket packet) throws IOException;

    void onRestartResponse(RestartResponsePacket packet) throws IOException;
}
