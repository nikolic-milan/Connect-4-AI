import java.sql.Timestamp;
import java.util.Arrays;
import java.lang.Math;  

public class MinMaxPlayer {
	int agentColour; // maximizer is agent player
	int minimizer; // minimizer is human player
	int alpha = -123456789; // min value
	int beta  = 123456789; // max value
	
	public static int WIN = 100000;
	public static int LOSE = -100000;
	public static int DRAW = 0;
	public static int THREECONNECTED = 800;
	public static int TWOCONNECTED = 200;
	public static int OPTIMALEVALUATIONSCORE = 1200;
	
	
	/**
	 * This method calculates and chooses the next move for the MinMax agent
	 * based on the utility score.
	 * @param board Represents the current playing board
	 * @param activePlayer Represents the current activePlayer
	 * @return Returns the column which is the agent's next move
	 */
	public int nextMove(int[][] board, int activePlayer) {
		agentColour = activePlayer;

		DecisionTree decisionTree = generateTree(board, activePlayer);
		
		calculateTreeUtil(decisionTree.root);
		int[] utilityScores = new int[decisionTree.root.childNodes.size()];
		for(int child = 0; child < decisionTree.root.childNodes.size(); child++) {
			utilityScores[child] = decisionTree.root.childNodes.get(child).utilityScore;
		}
		int score = Helper.getMaxOrMin(utilityScores, true);
		// TODO: fix this to get next move, directly.
		int movePlayed = 1;
		for (int i = 0; i < utilityScores.length; i++) {
			if(utilityScores[i] == score) {
				movePlayed = decisionTree.root.childNodes.get(i).movePlayed;
			}
		}
		return movePlayed;
	}
	
	/**
	 * This method calculates and chooses the next move for the MinMax agent based
	 * on the evaluation scores.
	 * @param board Represents the current playing board
	 * @param activePlayer Represents the current activePlayer
	 * @return Returns the column which is the agent's next move
	 */
	public int nextMoveEval(int[][] board, int activePlayer) {
		
		agentColour = activePlayer;
		alpha = -123456789; 
		beta  = 123456789;
		DecisionTree decisionTree = generateTree(board, activePlayer);
		calculateTreeEval(decisionTree.root);
		
		int[] evaluationScores = new int[decisionTree.root.childNodes.size()];
		for(int child = 0; child < decisionTree.root.childNodes.size(); child++) {
			evaluationScores[child] = decisionTree.root.childNodes.get(child).evaluationScore;
		}
		int score = Helper.getMaxOrMin(evaluationScores, true);
		// TODO: fix this to get next move, directly.
		int movePlayed = 1;
		for (int i = 0; i < evaluationScores.length; i++) {
			if(evaluationScores[i] == score) {
				movePlayed = decisionTree.root.childNodes.get(i).movePlayed;
				break;
			}
		}
		return movePlayed;
	}
	/**
	 * This method calculates and chooses the next move for the MinMax agent
	 * base on the evaluation scores but using alpha-beta pruning.
	 * @param board Represents the current playing board
	 * @param activePlayer Represents the current activePlayer
	 * @return Returns the column which is the agent's next move
	 */
	public int nextMoveAB(int[][] board, int activePlayer) {
		agentColour = activePlayer;

		DecisionTree decisionTree = generateTree(board, activePlayer);
		calculateTreeEvalAB(decisionTree.root);
		
		int[] evaluationScores = new int[decisionTree.root.childNodes.size()];
		for(int child = 0; child < decisionTree.root.childNodes.size(); child++) {
			evaluationScores[child] = decisionTree.root.childNodes.get(child).evaluationScore;
		}
		int score = Helper.getMaxOrMin(evaluationScores, true);
		// TODO: fix this to get next move, directly.
		int movePlayed = 1;
		for (int i = 0; i < evaluationScores.length; i++) {
			if(evaluationScores[i] == score) {
				movePlayed = decisionTree.root.childNodes.get(i).movePlayed;
				break;
			}
		}
		return movePlayed;
	}
	
	/**
	 * This method generate the decision tree by creating a root node and
	 * utilizing the generateSubtree method for recursively creating the
	 * the rest of the tree.
	 * @param board Represents the current board state
	 * @param activePlayer Represents the current active player
	 * @return Returns a build decision tree of depth 7.
	 */
	public DecisionTree generateTree(int[][] board, int activePlayer) {
		int nodeLevel = 0;
		DecisionTree decisionTree = new DecisionTree();
		decisionTree.root = new Node(true, 0, 0, Math.abs((activePlayer-3)), board, 0, 1);
		int[][] newBoard = board;
		generateSubtree(newBoard, activePlayer, decisionTree.root, nodeLevel);

		return decisionTree;
	}
	
