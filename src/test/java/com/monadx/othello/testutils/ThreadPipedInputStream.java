package com.monadx.othello.testutils;

import java.io.PipedInputStream;
import java.lang.reflect.Field;

public class ThreadPipedInputStream extends PipedInputStream {
    private static Field readSideField;
    private static Field writeSideField;

    public void maintainThread() {
        try {
            // f**k `PipedInputStream`, it should not check the thread.

            // `PipedInputStream` only allows one thread to read or write from it.
            // But sometimes, we need to pass it between different threads,
            // so we need a bit of hack to make it work.
            readSideField.set(this, Thread.currentThread());
            writeSideField.set(this, Thread.currentThread());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            Class<?> pipedInputStreamClass = Class.forName("java.io.PipedInputStream");
            readSideField = pipedInputStreamClass.getDeclaredField("readSide");
            readSideField.setAccessible(true);
            writeSideField = pipedInputStreamClass.getDeclaredField("writeSide");
            writeSideField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
