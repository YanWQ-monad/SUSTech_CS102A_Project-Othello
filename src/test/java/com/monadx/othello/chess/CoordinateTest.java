package com.monadx.othello.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.monadx.othello.TestHelper;

class CoordinateTest {
    @Test
    void testSerialize() {
        Coordinate coordinate = Coordinate.of(5, 7);
        byte[] serialized = TestHelper.serialize(coordinate::serialize);
        Coordinate recover = TestHelper.deserialize(serialized, Coordinate::deserialize);
        assertEquals(coordinate, recover);
    }
}