package com.monadx.othello.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Game {
    @NotNull private final Board board = new Board();
    @Nullable private ChessColor currentPlayer = ChessColor.BLACK;

    @NotNull private Status status = Status.PLAYING;

    // only set when the game is ended
    // if `winner` is null when the game is ended, it means it's a draw
    @Nullable private ChessColor winner = null;

    @NotNull private final List<Step> stepList = new ArrayList<>();
    @NotNull private final List<Snapshot> snapshotList = new ArrayList<>();

    public void reset() {
        board.reset();
        currentPlayer = ChessColor.BLACK;
        status = Status.PLAYING;
        winner = null;
        stepList.clear();
        snapshotList.clear();
    }

    public void loadFrom(@NotNull Game game) {
        board.loadFrom(game.board);
        currentPlayer = game.currentPlayer;
        status = game.status;
        winner = game.winner;
        stepList.clear();
        stepList.addAll(game.stepList);
        snapshotList.clear();
        snapshotList.addAll(game.snapshotList);
    }

    // Checks whether the given color of the chess can place on the position
    public boolean checkPlaceable(@NotNull Coordinate coordinate) {
        return status != Status.ENDED && currentPlayer != null && board.checkPlaceable(coordinate, currentPlayer);
    }

    // Place a chess on the position
    // Return true if the chess is placed successfully
    public boolean place(@NotNull Coordinate coordinate) {
        assert status == Status.PLAYING;
        assert currentPlayer != null;

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
        assert status == Status.PLAYING;

        if (stepList.size() == 0) {
            return;
        }

        stepList.remove(stepList.size() - 1);
        Snapshot snapshot = snapshotList.remove(snapshotList.size() - 1);

        board.loadFrom(snapshot.board());
        currentPlayer = snapshot.currentPlayer();
        status = snapshot.status();
    }

    public int getPlacedCount() {
        return board.getPlacedCount();
    }

    private void nextTurn() {
        assert currentPlayer != null;
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
                status = Status.ENDED;
                currentPlayer = null;
                winner = calculateWinner();
            }
        }
    }

    @Nullable
    private ChessColor calculateWinner() {
        assert status == Status.ENDED;
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

    @NotNull
    public Board getBoard() {
        return board;
    }

    @Nullable
    public ChessColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(@Nullable ChessColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Nullable
    public ChessColor getWinner() {
        return winner;
    }

    @NotNull
    public Status getStatus() {
        return status;
    }

    @NotNull
    public List<Step> getStepList() {
        return stepList;
    }

    @NotNull
    public List<Snapshot> getSnapshotList() {
        return snapshotList;
    }

    public enum Status {
        PLAYING,
        ENDED,
    }
}
