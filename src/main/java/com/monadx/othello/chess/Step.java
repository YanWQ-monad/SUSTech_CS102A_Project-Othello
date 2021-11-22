package com.monadx.othello.chess;

import java.io.Serial;
import java.io.Serializable;

public record Step(ChessColor player, int x, int y) implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
}
