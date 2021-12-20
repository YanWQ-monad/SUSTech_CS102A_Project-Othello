package com.monadx.othello.testutils;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class PairedStream implements Closeable {
    @NotNull private final ThreadPipedInputStream inputStream;
    @NotNull private final PipedOutputStream outputStream;

    public PairedStream() {
        this.inputStream = new ThreadPipedInputStream();
        this.outputStream = new PipedOutputStream();

        try {
            this.outputStream.connect(this.inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Impossible IOException", e);
        }
    }

    public void maintainThread() {
        this.inputStream.maintainThread();
    }

    @NotNull
    public InputStream getInputStream() {
        return inputStream;
    }

    @NotNull
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
        this.outputStream.close();
    }
}
