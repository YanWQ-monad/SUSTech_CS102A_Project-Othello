package com.monadx.othello.network.request;

public interface RequestListener {
    void onUndoRequest(RequestManager.RequestResultConsumer onResponse);

    void onRestartRequest(RequestManager.RequestResultConsumer onResponse);
}
