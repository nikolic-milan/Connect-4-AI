import java.util.Random;

public class RandomPlayer {
	
	/**
	 * This method randomly chooses the agents next move.
	 * @return Return and integers between 1 and 7 which
	 * 			represents the column in which to place
	 * 			the disk.
	 */
	public int nextMove() {
		Random random = new Random();
	    return random.nextInt(8 - 1) + 1;	    
	}
}
