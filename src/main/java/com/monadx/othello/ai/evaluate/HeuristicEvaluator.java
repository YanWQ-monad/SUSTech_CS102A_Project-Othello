package com.monadx.othello.ai.evaluate;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.ai.utils.BoardExtends;
import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public class HeuristicEvaluator extends Evaluator {
    @Override
    public int evaluateColor(@NotNull Board board, @NotNull ChessColor color, int progress) {
        BoardExtends boardExtends = new BoardExtends(board);

        int mobility = evalMobility(boardExtends, color);
        int disc = evalDisc(boardExtends, color);
        int corner = evalCorner(boardExtends, color);
        int weight = boardExtends.weightedCellScore(color);

        return 300 * corner + 20 * mobility + disc * 3 + weight;
    }

    @Override
    @NotNull
    public Result correctForfeit(@NotNull Board board, int progress, @NotNull Result result, @NotNull ChessColor color) {
        return correctResult(result, color, -100);
    }

    int evalMobility(@NotNull BoardExtends board, @NotNull ChessColor color) {
        int myMobility = board.mobility(color);
        int opponentMobility = board.mobility(color.getOpposite());

        return 100 * (myMobility - opponentMobility) / (myMobility + opponentMobility + 1);
    }

    int evalDisc(@NotNull BoardExtends board, @NotNull ChessColor color) {
        int myDisc = board.disc(color);
        int opponentDisc = board.disc(color.getOpposite());

        return 100 * (myDisc - opponentDisc) / (myDisc + opponentDisc);
    }

    int evalCorner(@NotNull BoardExtends board, @NotNull ChessColor color) {
        int myCorner = board.corner(color);
        int opponentCorner = board.corner(color.getOpposite());

        return 100 * (myCorner - opponentCorner) / (myCorner + opponentCorner + 1);
    }
}
