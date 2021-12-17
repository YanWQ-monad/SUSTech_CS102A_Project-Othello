package com.monadx.othello.network.packet.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.network.packet.Packet;

public class GameStartPacket extends Packet<GamePacketListener> {
    public static final int PACKET_ID = 0x01;

    @NotNull public final ChessColor color;
    public final int boardHash;

    public GameStartPacket(@NotNull ChessColor color, int boardHash) {
        this.color = color;
        this.boardHash = boardHash;
    }

    @NotNull
    @Contract("_ -> new")
    public static GameStartPacket deserialize(@NotNull DataInputStream stream) throws IOException {
        ChessColor color = ChessColor.deserialize(stream);
        int boardHash = stream.readInt();
        return new GameStartPacket(color, boardHash);
    }

    @Override
    public void serialize(@NotNull DataOutputStream stream) throws IOException {
        color.serialize(stream);
        stream.writeInt(boardHash);
    }

    @Override
    public void handle(@NotNull GamePacketListener listener) throws IOException {
        listener.handleGameStart(this);
    }

    @Override
    public int getPacketId() {
        return PACKET_ID;
    }

    @Override
    public String toString() {
        return "GameStartPacket{" +
                "color=" + color +
                ", boardHash=" + boardHash +
                '}';
    }
}
