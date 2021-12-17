package com.monadx.othello.ai.difficulty;

import com.monadx.othello.ai.evaluate.Evaluator;
import com.monadx.othello.ai.searcher.Searcher;
import com.monadx.othello.ai.utils.SearchResult;
import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public abstract class Difficulty {
    Evaluator evaluator;
    Searcher searcher;

    public SearchResult searchBestMove(Board board, ChessColor color, int progress) {
        return searcher.search(board, color, progress);
    }

    public Evaluator.Result evaluateBoard(Board board, int progress) {
        return evaluator.evaluate(board, progress);
    }
}
