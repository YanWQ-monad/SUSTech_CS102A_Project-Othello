package com.monadx.othello.network.packet;

import java.io.*;
import java.util.zip.CRC32;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record RawPacket(
    int packetId,
    int length,
    long checksum,
    byte[] data
) {
    @NotNull
    @Contract("_, _ -> new")
    public static RawPacket createFrom(int packetId, @NotNull byte[] data) {
        return new RawPacket(packetId, data.length, calculateChecksum(data), data);
    }

    @NotNull
    @Contract("_ -> new")
    public static <T extends PacketListener> RawPacket createFrom(@NotNull Packet<T> packet) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteStream);
        packet.serialize(stream);
        return RawPacket.createFrom(packet.getPacketId(), byteStream.toByteArray());
    }

    @NotNull
    @Contract("_ -> new")
    public static RawPacket readFrom(@NotNull DataInput stream) throws IOException {
        int packetId = stream.readInt();
        int length = stream.readInt();
        long checksum = stream.readLong();
        byte[] data = new byte[length];
        stream.readFully(data);

        return new RawPacket(packetId, length, checksum, data);
    }

    public void writeTo(@NotNull DataOutput stream) throws IOException {
        stream.writeInt(packetId);
        stream.writeInt(length);
        stream.writeLong(checksum);
        stream.write(data);
    }

    public boolean verify() {
        return calculateChecksum(data) == checksum;
    }

    private static long calculateChecksum(@NotNull byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }
}
