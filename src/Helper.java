import java.util.Arrays;
public class Helper {
	static int MAXCOL = Connect4JFrame.MAXCOL;
	static int MAXROW = Connect4JFrame.MAXROW;
	int BLANK = Connect4JFrame.BLANK;
	
	/**
	 * This method check if the board is full.
	 * @param board Represents playing board to check
	 * @return Returns true if full, false otherwise
	 */
	public boolean checkFull(int[][] board) {
		for (int column = 0; column < MAXCOL; column++) {
			for (int row = 0; row < MAXROW; row++) {
				if (board[column][row] == BLANK) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * This method checks the board to see if a player won the game.
	 * @param board Represents the playing board 
	 * @return Returns the color of the player that won, or 0 if
	 * 			nobody won
	 */
	public static int checkWin(int[][] board) {
		
		for (int row=0; row<MAXROW; row++) {
            for (int col=0; col<MAXCOL-3; col++) {
                int curr = board[row][col];
                if (curr>0
                 && curr == board[row][col+1]
                 && curr == board[row][col+2]
                 && curr == board[row][col+3]) {
                	return board[row][col];
                }
            }
        }
        // vertical columns
        for (int col=0; col<MAXCOL; col++) {
            for (int row=0; row<MAXROW-3; row++) {
                int curr = board[row][col];
                if (curr>0
                 && curr == board[row+1][col]
                 && curr == board[row+2][col]
                 && curr == board[row+3][col])
                    return board[row][col];
            }
        }
        // diagonal lower left to upper right
        for (int row=0; row<MAXROW-3; row++) {
            for (int col=0; col<MAXCOL-3; col++) {
                int curr = board[row][col];
                if (curr>0
                 && curr == board[row+1][col+1]
                 && curr == board[row+2][col+2]
                 && curr == board[row+3][col+3])
                	return board[row][col];
            }
        }
        // diagonal upper left to lower right
        for (int row=MAXROW-1; row>=3; row--) {
    		for (int col=0; col<MAXCOL-3; col++) {
    				int curr = board[row][col];
                    if (curr>0
                     && curr == board[row-1][col+1]
                     && curr == board[row-2][col+2]
                     && curr == board[row-3][col+3])
                    	return board[row][col];
            }
        }
        return 0;
	}
	
	/**
	 * This method finds the maximum or minimum of an array depending
	 * on the max values - if TRUE finds maximum, otherwise minimum
	 * @param array Represents the array from which to return min or max
	 * @param max Represents the needed value, if TRUE - maximum else minimum
	 * @return Returns the minimum or maximum value of the given array
	 */
	public static int getMaxOrMin(int[] array, boolean max) {
		int tempMax = -2000;
		int tempMin = 2000;
		if (max) {
			for (int i = 0; i < array.length; i++) {
				if(array[i] > tempMax)
					tempMax = array[i];
			}
			return tempMax;
		} else {
			for (int i = 0; i < array.length; i++) {
				if(array[i] < tempMin)
					tempMin = array[i];
			}
			return tempMin;
		}

	}
	static class ScoreException extends Exception{
		public ScoreException() {
			super("A good enough winning score was reached!");
		}
	}
}
