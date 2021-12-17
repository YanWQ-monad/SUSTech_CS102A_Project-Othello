package com.monadx.othello.ui.model

import java.util.function.Supplier

import com.monadx.othello.ai.difficulty.Difficulty
import com.monadx.othello.ai.difficulty.EasyDifficulty
import com.monadx.othello.ai.difficulty.NaiveDifficulty
import com.monadx.othello.ai.difficulty.NormalDifficulty

enum class Difficulties(val displayText: String, val createDifficultyFactory: Supplier<Difficulty>) {
    NAIVE("Naive", { NaiveDifficulty() }),
    EASY("Easy", { EasyDifficulty() }),
    NORMAL("Normal", { NormalDifficulty() });

    fun createDifficulty(): Difficulty {
        return createDifficultyFactory.get()
    }
}
