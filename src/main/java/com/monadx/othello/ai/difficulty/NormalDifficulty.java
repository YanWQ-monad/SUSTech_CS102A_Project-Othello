package com.monadx.othello.ai.difficulty;

import com.monadx.othello.ai.evaluate.HeuristicEvaluator;
import com.monadx.othello.ai.searcher.MinimaxSearcher;

public class NormalDifficulty extends Difficulty {
    public NormalDifficulty() {
        evaluator = new HeuristicEvaluator();
        searcher = new MinimaxSearcher(evaluator);
    }
}
