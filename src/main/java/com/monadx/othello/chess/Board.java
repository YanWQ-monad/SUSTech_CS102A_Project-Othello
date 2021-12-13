package com.monadx.othello.chess;

import java.util.Random;

public class Board {
    private final ChessColor[][] board;

    private int hash;

    private static final int[] DX = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] DY = {-1, 0, 1, -1, 1, -1, 0, 1};

    public Board() {
        board = new ChessColor[8][8];
        reset();
    }

    public void reset() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board[x][y] = ChessColor.EMPTY;
            }
        }
        board[3][3] = ChessColor.WHITE;
        board[4][4] = ChessColor.WHITE;
        board[3][4] = ChessColor.BLACK;
        board[4][3] = ChessColor.BLACK;

        hash = ZobristHashing.EMPTY_HASH
                ^ ZobristHashing.HASH[3][3][ChessColor.WHITE.getId()] ^ ZobristHashing.HASH[3][3][ChessColor.EMPTY.getId()]
                ^ ZobristHashing.HASH[4][4][ChessColor.WHITE.getId()] ^ ZobristHashing.HASH[4][4][ChessColor.EMPTY.getId()]
                ^ ZobristHashing.HASH[3][4][ChessColor.BLACK.getId()] ^ ZobristHashing.HASH[3][4][ChessColor.EMPTY.getId()]
                ^ ZobristHashing.HASH[4][3][ChessColor.BLACK.getId()] ^ ZobristHashing.HASH[4][3][ChessColor.EMPTY.getId()];
    }

    public Board copy() {
        Board clone = new Board();
        clone.setBoard(board);
        clone.hash = hash;
        return clone;
    }

    public void loadFrom(Board board) {
        setBoard(board.board);
        hash = board.hash;
    }

    // Checks whether the given color of the chess can place on the position
    public boolean checkPlaceable(Coordinate coordinate, ChessColor color) {
        if (board[coordinate.x()][coordinate.y()] != ChessColor.EMPTY)
            return false;

        for (int k = 0; k < 8; k++) {
            int nx = coordinate.x() + DX[k], ny = coordinate.y() + DY[k];
            boolean ok = false;

            while (Utils.coordinateInside(nx, ny) && board[nx][ny] == color.getOpposite()) {
                nx += DX[k];
                ny += DY[k];
                ok = true;
            }

            if (Utils.coordinateInside(nx, ny) && ok && board[nx][ny] == color)
                return true;
        }

        return false;
    }

    // Place a chess on the position
    // Return true if the chess is placed successfully
    public boolean place(Coordinate coordinate, ChessColor color) {
        int x = coordinate.x(), y = coordinate.y();
        if (board[x][y] != ChessColor.EMPTY)
            return false;

        boolean ok = false;
        int hash = this.hash;

        for (int k = 0; k < 8; k++) {
            int nx = x + DX[k], ny = y + DY[k];
            while (Utils.coordinateInside(nx, ny) && board[nx][ny] == color.getOpposite()) {
                nx += DX[k];
                ny += DY[k];
            }

            if (!Utils.coordinateInside(nx, ny) || board[nx][ny] != color)
                continue;

            ok = true;
            while (nx != x || ny != y) {
                nx -= DX[k];
                ny -= DY[k];

                hash ^= ZobristHashing.HASH[nx][ny][board[nx][ny].getId()] ^ ZobristHashing.HASH[nx][ny][color.getId()];

                board[nx][ny] = color;
            }
        }

        this.hash = hash;

        return ok;
    }

    public int getPlacedCount() {
        int count = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] != ChessColor.EMPTY)
                    count++;
            }
        }
        return count;
    }

    public void forceFlip(Coordinate coordinate) {
        int x = coordinate.x(), y = coordinate.y();
        ChessColor nextColor = switch (board[x][y]) {
            case EMPTY -> ChessColor.BLACK;
            case BLACK -> ChessColor.WHITE;
            case WHITE -> ChessColor.EMPTY;
        };

        this.hash ^= ZobristHashing.HASH[x][y][board[x][y].getId()] ^ ZobristHashing.HASH[x][y][nextColor.getId()];

        board[x][y] = nextColor;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    public ChessColor[][] getBoard() {
        return board;
    }

    // This method does not maintain `hash`, please take care of it
    private void setBoard(ChessColor[][] board) {
        for (int x = 0; x < 8; x++) {
            System.arraycopy(board[x], 0, this.board[x], 0, 8);
        }
    }

    private static class ZobristHashing {
        private static final int[][][] HASH = new int[8][8][3];
        private static final int EMPTY_HASH;

        static {
            // use a constant seed to make sure the hash won't change after restart.
            Random random = new Random(995085);

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 3; k++) {
                        HASH[i][j][k] = random.nextInt();
                    }
                }
            }

            int emptyHash = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    emptyHash ^= HASH[i][j][ChessColor.EMPTY.getId()];
                }
            }
            EMPTY_HASH = emptyHash;
        }
    }
}
