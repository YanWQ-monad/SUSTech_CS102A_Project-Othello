package com.monadx.othello.save;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.chess.Game;
import com.monadx.othello.chess.Step;

public record GameRecord(
        @NotNull List<Step> stepList,
        int boardHash,
        @Nullable ChessColor currentPlayer
) {
    @NotNull
    @Contract("_ -> new")
    public static GameRecord fromGame(@NotNull Game game) {
        return new GameRecord(
                game.getStepList(),
                game.getBoard().hashCode(),
                game.getCurrentPlayer());
    }

    @NotNull
    @Contract("_ -> new")
    public static GameRecord deserialize(@NotNull DataInput in) throws IOException {
        int stepListSize = in.readByte();
        List<Step> stepList = new ArrayList<>(stepListSize);

        for (int i = 0; i < stepListSize; i++) {
            stepList.add(Step.deserialize(in));
        }

        int boardHash = in.readInt();
        ChessColor currentPlayer = NullHelper.deserializeNullable(in, ChessColor::deserialize);

        return new GameRecord(stepList, boardHash, currentPlayer);
    }

    public void serialize(@NotNull DataOutput out) throws IOException {
        out.writeByte(stepList.size());

        for (Step step : stepList) {
            step.serialize(out);
        }

        out.writeInt(boardHash);

        // Lambda can NOT be replaced with method reference since `currentPlayer` is nullable.
        NullHelper.serializeNullable(out, currentPlayer, o -> currentPlayer.serialize(o));
    }
}
