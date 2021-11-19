package com.monadx.othello.chess;

public class Board {
    private final ChessColor[][] board;
    private ChessColor currentPlayer;

    private Integer hash = null;

    private static final int[] DX = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] DY = {-1, 0, 1, -1, 1, -1, 0, 1};

    public Board() {
        board = new ChessColor[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board[x][y] = ChessColor.EMPTY;
            }
        }
        board[3][3] = ChessColor.WHITE;
        board[4][4] = ChessColor.WHITE;
        board[3][4] = ChessColor.BLACK;
        board[4][3] = ChessColor.BLACK;

        currentPlayer = ChessColor.BLACK;
    }

    // Checks whether the given color of the chess can place on the position
    public boolean checkPlaceable(Coordinate coordinate) {
        if (board[coordinate.x()][coordinate.y()] != ChessColor.EMPTY)
            return false;

        for (int k = 0; k < 8; k++) {
            int nx = coordinate.x() + DX[k], ny = coordinate.y() + DY[k];
            if (!Utils.coordinateInside(nx, ny) || board[nx][ny] != currentPlayer.getOpposite())
                continue;
            while (true) {
                nx += DX[k];
                ny += DY[k];
                if (!Utils.coordinateInside(nx, ny) || board[nx][ny] == ChessColor.EMPTY)
                    break;
                if (board[nx][ny] == currentPlayer)
                    return true;
            }
        }

        return false;
    }

    // Place a chess on the position
    // Return true if the chess is placed successfully
    public boolean place(Coordinate coordinate) {
        if (board[coordinate.x()][coordinate.y()] != ChessColor.EMPTY)
            return false;

        boolean ok = false;

        for (int k = 0; k < 8; k++) {
            int nx = coordinate.x() + DX[k], ny = coordinate.y() + DY[k];
            if (!Utils.coordinateInside(nx, ny) || board[nx][ny] != currentPlayer.getOpposite())
                continue;
            while (true) {
                nx += DX[k];
                ny += DY[k];
                if (!Utils.coordinateInside(nx, ny) || board[nx][ny] == ChessColor.EMPTY)
                    break;
                if (board[nx][ny] == currentPlayer) {
                    while (nx != coordinate.x() || ny != coordinate.y()) {
                        nx -= DX[k];
                        ny -= DY[k];
                        board[nx][ny] = currentPlayer;
                    }
                    ok = true;
                    break;
                }
            }
        }

        if (ok) {
            hash = null;
            currentPlayer = currentPlayer.getOpposite();
        }

        return ok;
    }

    @Override
    public int hashCode() {
        if (hash == null) {
            int h = 0;
            for (int i = 0; i < 8; i++) {
                int row_hash = 0;
                for (int j = 0; j < 8; j++) {
                    row_hash = row_hash * 3 + board[i][j].hashCode();
                }
                h = h * 6151 + (h >> 20) + row_hash;
            }
            hash = h;
        }
        return hash;
    }

    public ChessColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ChessColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ChessColor[][] getBoard() {
        return board;
    }

    // Directly set the board
    public void setBoard(ChessColor[][] board) {
        hash = null;
        for (int x = 0; x < 8; x++) {
            System.arraycopy(board[x], 0, this.board[x], 0, 8);
        }
    }
}