package com.monadx.othello.network.broadcast;

import java.io.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.save.NullHelper;

public record MulticastMessage(
        int port,
        @NotNull String serverName,
        @Nullable ChessColor serverColor,
        boolean isPasswordRequired
) {
    static final long MAGIC_HEADER = 0x9784757560932L;

    @Nullable
    public static MulticastMessage createWithCheck(int port, @NotNull String serverName, @Nullable ChessColor serverColor, boolean isPasswordRequired) {
        if (port < 0 || port > 65535) {
            return null;
        }
        return new MulticastMessage(port, serverName, serverColor, isPasswordRequired);
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
            stream.writeByte(isPasswordRequired ? 1 : 0);

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
            boolean isPasswordRequired = stream.readByte() == 1;

            return MulticastMessage.createWithCheck(port, serverName, serverColor, isPasswordRequired);
        } catch (IOException e) {
            return null;
        }
    }
}
