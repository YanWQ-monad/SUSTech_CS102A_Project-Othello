package com.monadx.othello.save;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.monadx.othello.chess.*;

public class RecordLoader {
    private final String fileName;

    public RecordLoader(String fileName) {
        this.fileName = fileName;
    }

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

            game.place(new Coordinate(step.x(), step.y()));
        }

        if (game.getBoard().hashCode() != record.boardHash()) {
            throw new SaveException(String.format("Invalid hash code: %s, expected: %s", game.getBoard().hashCode(), record.boardHash()));
        }
        if (game.getCurrentPlayer() != record.currentPlayer()) {
            throw new SaveException(String.format("Invalid current player: %s, expected: %s", game.getCurrentPlayer(), record.currentPlayer()));
        }

        GameStatus expectedStatus = record.currentPlayer() == null ? GameStatus.ENDED : GameStatus.PLAYING;
        if (game.getStatus() != expectedStatus) {
            throw new SaveException(String.format("Invalid game status: %s, expected: %s", game.getStatus(), expectedStatus));
        }

        return game;
    }
}
