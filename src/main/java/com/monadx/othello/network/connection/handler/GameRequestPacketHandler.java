package com.monadx.othello.network.connection.handler;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.packet.game.*;

public class GameRequestPacketHandler extends PacketHandler<GameRequestPacketListener> {
    @NotNull private static final PacketMapping<GameRequestPacketListener> map = new PacketMapping<>();

    public GameRequestPacketHandler(@NotNull GameRequestPacketListener listener) {
        this.listener = listener;
    }

    @Override
    @NotNull
    PacketMapping<GameRequestPacketListener> getMapping() {
        return map;
    }

    static {
        register(map, UndoRequestPacket.PACKET_ID, UndoRequestPacket.class.getName(), UndoRequestPacket::deserialize);
        register(map, UndoResponsePacket.PACKET_ID, UndoResponsePacket.class.getName(), UndoResponsePacket::deserialize);
        register(map, RestartRequestPacket.PACKET_ID, RestartRequestPacket.class.getName(), RestartRequestPacket::deserialize);
        register(map, RestartResponsePacket.PACKET_ID, RestartResponsePacket.class.getName(), RestartResponsePacket::deserialize);
    }
}
