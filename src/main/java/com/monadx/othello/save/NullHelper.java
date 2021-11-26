package com.monadx.othello.save;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NullHelper {
    public static <T> T deserializeNullable(DataInput in, IOFunction<DataInput, T> function) throws IOException {
        boolean hasValue = in.readBoolean();
        if (hasValue) {
            return function.apply(in);
        } else {
            return null;
        }
    }

    public static <T> void serializeNullable(DataOutput out, T value, IOConsumer<DataOutput> function) throws IOException {
        if (value == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            function.apply(out);
        }
    }

    @FunctionalInterface
    public interface IOFunction<T, R> {
       R apply(T t) throws IOException;
    }

    @FunctionalInterface
    public interface IOConsumer<T> {
       void apply(T t) throws IOException;
    }
}
