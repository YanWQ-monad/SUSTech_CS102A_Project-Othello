package com.monadx.othello.save;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.monadx.othello.chess.Game;

public class RecordSaver {
    private final String fileName;

    public RecordSaver(String fileName) {
        this.fileName = fileName;
    }

    public void save(Game game) throws SaveException {
        GameRecord record = new GameRecord(
                game.getStepList(),
                game.getBoard().hashCode(),
                game.getCurrentPlayer());

        try {
            DataOutputStream stream = new DataOutputStream(new FileOutputStream(fileName));
            record.serialize(stream);
        } catch (IOException e) {
            throw new SaveException(e.getMessage(), e);
        }
    }
}
