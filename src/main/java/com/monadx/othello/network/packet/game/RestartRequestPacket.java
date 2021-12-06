package com.monadx.othello.network.packet.game;

import com.monadx.othello.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RestartRequestPacket extends Packet<GameRequestPacketListener> {
    public static final int PACKET_ID = 0x05;

    public final int requestId;

    public RestartRequestPacket(int requestId) {
        this.requestId = requestId;
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeInt(requestId);
    }

    public static RestartRequestPacket deserialize(DataInputStream stream) throws IOException {
        int requestId = stream.readInt();
        return new RestartRequestPacket(requestId);
    }

    @Override
    public void handle(GameRequestPacketListener listener) throws IOException {
        listener.onRestartRequest(this);
    }

    @Override
    public int getPacketId() {
        return PACKET_ID;
    }

    @Override
    public String toString() {
        return "RestartRequestPacket{" +
                "requestId=" + requestId +
                '}';
    }
}
