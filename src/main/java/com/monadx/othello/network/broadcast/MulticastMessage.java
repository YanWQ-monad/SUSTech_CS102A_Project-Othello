package com.monadx.othello.network.broadcast;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import java.io.*;

import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.save.NullHelper;

public class MulticastMessage {
    static final long MAGIC_HEADER = 0x9784757560932L;

    int port;
    String serverName;
    @Nullable ChessColor serverColor;

    public MulticastMessage(int port, String serverName, @Nullable ChessColor serverColor) {
        this.port = port;
        this.serverName = serverName;
        this.serverColor = serverColor;
    }

    @Nullable
    @Contract(pure = true)
    public static MulticastMessage createWithCheck(int port, String serverName, @Nullable ChessColor serverColor) {
        if (port < 0 || port > 65535) {
            return null;
        }
        return new MulticastMessage(port, serverName, serverColor);
    }

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
    public static MulticastMessage fromBytes(byte[] bytes, int offset, int length) {
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
