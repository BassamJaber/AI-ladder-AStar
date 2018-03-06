package Ladders;

@SuppressWarnings("rawtypes")
public class Node implements Comparable {

	private String word;
	private Node parent;
	private int cost;

	public Node(String word, Node parent) {
		super();
		this.word = word;
		this.parent = parent;
		if (parent != null) {
			this.cost = parent.getCost() + 1;
		} else {
			this.cost = 0; // in the case if root node
		}
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	@Override
	public int compareTo(Object node) {
		if (f((Node) node) == f(this)) {
			return 0;
		} else if (f((Node) node) < f(this)) {
			return 1;
		}
		return -1;

	}

	public int f(Node node) {
		return getHeuristic(node.getWord()) + node.getCost();
	}

	public int getHeuristic(String word) {
		// heuristic : the goal is reached by adding or removing characters , so
		// the heuristic is the number of characters missing from goal

		int[] strA = new int[26];

		for (int i = 0; i < Driver.goalWord.length(); i++) {
			strA[Driver.goalWord.charAt(i) - 'a']++;
		}
		for (int i = 0; i < word.length(); i++) {
			strA[word.charAt(i) - 'a']--;
		}
		int heuristicValue = 0;
		for (int i = 0; i < strA.length; i++) {
			heuristicValue += Math.abs(strA[i]);
		}

		return heuristicValue;
	}

}
