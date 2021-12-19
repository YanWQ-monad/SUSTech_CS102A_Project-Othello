package com.monadx.othello.network.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.monadx.othello.TestHelper;

class VarIntTest {
    @Test
    void testSerialize() {
        byte[] serialized = TestHelper.serialize(stream -> VarInt.writeVarInt(stream, 1286610189));
        assertEquals(TestHelper.deserialize(serialized, VarInt::readVarInt), 1286610189);
        assertEquals(serialized.length, 5);

        byte[] serialized2 = TestHelper.serialize(stream -> VarInt.writeVarInt(stream, 590049));
        assertEquals(TestHelper.deserialize(serialized2, VarInt::readVarInt), 590049);
        assertEquals(serialized2.length, 3);

        byte[] serialized3 = TestHelper.serialize(stream -> VarInt.writeVarInt(stream, 122));
        assertEquals(TestHelper.deserialize(serialized3, VarInt::readVarInt), 122);
        assertEquals(serialized3.length, 1);

        byte[] serialized4 = TestHelper.serialize(stream -> VarInt.writeVarInt(stream, 0));
        assertEquals(TestHelper.deserialize(serialized4, VarInt::readVarInt), 0);
        assertEquals(serialized4.length, 1);
    }
}
