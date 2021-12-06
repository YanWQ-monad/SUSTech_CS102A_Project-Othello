package com.monadx.othello.network.connection.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import com.monadx.othello.network.packet.Packet;
import com.monadx.othello.network.packet.PacketListener;
import com.monadx.othello.network.packet.RawPacket;

public abstract class PacketHandler<T extends PacketListener> {
    private final static Logger LOGGER = LogManager.getLogger(PacketHandler.class);

    protected T listener;

    abstract PacketMapping<T> getMapping();

    public boolean handle(RawPacket rawPacket) throws IOException {
        PacketItem<T> item = getMapping().get(rawPacket.packetId());
        PacketDeserializer<T> function = item.deserializer();
        if (function == null) {
            return false;
        }

        ByteArrayInputStream byteStream = new ByteArrayInputStream(rawPacket.data());
        DataInputStream stream = new DataInputStream(byteStream);
        Packet<T> packet = function.apply(stream);

        if (packet != null) {
            LOGGER.trace("Handle packet: {}", packet);
            packet.handle(listener);
        }
        return true;
    }

    protected static <T extends PacketListener> void register(PacketMapping<T> map, int packetId, String className, PacketDeserializer<T> deserializer) {
        if (map.containsKey(packetId)) {
            throw new IllegalArgumentException("Packet id " + packetId + " already registered");
        }
        map.put(packetId, new PacketItem<T>(className, deserializer));
    }

    public static class PacketMapping<T extends PacketListener> extends HashMap<Integer, PacketItem<T>> {}

    public static record PacketItem<T extends PacketListener>(String name, PacketDeserializer<T> deserializer) {}

    @FunctionalInterface
    public interface PacketDeserializer<T extends PacketListener> {
       Packet<T> apply(DataInputStream stream) throws IOException;
    }
}
