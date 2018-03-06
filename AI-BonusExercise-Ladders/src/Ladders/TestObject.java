package Ladders;

public class TestObject {
	private String initialWord;
	private String goalWord;
	private String outputFileName;

	public TestObject(String initialWord, String goalWord, String outputFileName) {
		super();
		this.initialWord = initialWord;
		this.goalWord = goalWord;
		this.outputFileName = outputFileName;
	}

	public String getInitialWord() {
		return initialWord;
	}

	public void setInitialWord(String initialWord) {
		this.initialWord = initialWord;
	}

	public String getGoalWord() {
		return goalWord;
	}

	public void setGoalWord(String goalWord) {
		this.goalWord = goalWord;
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
}
