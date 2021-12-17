package com.monadx.othello.network.request;

import org.jetbrains.annotations.NotNull;

public interface RequestListener {
    void onUndoRequest(@NotNull RequestManager.RequestResultConsumer onResponse);

    void onRestartRequest(@NotNull RequestManager.RequestResultConsumer onResponse);
}
