package com.monadx.othello.network.packet.game;

import com.monadx.othello.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RestartResponsePacket extends Packet<GameRequestPacketListener> {
    public static final int PACKET_ID = 0x06;

    public final int requestId;
    public final boolean response;

    public RestartResponsePacket(int requestId, boolean response) {
        this.requestId = requestId;
        this.response = response;
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeInt(requestId);
        stream.writeBoolean(response);
    }

    public static RestartResponsePacket deserialize(DataInputStream stream) throws IOException {
        int requestId = stream.readInt();
        boolean response = stream.readBoolean();
        return new RestartResponsePacket(requestId, response);
    }

    @Override
    public void handle(GameRequestPacketListener listener) throws IOException {
        listener.onRestartResponse(this);
    }

    @Override
    public int getPacketId() {
        return PACKET_ID;
    }

    @Override
    public String toString() {
        return "RestartResponsePacket{" +
                "requestId=" + requestId +
                ", response=" + response +
                '}';
    }
}
