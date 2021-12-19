package com.monadx.othello.save;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.monadx.othello.TestHelper;
import com.monadx.othello.chess.Coordinate;
import com.monadx.othello.chess.Game;

class GameRecordTest {
    @Test
    void testSerialize() {
        Game game = new Game();
        game.place(Coordinate.of(2, 3));
        game.place(Coordinate.of(4, 2));
        game.place(Coordinate.of(5, 4));
        game.place(Coordinate.of(1, 3));
        game.place(Coordinate.of(3, 2));
        game.place(Coordinate.of(4, 5));

        GameRecord record = GameRecord.fromGame(game);
        byte[] serialized = TestHelper.serialize(record::serialize);
        GameRecord recover = TestHelper.deserialize(serialized, GameRecord::deserialize);
        assertEquals(record, recover);
    }
}