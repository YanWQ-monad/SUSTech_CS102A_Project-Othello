package com.monadx.othello.network.packet.handshake;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.Constant;
import com.monadx.othello.network.packet.Packet;
import com.monadx.othello.network.utils.CryptoHelper;

public class ServerboundHelloPacket extends Packet<ServerPacketListener> {
    public static final int PACKET_ID = 0x01;

    @NotNull public final byte[] publicKeyEncoded;
    @NotNull public final byte[] nonce;

    public ServerboundHelloPacket(@NotNull byte[] publicKeyEncoded, @NotNull byte[] nonce) {
        this.publicKeyEncoded = publicKeyEncoded;
        this.nonce = nonce;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static ServerboundHelloPacket deserialize(@NotNull DataInputStream stream) throws IOException {
        int keyLength = stream.readInt();
        byte[] key = new byte[keyLength];
        stream.readFully(key);
        byte[] nonce = new byte[Constant.TWO_NONCE_LENGTH];
        stream.readFully(nonce);
        return new ServerboundHelloPacket(key, nonce);
    }

    @Override
    public void serialize(@NotNull DataOutputStream stream) throws IOException {
        stream.writeInt(publicKeyEncoded.length);
        stream.write(publicKeyEncoded);
        stream.write(nonce);
    }

    @Override
    public void handle(@NotNull ServerPacketListener listener) throws IOException {
        listener.handleHello(this);
    }

    @Override
    public int getPacketId() {
        return PACKET_ID;
    }

    @Override
    public String toString() {
        return "ServerboundHelloPacket{" +
                "publicKeyEncoded=" + CryptoHelper.toHexString(publicKeyEncoded) +
                ", nonce=" + CryptoHelper.toHexString(nonce) +
                '}';
    }
}