	/**
	 * This recursive method generates the subtree of the Decision tree. 
	 * (everything bellow root node)
	 * It generate the tree up to a depth of 7, due to performance 
	 * and RAM requirements/limitations. 
	 * @param board Represents the current board state
	 * @param activePlayer Represents the current active player
	 * @param curentNode Represents the node to which to add children nodes
	 * @param nodeLevel Represents the current node level
	 */
	public void generateSubtree(int[][] board, int activePlayer, Node curentNode, int nodeLevel) {
		nodeLevel++;
		for(int column = 0; column < Connect4JFrame.MAXCOL; column++) {
			if (board[0][column] == Connect4JFrame.BLANK) {
				int[][] newBoard = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
				newBoard = simulateMove(newBoard, activePlayer, column);
				int winner = Helper.checkWin(newBoard);
				int utilityScore = calculateUtility(winner, activePlayer);
				int evaluation = evaluate(newBoard, activePlayer);
				curentNode.childNodes.add(new Node(false, nodeLevel, column+1, activePlayer, newBoard, utilityScore, evaluation));
			}
		}
		// change player
		activePlayer = Math.abs((activePlayer-3));
		
		if (nodeLevel <= 7) {
			for (int node = 0; node < curentNode.childNodes.size(); node++) {
				// utility check
				/*
				if(curentNode.childNodes.get(node).utilityScore != WIN && 
						curentNode.childNodes.get(node).utilityScore != LOSE)
					generateSubtree(curentNode.childNodes.get(node).boardState, 
							activePlayer, curentNode.childNodes.get(node), nodeLevel);
				*/
				// evaluation check
				if (curentNode.childNodes.get(node).evaluationScore < OPTIMALEVALUATIONSCORE) 
					generateSubtree(curentNode.childNodes.get(node).boardState, 
							activePlayer, curentNode.childNodes.get(node), nodeLevel);
			}
		}
	}
	
	/**
	 * This method simulates the next move on the board.
	 * @param board Represents the current board
	 * @param activePlayer Represents the current active player yellow/red
	 * @param colNumber Represents to column in which the disk should be placed
	 * @return Returns the new board with the move played
	 */
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
	
	/**
	 * This method calculates the utility score. If the agent wins
	 * the score is 500, if the agents looses the score is -500.
	 * -1000 and 1000 for boards with no winner used as min and max
	 * values respectively.
	 * @param winner
	 * @param activePlayer
	 * @return Return the utility value
	 */
	public int calculateUtility(int winner, int activePlayer) {
		if (winner == 0) {
			return DRAW;
		}
		else if (winner == agentColour)
			return WIN;
		else
			return LOSE;
		
	}
	
	/**
	 * This method evaluates the given board and calculates a score.
	 * @param board Represent the board to be evaluated.
	 * @param activePlayer Represents the current active player.
	 * @return Returns the score calculate. Can be positive or negative!
	 */
	public int evaluate(int[][] board, int activePlayer) {
		int score = 0;
		int winner = Helper.checkWin(board);
		if (winner == 0) {
			// horizontal 2 & 3
			for (int row = 0; row < Connect4JFrame.MAXROW; row++) {
	            for (int col = 0; col < Connect4JFrame.MAXCOL - 3; col++) {
	                int curr = board[row][col];
	                if (curr > 0
	                 && curr == board[row][col + 1]
	                 && curr == board[row][col + 2]
	                 && board[row][col + 3] == Connect4JFrame.BLANK) {
	                	if (board[row][col] == agentColour)
	                		score += THREECONNECTED;
	                	else
	                		score -= THREECONNECTED;
	                } else if (curr > 0 
	                		&& curr == board[row][col+1]
	                		&& board[row][col+2] == Connect4JFrame.BLANK) {
	                	if (board[row][col] == agentColour)
	                		score += TWOCONNECTED;
	                	else
	                		score -= TWOCONNECTED;
	                }
	            }
	        }
			// vertical 2 & 3
	        for (int col = 0; col < Connect4JFrame.MAXCOL; col++) {
	            for (int row = 0; row < Connect4JFrame.MAXROW - 3; row++) {
	                int curr = board[row][col];
	                if (curr > 0
	                 && curr == board[row + 1][col]
	                 && curr == board[row + 2][col]
	                 && board[row + 3][col] == Connect4JFrame.BLANK)
	                	if (board[row][col] == agentColour)
	                		score += THREECONNECTED;
	                	else
	                		score -= THREECONNECTED;
	                else if (curr > 0
	   	                 && curr == board[row + 1][col]
	   	 	                 && board[row + 2][col] == Connect4JFrame.BLANK) {
	                	if (board[row][col] == agentColour)
	                		score += TWOCONNECTED;
	                	else
	                		score -= TWOCONNECTED;
	                }
	            }
	        }
	        // diagonal lower left to upper right 2 & 3
	        for (int row = 0; row < Connect4JFrame.MAXROW - 3; row++) {
	            for (int col=0; col < Connect4JFrame.MAXCOL - 3; col++) {
	                int curr = board[row][col];
	                if (curr > 0
	                 && curr == board[row + 1][col + 1]
	                 && curr == board[row + 2][col + 2]
	                 && board[row + 3][col + 3] == Connect4JFrame.BLANK)
	                	if (board[row][col] == agentColour)
	                		score += THREECONNECTED;
	                	else
	                		score -= THREECONNECTED;
	                else if (curr > 0
	                 && curr == board[row + 1][col + 1]
	                 && board[row + 2][col + 2] == Connect4JFrame.BLANK) {
	                	if (board[row][col] == agentColour)
	                		score += TWOCONNECTED;
	                	else
	                		score -= TWOCONNECTED;
	                }
	            }
	        }        
	     // diagonal upper left to lower right 2 & 3
	        for (int row = Connect4JFrame.MAXROW -1; row >= 3; row--) {
	    		for (int col = 0; col < Connect4JFrame.MAXCOL - 3; col++) {
	    				int curr = board[row][col];
	                    if (curr>0
	                     && curr == board[row - 1][col + 1]
	                     && curr == board[row - 2][col + 2]
	                     && board[row - 3][col + 3] == Connect4JFrame.BLANK)
	                    	if (board[row][col] == agentColour)
		                		score += THREECONNECTED;
		                	else
		                		score -= THREECONNECTED;
	                    else if (curr>0
	   	                     && curr == board[row - 1][col + 1]
	   	 	                     && board[row - 2][col + 2] == Connect4JFrame.BLANK)
	   	 	                    	if (board[row][col] == agentColour)
	   	 		                		score += THREECONNECTED;
	   	 		                	else
	   	 		                		score -= THREECONNECTED;
	            }
	        }
		} else if (winner == agentColour)
			return WIN;
		else
			return LOSE;
		return score;
	}
	
