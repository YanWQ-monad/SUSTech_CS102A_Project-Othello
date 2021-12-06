package com.monadx.othello.network.packet.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.network.packet.Packet;

public class GameStartPacket extends Packet<GamePacketListener> {
    public static final int PACKET_ID = 0x01;

    public final ChessColor color;
    public final int boardHash;

    public GameStartPacket(ChessColor color, int boardHash) {
        this.color = color;
        this.boardHash = boardHash;
    }

    public static GameStartPacket deserialize(DataInputStream stream) throws IOException {
        ChessColor color = ChessColor.deserialize(stream);
        int boardHash = stream.readInt();
        return new GameStartPacket(color, boardHash);
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        color.serialize(stream);
        stream.writeInt(boardHash);
    }

    @Override
    public void handle(GamePacketListener listener) throws IOException {
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
