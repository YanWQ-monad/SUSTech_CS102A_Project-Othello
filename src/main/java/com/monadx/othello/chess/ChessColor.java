package com.monadx.othello.chess;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public enum ChessColor {
    EMPTY(0),
    BLACK(1),
    WHITE(2);

    private final int id;

    ChessColor(int id) {
        this.id = id;
    }

    @NotNull
    public ChessColor getOpposite() {
        return switch (this) {
            case BLACK -> WHITE;
            case WHITE -> BLACK;
            case EMPTY -> EMPTY;
        };
    }

    @NotNull
    public static ChessColor deserialize(@NotNull DataInput in) throws IOException {
        return switch (in.readByte()) {
            case 0 -> EMPTY;
            case 1 -> BLACK;
            case 2 -> WHITE;
            default -> throw new IOException("Invalid ChessColor id: " + in.readByte());
        };
    }

    public void serialize(@NotNull DataOutput out) throws IOException {
        out.writeByte(id);
    }

    public int getId() {
        return id;
    }
}
