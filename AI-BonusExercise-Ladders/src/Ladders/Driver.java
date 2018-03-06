package Ladders;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class Driver {
	protected static String goalWord;
	private static WordList list;
	private static List<TestObject> tests;

	public static void main(String[] args) throws FileNotFoundException {
		Node rootNode = new Node(args[0], null);
		goalWord = args[1];
		list = new WordList();
		Node solution = AStarSearch(rootNode);
//		Node solution = IterativeDeepeningSearch(rootNode);
//		Node solution = breadthFirstSearch(rootNode);
		printSolution(solution, "output.txt");
	}

	public static void testing() throws FileNotFoundException {
		tests = new ArrayList<TestObject>();
		tests.add(new TestObject("croissant", "baritone", "output_1.txt"));
		tests.add(new TestObject("crumpet", "treacle", "output_2.txt"));
		tests.add(new TestObject("apple", "pear", "output_3.txt "));
		tests.add(new TestObject("lead", "gold", "output_4.txt"));
		tests.add(new TestObject("aah", "intersessions", "output_5.txt"));

		list = new WordList();
		for (TestObject test : tests) {
			System.out.println("Problem " + test.getInitialWord());
			Node rootNode = new Node(test.getInitialWord(), null);
			// Node solution = breadthFirstSearch(rootNode);
			goalWord = test.getGoalWord();
			// Node solution = IterativeDeepeningSearch(rootNode);
			Node solution = AStarSearch(rootNode);
			printSolution(solution, test.getOutputFileName());

		}
	}

	public static Node AStarSearch(Node rootNode) {
		Queue<Node> frontier = new PriorityQueue<>();
		Set<String> explored = new HashSet<String>();
		Set<String> frontierHash = new HashSet<String>();
		frontier.add(rootNode);
		frontierHash.add(rootNode.getWord());
		while (!frontier.isEmpty()) {
			Node node = frontier.poll();
			if (isGoalNode(node)) {
				System.out.println("Goal found!");
				return node;
			}
			explored.add(node.getWord());
			frontierHash.remove(node.getWord());

			List<Node> children = getAllChildrenFromPermutationDictionary(node);
			for (Node child : children) {
				// System.out.println(child.getWord());
				if (!explored.contains(child.getWord()) && !frontierHash.contains(child.getWord())) {
					frontier.add(child);
					frontierHash.add(child.getWord());
				}
			}

		}

		return null;
	}

	// // Comparator anonymous class implementation
	// public static Comparator<Node> comparator = new Comparator<Node>() {
	// @Override
	// public int compare(Node n1, Node n2) {
	// return (int) (f(n1) - f(n2));
	// }
	// };

	public static int f(Node node) {
		return getHeuristic(node.getWord()) + node.getCost();
	}

	public static int getHeuristic(String word) {
		// heuristic : the goal is reached by adding or removing characters , so
		// the heuristic is the number of characters missing from goal

		int[] strA = new int[26];

		for (int i = 0; i < goalWord.length(); i++) {
			strA[goalWord.charAt(i) - 'a']++;
			strA[word.charAt(i) - 'a']--;
		}
		int heuristicValue = 0;
		for (int i = 0; i < strA.length; i++) {
			heuristicValue += Math.abs(strA[i]);
		}

		return heuristicValue;
	}

	public static Node IterativeDeepeningSearch(Node rootNode) {
		for (int limit = 1; limit < 1000; limit++) {
			Node result = depthLimitedSearch(rootNode, limit);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public static Node depthLimitedSearch(Node node, int limit) {
		if (isGoalNode(node)) {
			System.out.println("Goal found!");
			return node;
		} else if (limit == 0) {
			return null;
		} else {
			// List<Node> children = getAllChildren(node);
			List<Node> children = getAllChildrenFromPermutationDictionary(node);
			for (Node child : children) {
				// System.out.println(child.getWord());
				Node result = depthLimitedSearch(child, limit - 1);
				if (result != null) {
					return result;
				}
			}
			return null;
		}
	}

	public static List<Node> getAllChildrenFromPermutationDictionary(Node node) {
		// we can add one letter or remove one letter and re-arrange all letters
		// , the resulted word should be inside the dictionary
		// System.out.println("Children of word "+node.getWord());
		List<Node> children = new ArrayList<Node>();
		List<String> possibleWords = new ArrayList<String>();
		// = new ArrayList<String>();
		String word = node.getWord();
		int wordLength = word.length();
		// first possibility remove a character
		if (wordLength > list.getMinWordLength()) {
			for (int i = 0; i < wordLength; i++) {
				String newWord = removeCharfromString(word, i);
				List<String> permutation = list.getKeyValue(newWord);
				if (permutation != null) {
					for (String string : permutation) {
						possibleWords.add(string);

					}
				}
			}
		}
		// second possibility add a character only if the length of current word
		// does not exceed the maximum
		if (wordLength < list.getMaxWordLength()) {
			for (char i = 'a'; i <= 'z'; i++) {
				List<String> permutation = list.getKeyValue(word + i);
				if (permutation != null) {
					for (String string : permutation) {
						possibleWords.add(string);
					}
				}
			}
		}

		// finally create nodes for all possible nodes and return list of
		// children
		for (String string : possibleWords) {
			children.add(new Node(string, node));
		}
		return children;
	}

	public static Node breadthFirstSearch(Node rootNode) {

		if (isGoalNode(rootNode)) {
			return rootNode;
		} else {
			Queue<Node> frontier = new LinkedList<Node>();
			Set<String> frontierHash = new HashSet<String>();
			Set<String> explored = new HashSet<String>();
			frontier.add(rootNode);
			frontierHash.add(rootNode.getWord());
			while (!frontier.isEmpty()) {
				Node node = frontier.remove();
				explored.add(node.getWord());
				List<Node> children = getAllChildren(node);
				for (Node child : children) {
					if (!explored.contains(child.getWord()) && !frontierHash.contains(child.getWord())) {
						if (isGoalNode(child)) {
							System.out.println("Goal found!");
							return child;
						} else {
							frontier.add(child);
							frontierHash.add(child.getWord());
						}
					}
				}
			}
			return null; // Failure No solution
		}
	}

	public static List<Node> getAllChildren(Node node) {
		// we can add one letter or remove one letter and re-arrange all letters
		// , the resulted word should be inside the dictionary
		List<Node> children = new ArrayList<Node>();
		Set<String> possibleWords = new HashSet<String>();
		String word = node.getWord();
		int wordLength = word.length();
		// first possibility remove a character
		if (wordLength > list.getMinWordLength()) {
			for (int i = 0; i < wordLength; i++) {
				String newWord = removeCharfromString(word, i);
				List<String> permutation = permutation(newWord);
				for (String string : permutation) {
					// add it if it is inside the word list
					if (list.hasWordInList(string)) {
						possibleWords.add(string);
					}

				}
			}
		}
		// second possibility add a character only if the length of current word
		// does not exceed the maximum
		if (wordLength < list.getMaxWordLength()) {
			for (char i = 'a'; i <= 'z'; i++) {
				List<String> permutation = permutation(word + i);
				for (String string : permutation) {
					if (list.hasWordInList(string)) {
						possibleWords.add(string);
					}
				}
			}
		}

		// finally create nodes for all possible nodes and return list of
		// children
		for (String string : possibleWords) {
			children.add(new Node(string, node));
		}
		return children;
	}

	public static String removeCharfromString(String word, int index) {
		StringBuilder string = new StringBuilder(word);
		string.deleteCharAt(index);
		return string.toString();
	}

	public static void printSolution(Node goalNode, String fileName) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(fileName);
		if (goalNode == null) {
			System.out.println("No Solution found !");
		} else {
			Stack<String> solution = new Stack<String>();
			int solutionLength = 0;
			while (goalNode != null) {
				System.out.println(goalNode.getWord());
				solution.push(goalNode.getWord());
				goalNode = goalNode.getParent();
				solutionLength++;
			}
			System.out.println("Path Length =" + solutionLength);

			while (!solution.isEmpty()) {
				out.print(solution.pop());
				solutionLength--;
				if (solutionLength != 0) {
					out.println();
				}
			}
		}
		out.close();
	}

	public static boolean isGoalNode(Node node) {
		return node.getWord().equals(goalWord);
	}

	public static List<String> permutation(String word) {
		List<String> list = new ArrayList<String>();
		// Set<String> list = new HashSet<String>();
		if (word.length() == 1) {
			list.add(word);
		} else if (word.length() > 1) {
			int lastIndex = word.length() - 1;
			String last = word.substring(lastIndex);
			String rest = word.substring(0, lastIndex);
			list = merge(permutation(rest), last);
		}
		return list;
	}

	public static List<String> merge(List<String> list, String c) {
		List<String> result = new ArrayList<String>();
		for (String s : list) {
			for (int i = 0; i <= s.length(); ++i) {
				String ps = new StringBuffer(s).insert(i, c).toString();
				result.add(ps);
			}
		}
		return result;
	}
}
