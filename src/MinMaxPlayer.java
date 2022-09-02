import java.sql.Timestamp;
import java.util.Arrays;

public class MinMaxPlayer {
	int maximizer; // maximizer is agent player
	int minimizer; // minimizer is human player
	
	public void firstMove(int[][] board, int activePlayer) {
		Long startTime = System.currentTimeMillis();
		DecisionTree decisionTree = generateTree(board, activePlayer);
		Long endTime = System.currentTimeMillis();
		
		// TODO: implement MinMax player
		// TODO: call next move
		System.out.println("Time needed to construct tree: " + (endTime-startTime));
	}
	
	public DecisionTree generateTree(int[][] board, int activePlayer) {
		int nodeLevel = 0;
		DecisionTree desicionTree = new DecisionTree();
		desicionTree.root = new Node(true, 0,  board, 0, 0);
		int[][] newBoard = board;
		generateSubtree(newBoard, activePlayer, desicionTree.root, nodeLevel);
		return desicionTree;
	}
	
	public void generateSubtree(int[][] board, int activePlayer, Node curentNode, int nodeLevel) {
		nodeLevel++;
		for(int column = 0; column < Connect4JFrame.MAXCOL; column++) {
			if (board[0][column] == 0) {
				int[][] newBoard = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
				newBoard = simulateMove(newBoard, activePlayer, column);
				curentNode.childNodes.add(new Node(false, nodeLevel, newBoard, 0, 0));;
			}
		}
		
		if (activePlayer == Connect4JFrame.YELLOW)
			activePlayer = Connect4JFrame.RED;
		else 
			activePlayer = Connect4JFrame.YELLOW;
		
		
		if (nodeLevel <= 7) {
			for (int node = 0; node < curentNode.childNodes.size(); node++) {
				generateSubtree(curentNode.childNodes.get(node).boardState, 
						activePlayer, curentNode.childNodes.get(node), nodeLevel);
			}
		}
	}
	

	
	public int getNumberOfPossibleMoves(int[][] board, int activePlayer) {
		int numberOfPossibleMoves = 0;
		for(int i = 0; i < Connect4JFrame.MAXCOL; i++) {
			if (board[0][i] == 0){
				numberOfPossibleMoves++;
			}
		}
		return numberOfPossibleMoves;
	}
	
	public int[][] simulateMove(int[][] board, int activePlayer, int colNumber){
		
		for(int row = 0; row < Connect4JFrame.MAXROW; row++) {
			if (board[row][colNumber] != Connect4JFrame.BLANK){
				board[row-1][colNumber] = activePlayer;
				return board;
			}
		}
		board[Connect4JFrame.MAXROW - 1][colNumber] = activePlayer;
		return board;
	}
}
