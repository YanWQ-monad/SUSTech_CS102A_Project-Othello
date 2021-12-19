package com.monadx.othello;

import java.io.*;
import org.jetbrains.annotations.NotNull;

public class TestHelper {
    @NotNull
    public static byte[] serialize(@NotNull SerializeFunction func) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(byteStream);
            func.serialize(stream);
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static <T> T deserialize(@NotNull byte[] bytes, @NotNull DeserializeFunction<T> func) {
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            DataInputStream stream = new DataInputStream(byteStream);
            return func.deserialize(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface SerializeFunction {
        void serialize(@NotNull DataOutput t) throws IOException;
    }

    @FunctionalInterface
    public interface DeserializeFunction<T> {
        @NotNull
        T deserialize(@NotNull DataInput t) throws IOException;
    }
}
