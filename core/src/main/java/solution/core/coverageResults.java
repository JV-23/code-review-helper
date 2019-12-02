package solution.core;

public class coverageResults {
	private int missedInstructions;
	private int coveredInstructions;
	private int missedBranches;
	private int coveredBranches;
	private int missedComplexity;
	private int coveredComplexity;
	private int missedLines;
	private int coveredLines;
	private int missedMethods;
	private int coveredMethods;
	
	public coverageResults() {
		
	}
	
	public int getCoveredInstructions() {
		return coveredInstructions;
	}
	public void setCoveredInstructions(int coveredInstructions) {
		this.coveredInstructions = coveredInstructions;
	}
	public int getMissedInstructions() {
		return missedInstructions;
	}
	public void setMissedInstructions(int missedInstructions) {
		this.missedInstructions = missedInstructions;
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

	public int getCoveredComplexity() {
		return coveredComplexity;
	}

	public void setCoveredComplexity(int coveredComplexity) {
		this.coveredComplexity = coveredComplexity;
	}

	public int getMissedComplexity() {
		return missedComplexity;
	}

	public void setMissedComplexity(int missedComplexity) {
		this.missedComplexity = missedComplexity;
	}

	public int getCoveredLines() {
		return coveredLines;
	}

	public void setCoveredLines(int coveredLines) {
		this.coveredLines = coveredLines;
	}

	public int getMissedLines() {
		return missedLines;
	}

	public void setMissedLines(int missedLines) {
		this.missedLines = missedLines;
	}

	public int getMissedMethods() {
		return missedMethods;
	}

	public void setMissedMethods(int missedMethods) {
		this.missedMethods = missedMethods;
	}

	public int getCoveredMethods() {
		return coveredMethods;
	}

	public void setCoveredMethods(int coveredMethods) {
		this.coveredMethods = coveredMethods;
	}
	
	@Override
	public String toString() {
		String result = new String();
		result += "missed Instructions: " + this.getMissedInstructions() + "\n";
		result += "covered Instructions: " + this.getCoveredInstructions() + "\n";
		result += "missed Branches: " + this.getMissedBranches() + "\n";
		result += "covered Branches: " + this.getCoveredBranches() + "\n";
		result += "missed Complexity: " + this.getMissedComplexity() + "\n";
		result += "covered Complexity: " + this.getCoveredComplexity() + "\n";
		result += "missed Lines: " + this.getMissedLines() + "\n";
		result += "covered Lines: " + this.getCoveredLines() + "\n";
		result += "missed Methods: " + this.getMissedMethods() + "\n";
		result += "covered Methods: " + this.getCoveredMethods() + "\n";
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(!(o instanceof coverageResults))
			return false;
		
		coverageResults c = (coverageResults) o;
		
		if(this.getMissedInstructions() != c.getMissedInstructions())
			return false;
		
		if(this.getCoveredInstructions() != c.getCoveredInstructions())
			return false;
		
		if(this.getMissedBranches() != c.getMissedBranches())
			return false;
		
		if(this.getCoveredBranches() != c.getCoveredBranches())
			return false;
		
		if(this.getMissedComplexity() != c.getMissedComplexity())
			return false;
		
		if(this.getCoveredComplexity() != c.getCoveredComplexity())
			return false;
		
		if(this.getMissedLines() != c.getMissedLines())
			return false;

		if(this.getCoveredLines() != c.getCoveredLines())
			return false;
		
		if(this.getMissedMethods() != c.getMissedMethods())
			return false;
		
		if(this.getCoveredMethods() != c.getCoveredMethods())
			return false;
		
		return true;
	}

}