	/**
	 * This method calculates the utility score for every node in the tree.
	 * @param node Represents the starting node
	 * @return Returns the utility score of the node
	 */
	public int calculateTreeUtil(Node node) {
		if (node.nodeLevel == 7 || node.childNodes.size() == 0) {
			return node.utilityScore;
		}
		int[] childNodesScores = new int[node.childNodes.size()];
		for (int n = 0; n < node.childNodes.size(); n++) {
			childNodesScores[n] = calculateTreeUtil(node.childNodes.get(n));
		}
		if(node.playerColour != agentColour)
			node.utilityScore = Helper.getMaxOrMin(childNodesScores, true);
		else
			node.utilityScore = Helper.getMaxOrMin(childNodesScores, false);
		
		return node.utilityScore; // exit needed for java, not used
	}
	
	/**
	 * This method calculates the evaluation score for every node in the tree.
	 * @param node Represents the starting node
	 * @return Returns the utility score of the node
	 */
	public int calculateTreeEval(Node node) {
		if (node.nodeLevel == 7 || node.childNodes.size() == 0) {
			return node.evaluationScore;
		}
		int[] childNodesScores = new int[node.childNodes.size()];
		for (int n = 0; n < node.childNodes.size(); n++) {
			childNodesScores[n] = calculateTreeEval(node.childNodes.get(n));
			//if agentColour != currentPlayer
				// if childNodesScore[n] < alpha
					//
		}
		if(node.playerColour != agentColour)
			// minValue = gemaxormin
			// alpha = minValue
			node.evaluationScore = Helper.getMaxOrMin(childNodesScores, true);
		else
			// mixValue = gemaxormin
			// beta = mixValue
			node.evaluationScore = Helper.getMaxOrMin(childNodesScores, false);
		
		return node.evaluationScore; // exit needed for java, not used
	}
	
	/**
	 * This method calculates the evaluation score for every node in the tree
	 * with alpha-beta pruning.
	 * @param node Represents the starting node
	 * @return Returns the utility score of the node
	 */
	public int calculateTreeEvalAB(Node node) {
		if (node.nodeLevel == 7 || node.childNodes.size() == 0) {
			return node.evaluationScore;
		}
		int[] childNodesScores = new int[node.childNodes.size()];
		for (int n = 0; n < node.childNodes.size(); n++) {
			childNodesScores[n] = calculateTreeEval(node.childNodes.get(n));
			if (node.playerColour != agentColour) {
				if (Helper.isMax(childNodesScores, childNodesScores[n])) {
					if(childNodesScores[n] >= beta)
						break;
					alpha = Helper.max(alpha, childNodesScores[n]);
				}
			} else {
				if (Helper.isMin(childNodesScores, childNodesScores[n])) {
					if(childNodesScores[n] <= alpha)
						break;
					beta = Helper.min(beta,  childNodesScores[n]);
				}
			}
		}
		
		if(node.playerColour != agentColour)
			// minValue = gemaxormin
			// alpha = minValue
			node.evaluationScore = Helper.getMaxOrMin(childNodesScores, true);
		else
			// mixValue = gemaxormin
			// beta = mixValue
			node.evaluationScore = Helper.getMaxOrMin(childNodesScores, false);
		
		return node.evaluationScore; // exit needed for java, not used
		
	}
}
