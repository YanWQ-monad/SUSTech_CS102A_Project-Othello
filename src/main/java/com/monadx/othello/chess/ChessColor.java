package com.monadx.othello.chess;

import java.io.Serial;
import java.io.Serializable;

public enum ChessColor implements Serializable {
    EMPTY(0),
    BLACK(1),
    WHITE(2);

    private final int id;

    ChessColor(int id) {
        this.id = id;
    }

    @Serial private static final long serialVersionUID = 1L;

    public ChessColor getOpposite() {
        return switch (this) {
            case BLACK -> WHITE;
            case WHITE -> BLACK;
            case EMPTY -> EMPTY;
        };
    }

    public int getId() {
        return id;
    }
}
