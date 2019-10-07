package solution.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import solution.service.*;

/**
 * Hello world!
 *
 */
public class coreApp 
{
	final Map<String, String> stableTestTimes = new HashMap<String, String>();
	final Map<String, String> pullRequestTestTimes = new HashMap<String, String>();
	final Map<String, String> testDifferences = new HashMap<String, String>();

	public void stableVersionTestPerformance() throws Exception {
		Path startPath = Paths.get(System.getProperty("user.dir") + "\\stableVersion\\target\\surefire-reports");
		
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
							stableTestTimes.put(element.getAttribute("name"), element.getAttribute("time"));

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
	
	
	public void pullRequestVersionTestPerformance() throws Exception {
		Path startPath = Paths.get(System.getProperty("user.dir") + "\\PRVersion\\target\\surefire-reports");
		
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
								pullRequestTestTimes.put(element.getAttribute("name"), element.getAttribute("time"));

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
	
	
	public void compareTestTimes() {
		for(Map.Entry<String, String> stableEntry:stableTestTimes.entrySet()) {
			if(pullRequestTestTimes.containsKey(stableEntry.getKey())) {
				String s = pullRequestTestTimes.get(stableEntry.getKey());
				Double i = Double.parseDouble(s) - Double.parseDouble(stableEntry.getValue());
				testDifferences.put(stableEntry.getKey(), String.valueOf(i));
			}
		}
	}
	
	
	public Map<String, String> getStableVersionTestTimes() {
		return stableTestTimes;
	}
	

	public Map<String, String> getPullRequestTestTimes() {
		return pullRequestTestTimes;
	}
	
	public Map<String, String> getTestDifferences() {
		return testDifferences;
	}
	
    public static void main( String[] args )
    {
    	serviceApp gitService = new serviceApp();
    	coreApp coreService = new coreApp();
    	
        try {
			//gitService.downloadStableVersion();
			//gitService.runStableVersionTests();
			//gitService.downloadPRVersion();
			//gitService.runPRVersionTests();
			coreService.stableVersionTestPerformance();
			coreService.pullRequestVersionTestPerformance();
			coreService.compareTestTimes();
			System.out.println(coreService.getStableVersionTestTimes());
			System.out.println(coreService.getPullRequestTestTimes());
			System.out.println(coreService.getTestDifferences());

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
