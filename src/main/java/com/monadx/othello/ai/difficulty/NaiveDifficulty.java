package com.monadx.othello.ai.difficulty;

import com.monadx.othello.ai.evaluate.NaiveEvaluator;
import com.monadx.othello.ai.searcher.MinimaxSearcher;

public class NaiveDifficulty extends Difficulty {
    public NaiveDifficulty() {
        evaluator = new NaiveEvaluator();
        searcher = new MinimaxSearcher(evaluator, 6);
    }
}
