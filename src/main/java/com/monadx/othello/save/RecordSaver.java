package com.monadx.othello.save;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

import com.monadx.othello.chess.Game;

public class RecordSaver {
    @NotNull private final String fileName;

    public RecordSaver(@NotNull String fileName) {
        this.fileName = fileName;
    }

    public void save(@NotNull Game game) throws SaveException {
        GameRecord record = GameRecord.fromGame(game);

        try {
            DataOutputStream stream = new DataOutputStream(new FileOutputStream(fileName));
            record.serialize(stream);
        } catch (IOException e) {
            throw new SaveException(e.getMessage(), e);
        }
    }
}
