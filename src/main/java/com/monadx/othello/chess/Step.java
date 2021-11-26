package com.monadx.othello.chess;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public record Step(ChessColor player, int x, int y) {
    public static Step deserialize(DataInput in) throws IOException {
        ChessColor player = ChessColor.deserialize(in);
        int x = in.readByte();
        int y = in.readByte();
        if (!Utils.coordinateInside(x, y)) {
            throw new IOException("Invalid coordinate: " + x + "," + y);
        }
        return new Step(player, x, y);
    }

    public void serialize(DataOutput out) throws IOException {
        player.serialize(out);
        out.writeByte(x);
        out.writeByte(y);
    }
}
