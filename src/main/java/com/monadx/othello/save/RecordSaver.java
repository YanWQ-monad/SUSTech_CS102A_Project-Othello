package com.monadx.othello.save;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fileName));
            stream.writeObject(record);
        } catch (IOException e) {
            throw new SaveException(e.getMessage(), e);
        }
    }
}
