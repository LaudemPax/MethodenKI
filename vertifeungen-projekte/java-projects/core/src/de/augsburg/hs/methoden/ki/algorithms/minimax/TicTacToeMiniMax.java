package de.augsburg.hs.methoden.ki.algorithms.minimax;

/***
 * Max is always X, Min is always O
 *
 */
public class TicTacToeMiniMax {

    public static final int MAX_WIN_SCORE = 10;
    public static final int MIN_WIN_SCORE = -10;

    public TicTacToe[][] generateEmptyGrid() {

        TicTacToe[][] grid = new TicTacToe[3][3];

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                grid[j][i] = TicTacToe.EMPTY;
            }
        }

        return grid;
    }

    public boolean isGridFull(TicTacToe[][] grid) {
        boolean isFull = true;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++){
                if(grid[j][i] == TicTacToe.EMPTY){
                    isFull = false;
                    break;
                }
            }
        }

        return isFull;
    }

    public int evaluateWinCondition(TicTacToe[][] grid) {

        // check each row for win
        for(int row = 0; row < 3; row++){
            if(grid[row][0] == grid[row][1] && grid[row][1] == grid[row][2]){
                if(grid[row][0] == TicTacToe.MAX) {
                    return MAX_WIN_SCORE;
                } else if(grid[row][0] == TicTacToe.MIN){
                    return MIN_WIN_SCORE;
                }
            }
        }

        // check each column for win
        for(int column = 0; column < 3; column++){
            if(grid[0][column] == grid[1][column] && grid[1][column] == grid[2][column]){
                if(grid[0][column] == TicTacToe.MAX) {
                    return MAX_WIN_SCORE;
                } else if(grid[0][column] == TicTacToe.MIN){
                    return MIN_WIN_SCORE;
                }
            }
        }

        // check diagonals for win
        if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2])
        {
            if (grid[0][0] == TicTacToe.MAX)
                return +10;
            else if (grid[0][0] == TicTacToe.MIN)
                return -10;
        }

        if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0])
        {
            if (grid[0][2] == TicTacToe.MAX)
                return +10;
            else if (grid[0][2] == TicTacToe.MIN)
                return -10;
        }

        return 0;
    }

    public TicTacToeMove findBestMove(TicTacToe[][] grid) {
        int bestVal = -1000;
        TicTacToeMove bestMove = new TicTacToeMove(-1,-1);

        System.out.println("=========================== Finding best move ===========================");

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(grid[i][j] == TicTacToe.EMPTY) {

                    // Make the move
                    grid[i][j] = TicTacToe.MAX;

                    // compute evaluation
                    int moveVal = minimax(grid, false);

                    // undo move
                    grid[i][j] = TicTacToe.EMPTY;

                    if(moveVal > bestVal)
                    {
                        bestMove.row = i;
                        bestMove.column = j;
                        bestVal = moveVal;
                    }

                    System.out.printf("Move (row=%d, column=%d) score: %d\n", i, j, moveVal);

                }
            }
        }

        return bestMove;
    }

    private int minimax(TicTacToe[][] grid, boolean isMax) {

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

        int best;
        if(isMax) {
            best = -1000;

            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if(grid[i][j] == TicTacToe.EMPTY) {

                        // make the move
                        grid[i][j] = TicTacToe.MAX;

                        // call recursively, change player
                        best = Math.max(best, minimax(grid, false));

                        // undo the move
                        grid[i][j] = TicTacToe.EMPTY;
                    }
                }
            }

        } else {
            best = 1000;

            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if(grid[i][j] == TicTacToe.EMPTY) {

                        // make the move
                        grid[i][j] = TicTacToe.MIN;

                        // call recursively, change player
                        best = Math.min(best, minimax(grid, true));

                        // undo the move
                        grid[i][j] = TicTacToe.EMPTY;
                    }
                }
            }

        }
        return best;
    }


}
