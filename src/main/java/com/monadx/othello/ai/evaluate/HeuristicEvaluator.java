package com.monadx.othello.ai.evaluate;

import com.monadx.othello.ai.utils.BoardExtends;
import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public class HeuristicEvaluator extends Evaluator {
    @Override
    public int evaluateColor(Board board, ChessColor color, int progress) {
        BoardExtends boardExtends = new BoardExtends(board);

        int mobility = evalMobility(boardExtends, color);
        int disc = evalDisc(boardExtends, color);
        int corner = evalCorner(boardExtends, color);
        int weight = boardExtends.weightedCellScore(color);

        return 300 * corner + 20 * mobility + disc * 3 + weight;
    }

    @Override
    public Result correctForfeit(Board board, int progress, Result result, ChessColor color) {
        return correctResult(result, color, -100);
    }

    int evalMobility(BoardExtends board, ChessColor color) {
        int myMobility = board.mobility(color);
        int opponentMobility = board.mobility(color.getOpposite());

        return 100 * (myMobility - opponentMobility) / (myMobility + opponentMobility + 1);
    }

    int evalDisc(BoardExtends board, ChessColor color) {
        int myDisc = board.disc(color);
        int opponentDisc = board.disc(color.getOpposite());

        return 100 * (myDisc - opponentDisc) / (myDisc + opponentDisc);
    }

    int evalCorner(BoardExtends board, ChessColor color) {
        int myCorner = board.corner(color);
        int opponentCorner = board.corner(color.getOpposite());

        return 100 * (myCorner - opponentCorner) / (myCorner + opponentCorner + 1);
    }
}
