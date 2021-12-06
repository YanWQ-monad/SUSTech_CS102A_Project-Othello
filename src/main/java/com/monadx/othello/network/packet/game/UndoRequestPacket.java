package com.monadx.othello.network.packet.game;

import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UndoRequestPacket extends Packet<GameRequestPacketListener> {
    public static final int PACKET_ID = 0x03;

    public final int requestId;

    public UndoRequestPacket(int requestId) {
        this.requestId = requestId;
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeInt(requestId);
    }

    public static UndoRequestPacket deserialize(DataInputStream stream) throws IOException {
        int requestId = stream.readInt();
        return new UndoRequestPacket(requestId);
    }

    @Override
    public void handle(GameRequestPacketListener listener) throws IOException {
        listener.onUndoRequest(this);
    }

    @Override
    public int getPacketId() {
        return PACKET_ID;
    }

    @Override
    public String toString() {
        return "UndoRequestPacket{" +
                "requestId=" + requestId +
                '}';
    }
}
