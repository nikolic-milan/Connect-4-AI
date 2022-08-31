import java.util.Random;

public class RandomPlayer {

	public int nextMove() {
		Random random = new Random();
	    return random.nextInt(8 - 1) + 1;	    
	}
}
