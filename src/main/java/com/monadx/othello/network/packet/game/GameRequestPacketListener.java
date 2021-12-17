package com.monadx.othello.network.packet.game;

import java.io.IOException;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.packet.PacketListener;

public interface GameRequestPacketListener extends PacketListener {
    void onUndoRequest(@NotNull UndoRequestPacket packet) throws IOException;

    void onUndoResponse(@NotNull UndoResponsePacket packet) throws IOException;

    void onRestartRequest(@NotNull RestartRequestPacket packet) throws IOException;

    void onRestartResponse(@NotNull RestartResponsePacket packet) throws IOException;
}
