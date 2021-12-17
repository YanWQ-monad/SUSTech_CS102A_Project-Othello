package com.monadx.othello.network.broadcast;

import java.io.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.save.NullHelper;

public record MulticastMessage(
        int port,
        @NotNull String serverName,
        @Nullable ChessColor serverColor
) {
    static final long MAGIC_HEADER = 0x9784757560932L;

    public MulticastMessage(int port, @NotNull String serverName, @Nullable ChessColor serverColor) {
        this.port = port;
        this.serverName = serverName;
        this.serverColor = serverColor;
    }

    @Nullable
    public static MulticastMessage createWithCheck(int port, @NotNull String serverName, @Nullable ChessColor serverColor) {
        if (port < 0 || port > 65535) {
            return null;
        }
        return new MulticastMessage(port, serverName, serverColor);
    }

    @NotNull
    public byte[] toBytes() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byteStream);

        try {
            stream.writeLong(MAGIC_HEADER);
            stream.writeInt(port);
            stream.writeUTF(serverName);
            NullHelper.serializeNullable(stream, serverColor, o -> serverColor.serialize(o));

            stream.close();
        } catch (IOException e) {
            // unreachable since `ByteArrayOutputStream` won't cause IOException
            assert false;
        }

        return byteStream.toByteArray();
    }

    @Nullable
    public static MulticastMessage fromBytes(@NotNull byte[] bytes, int offset, int length) {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes, offset, length);
        DataInputStream stream = new DataInputStream(byteStream);

        try {
            if (stream.readLong() != MAGIC_HEADER) {
                return null;
            }

            int port = stream.readInt();
            String serverName = stream.readUTF();
            ChessColor serverColor = NullHelper.deserializeNullable(stream, ChessColor::deserialize);

            return MulticastMessage.createWithCheck(port, serverName, serverColor);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MulticastMessage other = (MulticastMessage) obj;
        return port == other.port &&
                serverName.equals(other.serverName) &&
                serverColor == other.serverColor;
    }

    public int getPort() {
        return port;
    }

    @NotNull
    public String getServerName() {
        return serverName;
    }

    @Nullable
    public ChessColor getServerColor() {
        return serverColor;
    }

    @Override
    public String toString() {
        return "MulticastMessage{" +
                "port=" + port +
                ", serverName='" + serverName + '\'' +
                ", serverColor=" + serverColor +
                '}';
    }
}
