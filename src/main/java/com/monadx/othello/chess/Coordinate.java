package com.monadx.othello.chess;

import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public record Coordinate(int x, int y) {
    @NotNull
    public static Coordinate of(int x, int y) {
        if (!Utils.coordinateInside(x, y)) {
            throw new IllegalArgumentException("Coordinate is not inside the board");
        }
        return COORDINATES[x][y];
    }

    public byte encode() {
        return (byte)(x * 8 + y);
    }

    @NotNull
    public static Coordinate decode(byte value) {
        int encoded = value & 0x3F;
        return of(encoded / 8, encoded % 8);
    }

    @NotNull
    public static Coordinate deserialize(@NotNull DataInput in) throws IOException {
        byte encoded = in.readByte();
        return Coordinate.decode(encoded);
    }

    public void serialize(@NotNull DataOutput out) throws IOException {
        out.writeByte(encode());
    }

    public int component1() {
        return x;
    }

    public int component2() {
        return y;
    }

    @NotNull private static final Coordinate[][] COORDINATES;
    @NotNull public static final Coordinate NULL_PLACEHOLDER = new Coordinate(-1, -1);

    static {
        COORDINATES = new Coordinate[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                COORDINATES[i][j] = new Coordinate(i, j);
            }
        }
    }
}
