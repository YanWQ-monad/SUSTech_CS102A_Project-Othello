package com.monadx.othello.network.packet.game;

import com.monadx.othello.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UndoResponsePacket extends Packet<GameRequestPacketListener> {
    public static final int PACKET_ID = 0x04;

    public final int requestId;
    public final boolean response;

    public UndoResponsePacket(int requestId, boolean response) {
        this.requestId = requestId;
        this.response = response;
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeInt(requestId);
        stream.writeBoolean(response);
    }

    public static UndoResponsePacket deserialize(DataInputStream stream) throws IOException {
        int requestId = stream.readInt();
        boolean response = stream.readBoolean();
        return new UndoResponsePacket(requestId, response);
    }

    @Override
    public void handle(GameRequestPacketListener listener) throws IOException {
        listener.onUndoResponse(this);
    }

    @Override
    public int getPacketId() {
        return PACKET_ID;
    }

    @Override
    public String toString() {
        return "UndoResponsePacket{" +
                "requestId=" + requestId +
                ", response=" + response +
                '}';
    }
}
