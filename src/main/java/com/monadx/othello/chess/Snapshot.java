package com.monadx.othello.chess;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Snapshot(@NotNull Board board, @Nullable ChessColor currentPlayer, @NotNull Game.Status status) {}
