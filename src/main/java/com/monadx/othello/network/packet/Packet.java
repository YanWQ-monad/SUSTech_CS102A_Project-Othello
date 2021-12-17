package com.monadx.othello.network.packet;

import java.io.DataOutputStream;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public abstract class Packet<T extends PacketListener> {
    abstract public void serialize(@NotNull DataOutputStream stream) throws IOException;

    abstract public void handle(@NotNull T listener) throws IOException;

    abstract public int getPacketId();
}
