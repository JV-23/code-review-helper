package solution.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
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
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jdk.jfr.Recording;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

import solution.service.*;
//import solution.presentation.*;

/**
 * Hello world!
 *
 */
public class coreApp 
{
	static String repo;
	static int pullRequestNumber;
	
	Map<String, String> stableTestTimes = new HashMap<String, String>();
	Map<String, String> pullRequestTestTimes = new HashMap<String, String>();
	Map<String, String> testDifferences = new HashMap<String, String>();
	static List<File> stableVersionFolders = new ArrayList<File>();
	static List<File> PRVersionFolders = new ArrayList<File>();
	
	static Map<String, coverageResults> stableCoverage;
	static Map<String, coverageResults> pullRequestCoverage;
	static Map<String, coverageResults> coverageDifference;
	
	static List<ChangedLine> areChangesCovered;
	
	public void stableVersionTestPerformance() throws Exception {
		//get all the stable version folders
		Utilities util = new Utilities();
		stableVersionFolders = util.getSubFolders(System.getProperty("user.dir") + "\\stableVersion");
		
		stableTestTimes = util.testPerformance(stableVersionFolders);
	}
	

	public void pullRequestVersionTestPerformance() throws Exception {
		//get all the pull request version folders
		Utilities util = new Utilities();
		PRVersionFolders = util.getSubFolders(System.getProperty("user.dir") + "\\PRVersion");
		
		pullRequestTestTimes = util.testPerformance(PRVersionFolders);
	}
	
	
	public void compareTestTimes() {
		for(Map.Entry<String, String> stableEntry:stableTestTimes.entrySet()) {
			if(pullRequestTestTimes.containsKey(stableEntry.getKey())) {
				String s = pullRequestTestTimes.get(stableEntry.getKey());
				Double i = Double.parseDouble(s) - Double.parseDouble(stableEntry.getValue());
				testDifferences.put(stableEntry.getKey(), String.valueOf(i));
			}
			else {
				System.out.println(stableEntry);
			}
		}
	}
	
	public void runDduMetric() throws Exception {
		ProcessBuilder proc = new ProcessBuilder();
		proc.command("cmd.exe", "/c", "cd stableVersion && mvn ddu:test");
		proc.redirectErrorStream(true);
		Process process = proc.start();

		// Read the output
		
		BufferedReader reader =  
		      new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String line = "";
		while((line = reader.readLine()) != null) {
		    System.out.print(line + "\n");
		}
		
		process.waitFor();
		
		proc = new ProcessBuilder();
		proc.command("cmd.exe", "/c", "cd PRVersion && mvn ddu:test");
		proc.redirectErrorStream(true);
		process = proc.start();

		// Read the output
		
		reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		line = "";
		while((line = reader.readLine()) != null) {
		    System.out.print(line + "\n");
		}
		
		process.waitFor();
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
	
	@SuppressWarnings("restriction")
	public static void main( String[] args )
    {	
    	serviceApp gitService = new serviceApp();
    	coreApp coreService = new coreApp();
    	Utilities util = new Utilities();
    	coverageAnalysis coverage = new coverageAnalysis();
    	OutputCoverage oc = new OutputCoverage();
    	OutputTestTimes ott = new OutputTestTimes();
    	
        try {
			//repo = gitService.downloadStableVersion();
			//pullRequestNumber = gitService.downloadPRVersion();

			/*gitService.runStableVersionTests();
        	gitService.runPRVersionTests();
        	
        	coreService.stableVersionTestPerformance();
        	coreService.pullRequestVersionTestPerformance();
        	coreService.compareTestTimes();
        	
			//System.out.println(coreService.getStableVersionTestTimes());
			//System.out.println(coreService.getPullRequestTestTimes());
			//System.out.println(coreService.getTestDifferences());
			
			ott.output(coreService.getStableVersionTestTimes(), "stableTestTimes.json");
        	ott.output(coreService.getTestDifferences(), "timeDifferences.json");
			
			*/
        	//coverage.generateReports();
        	stableCoverage = coverage.parseReports(new File(System.getProperty("user.dir") + "\\stableVersion"), new HashMap<String, coverageResults>());
    		pullRequestCoverage = coverage.parseReports(new File(System.getProperty("user.dir") + "\\PRVersion"), new HashMap<String, coverageResults>());
    		//oc.output(stableCoverage, "stableCoverage.json");
    		//oc.output(pullRequestCoverage, "pullRequestCoverage.json");
    		//System.out.println(pullRequestCoverage);
    		coverageDifference = coverage.difference(stableCoverage, pullRequestCoverage);
    		//oc.output(coverageDifference, "coverageDifference.json");
        	
        	//areChangesCovered = coverage.checkIfChangesAreCovered(repo, pullRequestNumber);
        	areChangesCovered = coverage.checkIfChangesAreCovered("https://github.com/bonigarcia/webdrivermanager", 414);
        	
        	System.out.println(areChangesCovered);
        	
        	//coreService.runDduMetric();
        	 
        	
        	
        	/*File file = new File(System.getProperty("user.dir") + "\\PRVersion\\recording.jfr");
        	Path path = file.toPath();
        	//Recording r = new Recording();
        	String s = new String();
        	System.out.println("here");
        	for (RecordedEvent event : RecordingFile.readAllEvents(path)) {
        		if(event.getStackTrace() != null) {
        			s += event.getStackTrace().toString();
        		}
        	}
        	System.out.println("heree");
        	
        	Scanner scanner = new Scanner(s);
        	while (scanner.hasNextLine()) {
        	  String line = scanner.nextLine();
        	 if(!line.equals("{") && !line.equals("}") && !line.equals("  truncated = false") && !line.equals("  frames = [") && !line.equals("  truncated = true")) {
        		 line += " 1";
        		 String replaced = line.replaceAll(",     ", ";");
        		 System.out.println(replaced);
        		 
        	 }
        	}
        	scanner.close();*/
        	
        	//presentationApp view = new presentationApp();
        	//view.helloWorld();
    	}
        catch (Exception e) {
			e.printStackTrace();
		}
    }
}
