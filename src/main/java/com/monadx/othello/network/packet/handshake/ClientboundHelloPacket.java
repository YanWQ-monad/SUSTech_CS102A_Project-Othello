package com.monadx.othello.network.packet.handshake;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.monadx.othello.network.packet.Packet;

public class ClientboundHelloPacket extends Packet<ClientPacketListener> {
    public static final int PACKET_ID = 0x01;

    public static ClientboundHelloPacket deserialize(DataInputStream stream) throws IOException {
        return new ClientboundHelloPacket();
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {

    }

    @Override
    public void handle(ClientPacketListener listener) throws IOException {
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
