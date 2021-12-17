package com.monadx.othello.ai.difficulty;

import com.monadx.othello.ai.evaluate.GreedyEvaluator;
import com.monadx.othello.ai.searcher.MinimaxSearcher;

public class EasyDifficulty extends Difficulty {
    public EasyDifficulty() {
        evaluator = new GreedyEvaluator();
        searcher = new MinimaxSearcher(evaluator, 6);
    }
}
