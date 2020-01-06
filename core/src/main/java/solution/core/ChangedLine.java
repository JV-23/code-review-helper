package solution.core;

public class ChangedLine {
	private int lineNumber;
	private int missedInstructions;
	private int coveredInstructions;
	private int missedBranches;
	private int coveredBranches;
	private String change;
	private String filename;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public int getMissedInstructions() {
		return missedInstructions;
	}
	public void setMissedInstructions(int missedInstructions) {
		this.missedInstructions = missedInstructions;
	}
	public int getCoveredInstructions() {
		return coveredInstructions;
	}
	public void setCoveredInstructions(int coveredInstructions) {
		this.coveredInstructions = coveredInstructions;
	}
	public int getMissedBranches() {
		return missedBranches;
	}
	public void setMissedBranches(int missedBranches) {
		this.missedBranches = missedBranches;
	}
	public int getCoveredBranches() {
		return coveredBranches;
	}
	public void setCoveredBranches(int coveredBranches) {
		this.coveredBranches = coveredBranches;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}

	@Override
	public String toString() {
		String result = new String();
		result += "missed Instructions: " + this.getMissedInstructions() + "\n";
		result += "covered Instructions: " + this.getCoveredInstructions() + "\n";
		result += "missed Branches: " + this.getMissedBranches() + "\n";
		result += "covered Branches: " + this.getCoveredBranches() + "\n";
		result += "line Number: " + this.getLineNumber() + "\n";
		result += "filename: " + this.getFilename() + "\n";
		result += "change: " + this.getChange() + "\n";
		return result;
	}
}
