package com.monadx.othello.network.connection.handler;

import com.monadx.othello.network.packet.handshake.ServerPacketListener;
import com.monadx.othello.network.packet.handshake.ServerboundHelloPacket;

public class ServerHandshakePacketHandler extends PacketHandler<ServerPacketListener> {
    private static final PacketMapping<ServerPacketListener> map = new PacketMapping<>();

    public ServerHandshakePacketHandler(ServerPacketListener listener) {
        this.listener = listener;
    }

    @Override
    PacketMapping<ServerPacketListener> getMapping() {
        return map;
    }

    static {
        register(map, ServerboundHelloPacket.PACKET_ID, ServerboundHelloPacket.class.getName(), ServerboundHelloPacket::deserialize);
    }
}
