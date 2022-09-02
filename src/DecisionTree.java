import java.util.*;

public class DecisionTree {
	Node root;

	public DecisionTree() {
		
	}
	
}
class Node{
	boolean rootNode;
	int nodeLevel;
	int[][] boardState;
	int utilityScore;
	int evaluationScore;
	ArrayList<Node> childNodes;
	
	public Node(boolean rootNode, int nodeLevel, int[][] boardState,
			int utilityScore, int evaluationScore) {
		this.rootNode = rootNode;
		this.nodeLevel = nodeLevel;
		this.boardState = boardState;
		this.utilityScore = utilityScore;
		this.evaluationScore = evaluationScore;
		this.childNodes = new ArrayList<Node>();
	}
}
