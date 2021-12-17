package com.monadx.othello.ai.utils;

import com.monadx.othello.ai.evaluate.Evaluator;
import com.monadx.othello.chess.Coordinate;

public record SearchResult(Coordinate best, Evaluator.Result score) {}
