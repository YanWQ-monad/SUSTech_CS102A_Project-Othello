package com.monadx.othello.network.utils;

import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class VarInt {
    public static int readVarInt(@NotNull DataInput stream) throws IOException {
        int out = 0;
        int shift = 0;
        byte b;
        do {
            b = stream.readByte();
            out |= (b & 0x7F) << shift;
            shift += 7;
        } while ((b & 0x80) != 0);
        return out;
    }

    public static void writeVarInt(@NotNull DataOutput stream, int value) throws IOException {
        while ((value & ~0x7F) != 0) {
            stream.writeByte((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        stream.writeByte((byte) value);
    }
}
