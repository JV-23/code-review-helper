package solution.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class OutputCoverage{
	public void output(Map<String, coverageResults> results, String filename) {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = JsonNodeFactory.instance.arrayNode();
		JsonNode node = mapper.createObjectNode();
		JsonNode parent = mapper.createObjectNode();
		((ObjectNode)parent).put("name", "project");
		//((ObjectNode)node).put("key", "coverage");
		
		for (Map.Entry<String, coverageResults> entry : results.entrySet()) {
			JsonNode child = mapper.createObjectNode();
			((ObjectNode)node).put("name", entry.getKey());
			ArrayNode a = JsonNodeFactory.instance.arrayNode(); 
			
			((ObjectNode)child).put("name", "Covered Instructions: " + entry.getValue().getCoveredInstructions());
			((ObjectNode)child).put("size", entry.getValue().getCoveredInstructions());
			a.add(child.deepCopy());
			
			((ObjectNode)child).put("name", "Missed Instructions: " + entry.getValue().getMissedInstructions());
			((ObjectNode)child).put("size", entry.getValue().getMissedInstructions());
			a.add(child.deepCopy());
			
			((ObjectNode)child).put("name", "Covered Branches: " + entry.getValue().getCoveredBranches());
			((ObjectNode)child).put("size", entry.getValue().getCoveredBranches());
			a.add(child.deepCopy());
			((ObjectNode)child).put("name", "Missed Branches: " + entry.getValue().getMissedBranches());
			((ObjectNode)child).put("size", entry.getValue().getMissedBranches());
			a.add(child.deepCopy());
			
			((ObjectNode)child).put("name", "Covered Complexity: " + entry.getValue().getCoveredComplexity());
			((ObjectNode)child).put("size", entry.getValue().getCoveredComplexity());
			a.add(child.deepCopy());
			((ObjectNode)child).put("name", "Missed Complexity: " + entry.getValue().getMissedComplexity());
			((ObjectNode)child).put("size", entry.getValue().getMissedComplexity());
			a.add(child.deepCopy());
			
			((ObjectNode)child).put("name", "Covered Lines: " + entry.getValue().getCoveredLines());
			((ObjectNode)child).put("size", entry.getValue().getCoveredLines());
			a.add(child.deepCopy());
			((ObjectNode)child).put("name", "Missed Lines: " + entry.getValue().getMissedLines());
			((ObjectNode)child).put("size", entry.getValue().getMissedLines());
			a.add(child.deepCopy());
			
			((ObjectNode)child).put("name", "Covered Methods: " + entry.getValue().getCoveredMethods());
			((ObjectNode)child).put("size", entry.getValue().getCoveredMethods());
			a.add(child.deepCopy());
			((ObjectNode)child).put("name", "Missed Methods: " + entry.getValue().getMissedMethods());
			((ObjectNode)child).put("size", entry.getValue().getMissedMethods());
			a.add(child.deepCopy());
			
						
			((ObjectNode)node).set("children", a);
			
			array.add(node.deepCopy());
		}
		((ObjectNode)parent).set("children", array);
		//((ObjectNode)node).set("values", array);

		String str = parent.toString();
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
