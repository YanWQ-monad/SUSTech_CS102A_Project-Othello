package com.monadx.othello.ai.utils;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.ai.evaluate.Evaluator;
import com.monadx.othello.chess.Coordinate;

public record SearchResult(@NotNull Coordinate best, @NotNull Evaluator.Result score) {}
