package com.monadx.othello.network.connection.handler;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.packet.handshake.ClientPacketListener;
import com.monadx.othello.network.packet.handshake.ClientboundHelloPacket;

public class ClientHandshakePacketHandler extends PacketHandler<ClientPacketListener> {
    @NotNull private static final PacketMapping<ClientPacketListener> map = new PacketMapping<>();

    public ClientHandshakePacketHandler(@NotNull ClientPacketListener listener) {
        this.listener = listener;
    }

    @Override
    @NotNull
    PacketMapping<ClientPacketListener> getMapping() {
        return map;
    }

    static {
        register(map, ClientboundHelloPacket.PACKET_ID, ClientboundHelloPacket.class.getName(), ClientboundHelloPacket::deserialize);
    }
}
