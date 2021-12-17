package com.monadx.othello.ai.searcher;

import com.monadx.othello.ai.utils.SearchResult;
import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public abstract class Searcher {
    public abstract SearchResult search(Board board, ChessColor color, int progress);
}
