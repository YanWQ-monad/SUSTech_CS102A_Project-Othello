package com.monadx.othello.chess;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Step(@NotNull ChessColor player, int x, int y) {
    public Step {
        if (player == ChessColor.EMPTY) {
            throw new IllegalArgumentException("player must not be EMPTY");
        }
        if (!Utils.coordinateInside(x, y)) {
            throw new IllegalArgumentException("x, y must be inside the board");
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static Step deserialize(@NotNull DataInput in) throws IOException {
        byte encoded = in.readByte();

        Coordinate coordinate = Coordinate.decode(encoded);
        int x = coordinate.x();
        int y = coordinate.y();

        int playerBit = (encoded >> 6) & 1;
        ChessColor player = switch (playerBit) {
            case 0 -> ChessColor.BLACK;
            case 1 -> ChessColor.WHITE;
            default -> throw new RuntimeException("Unreachable");
        };

        return new Step(player, x, y);
    }

    public void serialize(@NotNull DataOutput out) throws IOException {
        byte encoded = Coordinate.of(x, y).encode();
        int playerBit = player == ChessColor.BLACK ? 0 : 1;
        encoded |= (playerBit << 6);
        out.writeByte(encoded);
    }
}
