package com.monadx.othello.ai.searcher;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.ai.utils.SearchResult;
import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public abstract class Searcher {
    @NotNull
    public abstract SearchResult search(@NotNull Board board, @NotNull ChessColor color, int progress);
}
