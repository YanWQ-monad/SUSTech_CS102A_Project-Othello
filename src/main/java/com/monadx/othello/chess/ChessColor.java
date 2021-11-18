package com.monadx.othello.chess;

public enum ChessColor {
    EMPTY,
    BLACK,
    WHITE;

    public ChessColor getOpposite() {
        return switch (this) {
            case BLACK -> WHITE;
            case WHITE -> BLACK;
            case EMPTY -> EMPTY;
        };
    }
}
