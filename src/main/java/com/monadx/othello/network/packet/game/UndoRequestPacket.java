package com.monadx.othello.network.packet.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.packet.Packet;

public class UndoRequestPacket extends Packet<GameRequestPacketListener> {
    public static final int PACKET_ID = 0x03;

    public final int requestId;

    public UndoRequestPacket(int requestId) {
        this.requestId = requestId;
    }

    @Override
    public void serialize(@NotNull DataOutputStream stream) throws IOException {
        stream.writeInt(requestId);
    }

    @NotNull
    @Contract("_ -> new")
    public static UndoRequestPacket deserialize(@NotNull DataInputStream stream) throws IOException {
        int requestId = stream.readInt();
        return new UndoRequestPacket(requestId);
    }

    @Override
    public void handle(@NotNull GameRequestPacketListener listener) throws IOException {
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
