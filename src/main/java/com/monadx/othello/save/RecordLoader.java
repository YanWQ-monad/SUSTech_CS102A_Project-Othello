package com.monadx.othello.save;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.chess.*;

public class RecordLoader {
    @NotNull private final String fileName;

    public RecordLoader(@NotNull String fileName) {
        this.fileName = fileName;
    }

    @NotNull
    @Contract(" -> new")
    public Game load() throws SaveException {
        GameRecord record;
        try {
            DataInputStream stream = new DataInputStream(new FileInputStream(fileName));
            record = GameRecord.deserialize(stream);
        } catch (IOException e) {
            throw new SaveException(e.getMessage(), e);
        }

        Game game = new Game();

        for (Step step : record.stepList()) {
            ChessColor player = step.player();
            if (player != game.getCurrentPlayer()) {
                throw new SaveException(String.format("Invalid player: %s, expected: %s", player, game.getCurrentPlayer()));
            }

            game.place(Coordinate.of(step.x(), step.y()));
        }

        if (game.getBoard().hashCode() != record.boardHash()) {
            throw new SaveException(String.format("Invalid hash code: %s, expected: %s", game.getBoard().hashCode(), record.boardHash()));
        }
        if (game.getCurrentPlayer() != record.currentPlayer()) {
            throw new SaveException(String.format("Invalid current player: %s, expected: %s", game.getCurrentPlayer(), record.currentPlayer()));
        }

        Game.Status expectedStatus = record.currentPlayer() == null ? Game.Status.ENDED : Game.Status.PLAYING;
        if (game.getStatus() != expectedStatus) {
            throw new SaveException(String.format("Invalid game status: %s, expected: %s", game.getStatus(), expectedStatus));
        }

        return game;
    }
}
