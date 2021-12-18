package com.monadx.othello.network.packet.handshake;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.Constant;
import com.monadx.othello.network.packet.Packet;
import com.monadx.othello.network.utils.CryptoHelper;

public class ClientboundHelloPacket extends Packet<ClientPacketListener> {
    public static final int PACKET_ID = 0x01;

    @NotNull public final byte[] publicKeyEncoded;
    @NotNull public final byte[] nonce;
    @NotNull public final byte[] salt;
    @NotNull public final byte[] hashedPassword;

    public ClientboundHelloPacket(@NotNull byte[] publicKeyEncoded, @NotNull byte[] nonce, @NotNull byte[] salt, @NotNull byte[] hashedPassword) {
        this.publicKeyEncoded = publicKeyEncoded;
        this.nonce = nonce;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static ClientboundHelloPacket deserialize(@NotNull DataInputStream stream) throws IOException {
        int keyLength = stream.readInt();
        byte[] key = new byte[keyLength];
        stream.readFully(key);
        byte[] nonce = new byte[Constant.TWO_NONCE_LENGTH];
        stream.readFully(nonce);
        byte[] salt = new byte[Constant.PASSWORD_SALT_LENGTH];
        stream.readFully(salt);
        byte[] hashedPassword = new byte[Constant.DIGEST_LENGTH];
        stream.readFully(hashedPassword);
        return new ClientboundHelloPacket(key, nonce, salt, hashedPassword);
    }

    @Override
    public void serialize(@NotNull DataOutputStream stream) throws IOException {
        stream.writeInt(publicKeyEncoded.length);
        stream.write(publicKeyEncoded);
        stream.write(nonce);
        stream.write(salt);
        stream.write(hashedPassword);
    }

    @Override
    public void handle(@NotNull ClientPacketListener listener) throws IOException {
        listener.handleHello(this);
    }

    @Override
    public int getPacketId() {
        return PACKET_ID;
    }

    @Override
    public String toString() {
        return "ClientboundHelloPacket{" +
                "publicKeyEncoded=" + CryptoHelper.toHexString(publicKeyEncoded) +
                ", nonce=" + CryptoHelper.toHexString(nonce) +
                ", salt=" + CryptoHelper.toHexString(salt) +
                ", hashedPassword=" + CryptoHelper.toHexString(hashedPassword) +
                '}';
    }
}
