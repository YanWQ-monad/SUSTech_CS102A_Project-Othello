package com.monadx.othello.network.packet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;

import com.monadx.othello.network.connection.Channel;

public class PacketStream implements Closeable {
    private final static Logger LOGGER = LogManager.getLogger(PacketStream.class);

    DataInputStream in;
    DataOutputStream out;
    Channel channel;

    public PacketStream(Channel channel) {
        this.in = new DataInputStream(channel.input());
        this.out = new DataOutputStream(channel.output());
        this.channel = channel;
    }

    public RawPacket read() throws IOException {
        return RawPacket.readFrom(in);
    }

    private void send(RawPacket packet) throws IOException {
        packet.writeTo(out);
    }

    public <T extends Packet<L>, L extends PacketListener> void send(T packet) throws IOException {
        LOGGER.trace("Sending packet: {}", packet);
        send(RawPacket.createFrom(packet));
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        channel.close();
    }

    public Channel getChannel() {
        return channel;
    }
}
