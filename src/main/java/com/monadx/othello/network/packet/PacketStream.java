package com.monadx.othello.network.packet;

import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.network.connection.Channel;

public class PacketStream implements Closeable {
    private final static Logger LOGGER = LogManager.getLogger(PacketStream.class);

    @NotNull private final DataInputStream in;
    @NotNull private final DataOutputStream out;
    @NotNull private final Channel channel;

    public PacketStream(@NotNull Channel channel) {
        this.in = new DataInputStream(channel.input());
        this.out = new DataOutputStream(channel.output());
        this.channel = channel;
    }

    @NotNull
    public RawPacket read() throws IOException {
        return RawPacket.readFrom(in);
    }

    private void send(@NotNull RawPacket packet) throws IOException {
        packet.writeTo(out);
        out.flush();
    }

    public <T extends Packet<L>, L extends PacketListener> void send(@NotNull T packet) throws IOException {
        LOGGER.trace("Sending packet: {}", packet);
        send(RawPacket.createFrom(packet));
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        channel.close();
    }

    @NotNull
    public Channel getChannel() {
        return channel;
    }
}
