package com.monadx.othello.network.packet.handshake;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.packet.Packet;

public class ClientboundHelloPacket extends Packet<ClientPacketListener> {
    public static final int PACKET_ID = 0x01;

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static ClientboundHelloPacket deserialize(@NotNull DataInputStream stream) throws IOException {
        return new ClientboundHelloPacket();
    }

    @Override
    public void serialize(@NotNull DataOutputStream stream) throws IOException {

    }

    @Override
    public void handle(@NotNull ClientPacketListener listener) throws IOException {
        listener.handleHello(this);
    }

    @Override
    public int getPacketId() {
        return PACKET_ID;
    }

    @Override
    public String toString() {
        return "ClientboundHelloPacket{}";
    }
}
