package com.monadx.othello.testutils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WrapThread<T> {
    @NotNull private final RunnableWithException<T> runnable;
    @NotNull private final Thread thread;

    @Nullable private Exception exception;
    @Nullable private T result;

    public WrapThread(@NotNull RunnableWithException<T> runnable) {
        this.runnable = runnable;

        thread = new Thread(() -> {
            try {
                result = runnable.run();
            } catch (Exception e) {
                exception = e;
            }
        });
    }

    public void start() {
        thread.start();
    }

    public T join() throws Exception {
        thread.join();
        if (exception != null) {
            throw exception;
        }
        return result;
    }

    @FunctionalInterface
    public interface RunnableWithException<T> {
         T run() throws Exception;
    }
}
