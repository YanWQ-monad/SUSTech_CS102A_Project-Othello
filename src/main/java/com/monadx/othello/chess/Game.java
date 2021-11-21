package com.monadx.othello.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private final Board board = new Board();
    private ChessColor currentPlayer = ChessColor.BLACK;

    private GameStatus status = GameStatus.PLAYING;

    // only set when the game is ended
    // if `winner` is null when the game is ended, it means it's a draw
    private ChessColor winner = null;

    private List<Step> stepList = new ArrayList<>();
    private List<Snapshot> snapshotList = new ArrayList<>();

    public void reset() {
        board.reset();
        currentPlayer = ChessColor.BLACK;
        status = GameStatus.PLAYING;
        winner = null;
        stepList.clear();
        snapshotList.clear();
    }

    // Checks whether the given color of the chess can place on the position
    public boolean checkPlaceable(Coordinate coordinate) {
        return status != GameStatus.ENDED && board.checkPlaceable(coordinate, currentPlayer);
    }

    // Place a chess on the position
    // Return true if the chess is placed successfully
    public boolean place(Coordinate coordinate) {
        assert status == GameStatus.PLAYING;

        snapshotList.add(new Snapshot(board.copy(), currentPlayer, status));

        boolean result = board.place(coordinate, currentPlayer);
        if (result) {
            stepList.add(new Step(currentPlayer, coordinate.x(), coordinate.y()));

            nextTurn();
        } else {
            snapshotList.remove(snapshotList.size() - 1);
        }
        return result;
    }

    // Undo the last step
    public void undo() {
        assert status == GameStatus.PLAYING;

        if (stepList.size() == 0) {
            return;
        }

        stepList.remove(stepList.size() - 1);
        Snapshot snapshot = snapshotList.remove(snapshotList.size() - 1);

        board.setBoard(snapshot.board().getBoard());
        currentPlayer = snapshot.currentPlayer();
        status = snapshot.status();
    }

    public int getPlacedCount() {
        return board.getPlacedCount();
    }

    private void nextTurn() {
        currentPlayer = currentPlayer.getOpposite();

        boolean placeable = Arrays.stream(Utils.POSITION_LIST)
                .anyMatch(coordinate -> board.checkPlaceable(coordinate, currentPlayer));
        if (!placeable) {
            // If the current player cannot make a move,
            // then pass the turn to the opponent
            currentPlayer = currentPlayer.getOpposite();

            boolean placeable2 = Arrays.stream(Utils.POSITION_LIST)
                    .anyMatch(coordinate -> board.checkPlaceable(coordinate, currentPlayer));
            if (!placeable2) {
                // If the opponent cannot make a move, then the game is ended
                status = GameStatus.ENDED;
                winner = calculateWinner();
            }
        }
    }

    private ChessColor calculateWinner() {
        assert status == GameStatus.ENDED;
        int black = 0, white = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessColor color = board.getBoard()[x][y];
                if (color == ChessColor.BLACK) {
                    black++;
                } else if (color == ChessColor.WHITE) {
                    white++;
                }
            }
        }

        if (black > white) {
            return ChessColor.BLACK;
        } else if (white > black) {
            return ChessColor.WHITE;
        } else {
            return null;
        }
    }

    public Board getBoard() {
        return board;
    }

    public ChessColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ChessColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ChessColor getWinner() {
        return winner;
    }

    public GameStatus getStatus() {
        return status;
    }
}
