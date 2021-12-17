package com.monadx.othello.network.connection.handler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.monadx.othello.network.packet.Packet;
import com.monadx.othello.network.packet.PacketListener;
import com.monadx.othello.network.packet.RawPacket;

public abstract class PacketHandler<T extends PacketListener> {
    private final static Logger LOGGER = LogManager.getLogger(PacketHandler.class);

    // `listener` should and will be set in the subclass constructor.
    @NotNull protected T listener;

    @NotNull
    abstract PacketMapping<T> getMapping();

    public boolean handle(@NotNull RawPacket rawPacket) throws IOException {
        PacketItem<T> item = getMapping().get(rawPacket.packetId());
        if (item == null) {
            return false;
        }
        PacketDeserializer<T> function = item.deserializer();

        ByteArrayInputStream byteStream = new ByteArrayInputStream(rawPacket.data());
        DataInputStream stream = new DataInputStream(byteStream);
        Packet<T> packet = function.apply(stream);

        if (packet != null) {
            LOGGER.trace("Handle packet: {}", packet);
            packet.handle(listener);
        }
        return true;
    }

    protected static <T extends PacketListener> void register(@NotNull PacketMapping<T> map, int packetId, @NotNull String className, @NotNull PacketDeserializer<T> deserializer) {
        if (map.containsKey(packetId)) {
            throw new IllegalArgumentException("Packet id " + packetId + " already registered");
        }
        map.put(packetId, new PacketItem<>(className, deserializer));
    }

    public static class PacketMapping<T extends PacketListener> extends HashMap<Integer, PacketItem<T>> {}

    public static record PacketItem<T extends PacketListener>(@NotNull String name, @NotNull PacketDeserializer<T> deserializer) {}

    @FunctionalInterface
    public interface PacketDeserializer<T extends PacketListener> {
        @Nullable
        Packet<T> apply(@NotNull DataInputStream stream) throws IOException;
    }
}
