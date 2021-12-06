package com.monadx.othello.network.packet.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.network.packet.Packet;

public class ChessPlacePacket extends Packet<GamePacketListener> {
    public static final int PACKET_ID = 0x02;

    public final int x;
    public final int y;
    public final ChessColor color;
    public final int boardHash;

    public ChessPlacePacket(int x, int y, ChessColor color, int boardHash) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.boardHash = boardHash;
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeByte(x);
        stream.writeByte(y);
        color.serialize(stream);
        stream.writeInt(boardHash);
    }

    public static ChessPlacePacket deserialize(DataInputStream stream) throws IOException {
        int x = stream.readByte();
        int y = stream.readByte();
        ChessColor color = ChessColor.deserialize(stream);
        int boardHash = stream.readInt();
        return new ChessPlacePacket(x, y, color, boardHash);
    }

    @Override
    public void handle(GamePacketListener listener) throws IOException {
        listener.handleChessPlace(this);
    }

    @Override
    public int getPacketId() {
        return PACKET_ID;
    }

    @Override
    public String toString() {
        return "ChessPlacePacket{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
                ", boardHash=" + boardHash +
                '}';
    }
}
