package de.augsburg.hs.methoden.ki.algorithms.minimax;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;

/***
 * Max is always X, Min is always O
 *
 */
public class TicTacToeMiniMax {

    private final int MAX_WIN_SCORE = 10;
    private final int MIN_WIN_SCORE = -10;

    public CellOptions[][] generateEmptyGrid() {

        CellOptions[][] grid = new CellOptions[3][3];

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                grid[j][i] = CellOptions.EMPTY;
            }
        }

        return grid;
    }

    private boolean isGridFull(CellOptions[][] grid) {
        boolean isFull = true;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++){
                if(grid[j][i] == CellOptions.EMPTY){
                    isFull = false;
                    break;
                }
            }
        }

        return isFull;
    }

    private int evaluateWinCondition(CellOptions[][] grid) {

        // check each row for win
        for(int row = 0; row < 3; row++){
            if(grid[row][0] == grid[row][1] && grid[row][1] == grid[row][2]){
                if(grid[row][0] == CellOptions.MAX) {
                    return MAX_WIN_SCORE;
                } else if(grid[row][0] == CellOptions.MIN){
                    return MIN_WIN_SCORE;
                }
            }
        }

        // check each column for win
        for(int column = 0; column < 3; column++){
            if(grid[0][column] == grid[1][column] && grid[1][column] == grid[2][column]){
                if(grid[0][column] == CellOptions.MAX) {
                    return MAX_WIN_SCORE;
                } else if(grid[0][column] == CellOptions.MIN){
                    return MIN_WIN_SCORE;
                }
            }
        }

        // check diagonals for win
        if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2])
        {
            if (grid[0][0] == CellOptions.MAX)
                return +10;
            else if (grid[0][0] == CellOptions.MIN)
                return -10;
        }

        if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0])
        {
            if (grid[0][2] == CellOptions.MAX)
                return +10;
            else if (grid[0][2] == CellOptions.MIN)
                return -10;
        }

        return 0;
    }

    public Move findBestMove(CellOptions[][] grid) {
        int bestVal = -1000;
        Move bestMove = new Move(-1,-1);

        for(int i = 0; i < 3; i++) {
            for(int j = 0; i < 3; i++) {
                if(grid[i][j] == CellOptions.EMPTY) {

                    // Make the move
                    grid[i][j] = CellOptions.MAX;

                    // compute evalueation
                    int moveVal = minimax(grid, false);

                    // undo move
                    grid[i][j] = CellOptions.EMPTY;

                    if(moveVal > bestVal)
                    {
                        bestMove.row = i;
                        bestMove.column = j;
                        bestVal = moveVal;
                    }

                }
            }
        }

        return bestMove;
    }

    private int minimax(CellOptions[][] grid, boolean isMax) {

        int score = evaluateWinCondition(grid);

        // if max has won
        if(score == MAX_WIN_SCORE) {
            return score;
        }

        // if min has won
        if(score == MIN_WIN_SCORE) {
            return score;
        }

        // there are no moves left available
        if(isGridFull(grid)){
           return 0;
        }

        if(isMax) {
            int best = -1000;

            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if(grid[i][j] == CellOptions.EMPTY) {

                        // make the move
                        grid[i][j] = CellOptions.MAX;

                        // call recursively, change player
                        best = Math.max(best, minimax(grid, !isMax));

                        // undo the move
                        grid[i][j] = CellOptions.EMPTY;
                    }
                }
            }

            return best;

        } else {
            int best = 1000;

            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if(grid[i][j] == CellOptions.EMPTY) {

                        // make the move
                        grid[i][j] = CellOptions.MIN;

                        // call recursively, change player
                        best = Math.min(best, minimax(grid, !isMax));

                        // undo the move
                        grid[i][j] = CellOptions.EMPTY;
                    }
                }
            }

            return best;
        }
    }

    public class Move {
        public int row;
        public int column;

        public Move(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}
