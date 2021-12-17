package com.monadx.othello.network.connection.handler;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.packet.handshake.ServerPacketListener;
import com.monadx.othello.network.packet.handshake.ServerboundHelloPacket;

public class ServerHandshakePacketHandler extends PacketHandler<ServerPacketListener> {
    @NotNull private static final PacketMapping<ServerPacketListener> map = new PacketMapping<>();

    public ServerHandshakePacketHandler(@NotNull ServerPacketListener listener) {
        this.listener = listener;
    }

    @Override
    @NotNull
    PacketMapping<ServerPacketListener> getMapping() {
        return map;
    }

    static {
        register(map, ServerboundHelloPacket.PACKET_ID, ServerboundHelloPacket.class.getName(), ServerboundHelloPacket::deserialize);
    }
}
