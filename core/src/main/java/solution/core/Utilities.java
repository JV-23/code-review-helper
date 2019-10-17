package solution.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utilities {
	
	public List<File> getSubFolders(String dirName) throws Exception {
		File directory = new File(dirName);
	
	    List<File> resultList = new ArrayList<File>();
	
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isDirectory()) {
	        	resultList.add(file.getAbsoluteFile());
	            resultList.addAll(getSubFolders(file.getAbsolutePath()));
	        }
	    }
	    
	    return resultList;	
	}
	
	public Map<String, String> testPerformance(List<File> folders) throws IOException {
		final Map<String, String> testTimes = new HashMap<String, String>();
		for(File f : folders) {
			Path startPath = Paths.get(f.getAbsolutePath());
			
			Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() { 
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
				try {
					if(file.getFileName().toString().endsWith(".xml")) {
						File xmlFile = new File(file.toString());
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document doc = dBuilder.parse(xmlFile);
						doc.normalize();
						NodeList nodeList = doc.getElementsByTagName("testcase");
						
						for(int i = 0; i < nodeList.getLength(); i++) {
							Node node = nodeList.item(i);
							
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								Element element = (Element) node;
								testTimes.put(element.getAttribute("name"), element.getAttribute("time"));

							}
						}
					}
					return FileVisitResult.CONTINUE;
				}
				catch(Exception e){
					e.printStackTrace();
					return FileVisitResult.CONTINUE;
				}
			}
				
			});
		
		}
		return testTimes;
	}
	
}
