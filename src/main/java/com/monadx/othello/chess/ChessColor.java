package com.monadx.othello.chess;

import java.io.Serial;
import java.io.Serializable;

public enum ChessColor implements Serializable {
    EMPTY,
    BLACK,
    WHITE;

    @Serial private static final long serialVersionUID = 1L;

    public ChessColor getOpposite() {
        return switch (this) {
            case BLACK -> WHITE;
            case WHITE -> BLACK;
            case EMPTY -> EMPTY;
        };
    }

    public int customHashcode() {
        return switch (this) {
            case BLACK -> 0;
            case WHITE -> 1;
            case EMPTY -> 2;
        };
    }
}
