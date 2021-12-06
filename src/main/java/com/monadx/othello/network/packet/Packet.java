package com.monadx.othello.network.packet;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet<T extends PacketListener> {
    abstract public void serialize(DataOutputStream stream) throws IOException;

    abstract public void handle(T listener) throws IOException;

    abstract public int getPacketId();
}
