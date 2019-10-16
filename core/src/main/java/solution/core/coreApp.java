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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
	static List<File> stableVersionFolders = new ArrayList<File>();
	static List<File> PRVersionFolders = new ArrayList<File>();
	
	public void stableVersionTestPerformance(List<File> directories) throws Exception {
		//get all the stable version folders
		Utilities util = new Utilities();
		stableVersionFolders = util.getSubFolders(System.getProperty("user.dir") + "\\stableVersion");
		
		//visit them and find test results
		for(File f : stableVersionFolders) {
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
	}
	
	
	public void pullRequestVersionTestPerformance() throws Exception {
		//get all the pull request version folders
		Utilities util = new Utilities();
		PRVersionFolders = util.getSubFolders(System.getProperty("user.dir") + "\\PRVersion");
		
		//visit them and find test results
		for(File f : PRVersionFolders) {
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
	
	public List<File> getStableVersionFolders(){
		return stableVersionFolders;
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
    	Utilities util = new Utilities();
    	
        try {
			//gitService.downloadStableVersion();
			//gitService.runStableVersionTests();
			//gitService.downloadPRVersion();
        	//gitService.runPRVersionTests();
        	coreService.stableVersionTestPerformance(stableVersionFolders);
			coreService.pullRequestVersionTestPerformance();
			coreService.compareTestTimes();
			//System.out.println(coreService.getStableVersionTestTimes());
			//System.out.println(coreService.getPullRequestTestTimes());
			System.out.println(coreService.getTestDifferences());

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
