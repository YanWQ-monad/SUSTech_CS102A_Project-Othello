package com.monadx.othello.network.packet;

import java.io.*;
import java.util.zip.CRC32;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.utils.VarInt;

public record RawPacket(
    int packetId,
    int length,
    int checksum,
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
        int packetId = stream.readByte();
        int length = VarInt.readVarInt(stream);
        int checksum = stream.readInt();
        byte[] data = new byte[length];
        stream.readFully(data);

        return new RawPacket(packetId, length, checksum, data);
    }

    public void writeTo(@NotNull DataOutput stream) throws IOException {
        stream.writeByte(packetId);
        VarInt.writeVarInt(stream, length);
        stream.writeInt(checksum);
        stream.write(data);
    }

    public boolean verify() {
        return calculateChecksum(data) == checksum;
    }

    private static int calculateChecksum(@NotNull byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        long value = crc.getValue();
        return (int)((value >>> 32) ^ value);
    }
}
