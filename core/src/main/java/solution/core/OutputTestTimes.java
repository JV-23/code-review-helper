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

public class OutputTestTimes {
	public void output(Map<String, String> results, String filename) {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = JsonNodeFactory.instance.arrayNode();
		JsonNode node = mapper.createObjectNode();
		
		for (Map.Entry<String, String> entry : results.entrySet()) {
			JsonNode child = mapper.createObjectNode();
			((ObjectNode)node).put("name", entry.getKey());
			((ObjectNode)node).put("status", entry.getValue());
			array.add(node.deepCopy());
		}

		String str = array.toString();
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
