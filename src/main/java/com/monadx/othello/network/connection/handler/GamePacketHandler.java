package com.monadx.othello.network.connection.handler;

import com.monadx.othello.network.packet.game.ChessPlacePacket;
import com.monadx.othello.network.packet.game.GamePacketListener;
import com.monadx.othello.network.packet.game.GameStartPacket;

public class GamePacketHandler extends PacketHandler<GamePacketListener> {
    private static final PacketMapping<GamePacketListener> map = new PacketMapping<>();

    public GamePacketHandler(GamePacketListener listener) {
        this.listener = listener;
    }

    @Override
    PacketMapping<GamePacketListener> getMapping() {
        return map;
    }

    static {
        register(map, GameStartPacket.PACKET_ID, GameStartPacket.class.getName(), GameStartPacket::deserialize);
        register(map, ChessPlacePacket.PACKET_ID, ChessPlacePacket.class.getName(), ChessPlacePacket::deserialize);
    }
}

