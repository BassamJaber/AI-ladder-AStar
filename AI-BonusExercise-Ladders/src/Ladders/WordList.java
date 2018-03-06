package Ladders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WordList {
	private static List<String> list;
	private final static String FILE_NAME_WORD_LIST = "wordList.txt";
	private final static String FILE_NAME_PRE_COMPUTED_LIST = "precomputed_permutation_list_6.txt";
	private final static String FILE_NAME_PRE_COMPUTED_PREFIX = "precomputed_permutation_list";
	private final static String FILE_NAME_PRE_COMPUTED_LIST_MERGED = "precomputed_permutation_list.txt";
	private int maxWordLength = 28;
	private int minWordLength = 3;
	private File inputFile;
	private static PrintWriter write;
	private static Map<String, List<String>> permutationDictionaryList = new HashMap<String, List<String>>();

	public WordList() {
		fillListFromFile();
		readPermutationDictionaryList();
	}

	private void readPermutationDictionaryList() {
		System.out.println("Reading the file");
		inputFile = new File(FILE_NAME_PRE_COMPUTED_LIST_MERGED);
		try {
			Scanner in = new Scanner(inputFile);
			int lineCount = 0;
			while (in.hasNext()) {
				String line = in.nextLine();
				String[] parts = line.split(":");
				String key = parts[0];
				String[] words = parts[1].split(" ");
				List<String> wordList = new ArrayList<String>();
				for (int i = 0; i < words.length; i++) {
					wordList.add(words[i]);
				}
				permutationDictionaryList.put(key, wordList);
				lineCount++;
			}
			in.close();
			System.out.println("Number of lines inside the file : " + lineCount);
			System.out.println("Number of words inside the file : " + permutationDictionaryList.size());
			System.out.println("Reading file Done");
		} catch (FileNotFoundException e) {
			System.out.println("File not Found " + FILE_NAME_PRE_COMPUTED_LIST_MERGED);
		}
	}

	public List<String> getKeyValue(String key) {
		return permutationDictionaryList.get(key);
	}

	private void fillListFromFile() {
		System.out.println("Reading the file");
		list = new ArrayList<String>();
		inputFile = new File(FILE_NAME_WORD_LIST);
		try {
			Scanner in = new Scanner(inputFile);
			while (in.hasNext()) {
				String word = in.nextLine().trim();
				int wordLength = word.length();
				if (maxWordLength < wordLength) {
					maxWordLength = wordLength;
				}

				if (minWordLength > wordLength) {
					minWordLength = wordLength;
				}
				list.add(word);
			}
			in.close();
			System.out.println("Reading file Done");
			System.out.println("Number of words inside the file : " + list.size());
			System.out.println("Maximum word Length : " + maxWordLength);
			System.out.println("Maximum word Length : " + minWordLength);
		} catch (FileNotFoundException e) {
			System.out.println("File not Found " + FILE_NAME_WORD_LIST);
		}
	}

	public boolean hasWordInList(String word) {
		return list.contains(word);
	}

	public int getMaxWordLength() {
		return maxWordLength;
	}

	public int getMinWordLength() {
		return minWordLength;
	}

	public static void generateData() throws FileNotFoundException {
		 write = new PrintWriter(FILE_NAME_PRE_COMPUTED_LIST);
		 generatePossiblePermutationForDictionary();
		 write.close();
		 joinFiles();
	}

	public static void joinFiles() throws FileNotFoundException {
		// number of files generated are 6
		File inputFile;
		PrintWriter mergedFile = new PrintWriter(FILE_NAME_PRE_COMPUTED_LIST_MERGED);
		for (int i = 1; i <= 6; i++) {
			String fileName = FILE_NAME_PRE_COMPUTED_PREFIX + "_" + i + ".txt";
			inputFile = new File(fileName);
			Scanner input = new Scanner(inputFile);
			while (input.hasNextLine()) {
				String line = input.nextLine();
				if (!line.trim().equals("")) {
					mergedFile.println(line);
				}
			}
			input.close();
		}
		mergedFile.close();
	}

	public static void generatePossiblePermutationForDictionary() {
		WordList dictionary = new WordList();
		int minLength = dictionary.getMinWordLength();
		int maxLength = dictionary.getMaxWordLength();
		for (String word : list) {
			// it required alot of time , so partitioning was required
			// for(int j=1;j<list.size();j++){
			// String word=list.get(j);
			int wordLength = word.length();

			if (wordLength > minLength) {
				for (int i = 0; i < wordLength; i++) {
					String newWord = removeCharfromString(word, i);
					List<String> permList = getListOfPermutationFromList(newWord);
					appendWordListPermutationTofile(newWord, permList);
				}
			}

			if (wordLength < maxLength) {
				for (char i = 'a'; i <= 'z'; i++) {
					String newWord = word + i;
					List<String> permList = getListOfPermutationFromList(newWord);
					appendWordListPermutationTofile(newWord, permList);
				}
			}
		}
	}

	public static void appendWordListPermutationTofile(String word, List<String> permList) {
		if (!permList.isEmpty()) {
			write.append(word + ":");
			for (String string : permList) {
				write.append(string + " ");
			}
			write.append("\n");
		}
	}

	public static List<String> getListOfPermutationFromList(String word) {
		ArrayList<String> permsInDic = new ArrayList<String>();
		for (String string : list) {
			if (isPermutation(word, string)) {
				permsInDic.add(string);
			}
		}
		return permsInDic;
	}

	public static boolean isPermutation(String a, String b) {
		if (a.length() != b.length()) {
			return false;
		}
		int[] strA = new int[26];

		for (int i = 0; i < a.length(); i++) {
			strA[a.charAt(i) - 'a']++;
			strA[b.charAt(i) - 'a']--;
		}
		for (int i = 0; i < strA.length; i++) {
			if (strA[i] != 0) {
				return false;
			}
		}
		return true;
	}

	public static String removeCharfromString(String word, int index) {
		StringBuilder string = new StringBuilder(word);
		string.deleteCharAt(index);
		return string.toString();
	}
}
