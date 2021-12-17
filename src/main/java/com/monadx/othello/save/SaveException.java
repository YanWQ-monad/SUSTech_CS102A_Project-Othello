package com.monadx.othello.save;

import org.jetbrains.annotations.NotNull;

public class SaveException extends Exception {
    public SaveException(@NotNull String message) {
        super(message);
    }

    public SaveException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
