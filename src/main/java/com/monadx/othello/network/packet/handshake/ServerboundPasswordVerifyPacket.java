package com.monadx.othello.network.packet.handshake;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.Constant;
import com.monadx.othello.network.packet.Packet;
import com.monadx.othello.network.utils.CryptoHelper;

public class ServerboundPasswordVerifyPacket extends Packet<ServerPacketListener> {
    public static final int PACKET_ID = 0x02;

    @NotNull public final byte[] hashedPassword;

    public ServerboundPasswordVerifyPacket(@NotNull byte[] hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static ServerboundPasswordVerifyPacket deserialize(@NotNull DataInputStream stream) throws IOException {
        byte[] hashedPassword = new byte[Constant.DIGEST_LENGTH];
        stream.readFully(hashedPassword);
        return new ServerboundPasswordVerifyPacket(hashedPassword);
    }

    @Override
    public void serialize(@NotNull DataOutputStream stream) throws IOException {
        stream.write(hashedPassword);
    }

    @Override
    public void handle(@NotNull ServerPacketListener listener) throws IOException {
        listener.handlePasswordVerify(this);
    }

    @Override
    public int getPacketId() {
        return PACKET_ID;
    }

    @Override
    public String toString() {
        return "ServerboundPasswordVerifyPacket{" +
                "hashedPassword=" + CryptoHelper.toHexString(hashedPassword) +
                '}';
    }
}
