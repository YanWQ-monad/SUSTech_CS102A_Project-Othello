package com.monadx.othello.network.connection.handler;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.packet.game.ChessPlacePacket;
import com.monadx.othello.network.packet.game.GamePacketListener;
import com.monadx.othello.network.packet.game.GameStartPacket;

public class GamePacketHandler extends PacketHandler<GamePacketListener> {
    @NotNull private static final PacketMapping<GamePacketListener> map = new PacketMapping<>();

    public GamePacketHandler(@NotNull GamePacketListener listener) {
        this.listener = listener;
    }

    @Override
    @NotNull
    PacketMapping<GamePacketListener> getMapping() {
        return map;
    }

    static {
        register(map, GameStartPacket.PACKET_ID, GameStartPacket.class.getName(), GameStartPacket::deserialize);
        register(map, ChessPlacePacket.PACKET_ID, ChessPlacePacket.class.getName(), ChessPlacePacket::deserialize);
    }
}

