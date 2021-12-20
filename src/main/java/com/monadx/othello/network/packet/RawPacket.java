package com.monadx.othello.network.packet;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawPacket rawPacket = (RawPacket) o;
        return packetId == rawPacket.packetId && length == rawPacket.length && checksum == rawPacket.checksum && Arrays.equals(data, rawPacket.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(packetId, length, checksum);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
