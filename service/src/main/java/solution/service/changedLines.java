package solution.service;

import java.util.Map;

public class changedLines {
	public String filename;
	public Map<Integer, String> changes;
	
	public changedLines(String file, Map<Integer, String> change) {
		filename = file;
		changes = change;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Map<Integer, String> getChanges() {
		return changes;
	}
	public void setChanges(Map<Integer, String> changes) {
		this.changes = changes;
	}
	
	@Override
	public String toString() {
		String result = new String();
		result += "file: " + this.getFilename() + "\n";
		result += "changes: " + this.getChanges() + "\n";
		return result;
	}
}
