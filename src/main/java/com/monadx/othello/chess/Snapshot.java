package com.monadx.othello.chess;

public record Snapshot(Board board, ChessColor currentPlayer, Game.Status status) {}
