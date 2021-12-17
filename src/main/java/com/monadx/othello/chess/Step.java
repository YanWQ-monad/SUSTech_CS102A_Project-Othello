package com.monadx.othello.chess;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Step(@NotNull ChessColor player, int x, int y) {
    @NotNull
    @Contract("_ -> new")
    public static Step deserialize(@NotNull DataInput in) throws IOException {
        ChessColor player = ChessColor.deserialize(in);
        int x = in.readByte();
        int y = in.readByte();
        if (!Utils.coordinateInside(x, y)) {
            throw new IOException("Invalid coordinate: " + x + "," + y);
        }
        return new Step(player, x, y);
    }

    public void serialize(@NotNull DataOutput out) throws IOException {
        player.serialize(out);
        out.writeByte(x);
        out.writeByte(y);
    }
}
