package com.monadx.othello.chess;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.monadx.othello.TestHelper;

class StepTest {
    private static final Logger LOGGER = LogManager.getLogger(StepTest.class);

    @Test
    void testSerialize1() {
        Step step = new Step(ChessColor.BLACK, 1, 5);
        byte[] serialized = TestHelper.serialize(step::serialize);
        Step recover = TestHelper.deserialize(serialized, Step::deserialize);
        assertEquals(step, recover);
    }

    @Test
    void testSerialize2() {
        Step step = new Step(ChessColor.WHITE, 7, 3);
        byte[] serialized = TestHelper.serialize(step::serialize);
        Step recover = TestHelper.deserialize(serialized, Step::deserialize);
        assertEquals(step, recover);
    }
}
