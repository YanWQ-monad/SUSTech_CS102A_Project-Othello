package com.monadx.othello.network.connection.handler;

import com.monadx.othello.network.packet.handshake.ClientPacketListener;
import com.monadx.othello.network.packet.handshake.ClientboundHelloPacket;

public class ClientHandshakePacketHandler extends PacketHandler<ClientPacketListener> {
    private static final PacketMapping<ClientPacketListener> map = new PacketMapping<>();

    public ClientHandshakePacketHandler(ClientPacketListener listener) {
        this.listener = listener;
    }

    @Override
    PacketMapping<ClientPacketListener> getMapping() {
        return map;
    }

    static {
        register(map, ClientboundHelloPacket.PACKET_ID, ClientboundHelloPacket.class.getName(), ClientboundHelloPacket::deserialize);
    }
}
