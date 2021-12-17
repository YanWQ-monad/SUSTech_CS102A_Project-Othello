package com.monadx.othello.save;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NullHelper {
    public static <T> T deserializeNullable(@NotNull DataInput in, @NotNull IOFunction<DataInput, T> function) throws IOException {
        boolean hasValue = in.readBoolean();
        if (hasValue) {
            return function.apply(in);
        } else {
            return null;
        }
    }

    public static <T> void serializeNullable(@NotNull DataOutput out, @Nullable T value, @NotNull IOConsumer<DataOutput> function) throws IOException {
        if (value == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            function.apply(out);
        }
    }

    @FunctionalInterface
    public interface IOFunction<T, R> {
        @NotNull
        R apply(@NotNull T t) throws IOException;
    }

    @FunctionalInterface
    public interface IOConsumer<T> {
       void apply(@NotNull T t) throws IOException;
    }
}
