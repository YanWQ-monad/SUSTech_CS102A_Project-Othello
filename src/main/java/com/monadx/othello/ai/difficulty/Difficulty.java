package com.monadx.othello.ai.difficulty;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.ai.evaluate.Evaluator;
import com.monadx.othello.ai.searcher.Searcher;
import com.monadx.othello.ai.utils.SearchResult;
import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public abstract class Difficulty {
    // If the subclass use other Evaluator and Searcher combination,
    // the two fields can be `null`.
    Evaluator evaluator;
    Searcher searcher;

    @NotNull
    public SearchResult searchBestMove(@NotNull Board board, @NotNull ChessColor color, int progress) {
        return searcher.search(board, color, progress);
    }

    @NotNull
    public Evaluator.Result evaluateBoard(@NotNull Board board, int progress) {
        return evaluator.evaluate(board, progress);
    }
}
