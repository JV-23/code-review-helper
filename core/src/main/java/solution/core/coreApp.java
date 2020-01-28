package solution.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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
	
	static Set<String> relatedAreas = new HashSet<String>();
	
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
				testDifferences.put(stableEntry.getKey(), "???");
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
	
	@SuppressWarnings("restriction")
	public void generateTrace(String recordingLocation, String outputName) throws IOException {
		File file = new File(recordingLocation);
    	Path path = file.toPath();
    	//Recording r = new Recording();
    	String s = new String();
    	System.out.println("Processing recording...");
    	for (RecordedEvent event : RecordingFile.readAllEvents(path)) {
    		if(event.getStackTrace() != null) {
    			s += event.getStackTrace().toString();
    		}
    	}
    	System.out.println("Recording processed, writing to file");
    	BufferedWriter writer;
		writer = new BufferedWriter(new FileWriter(outputName));

    	Scanner scanner = new Scanner(s);
    	while (scanner.hasNextLine()) {
    	  String line = scanner.nextLine();
    	 if(!line.equals("{") && !line.equals("}") && !line.equals("  truncated = false") && !line.equals("  frames = [") && !line.equals("  truncated = true")) {
    		 line += " 1";
    		 String replaced = line.replaceAll(",     ", ";");
    		 writer.write(replaced + "\n");
    		 
    	 }
    	}
    	writer.close();
    	scanner.close();

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
    	coverageAnalysis coverage = new coverageAnalysis();
    	OutputCoverage oc = new OutputCoverage();
    	OutputTestTimes ott = new OutputTestTimes();
    	
        try {
        	Scanner in = new Scanner(System.in);
        	System.out.println("Please enter option (1 - download repos; 2 - run analysis): ");
        	int option = in.nextInt();
        	
        	switch(option) {
        		case 1: 
        			repo = gitService.downloadStableVersion();
        			pullRequestNumber = gitService.downloadPRVersion();
        			break;
        		case 2:
        			Scanner in2 = new Scanner(System.in);
        			System.out.println("Please enter link to repository: ");
        			String repo = in2.nextLine();
        			System.out.println("Please enter pull request number: ");
        			int number = in2.nextInt();
        			/*gitService.runStableVersionTests();
                	gitService.runPRVersionTests();
                	
                	coreService.stableVersionTestPerformance();
                	coreService.pullRequestVersionTestPerformance();
                	coreService.compareTestTimes();
                	
        			ott.output(coreService.getStableVersionTestTimes(), "stableTestTimes.json");
                	ott.output(coreService.getTestDifferences(), "timeDifferences.json");

                	coreService.generateTrace(System.getProperty("user.dir") + "\\PRVersion\\recording.jfr", "prprofile");
                	coreService.generateTrace(System.getProperty("user.dir") + "\\stableVersion\\recording.jfr", "stableprofile");
					*/
        			
                	//coverage.generateReports();
                	stableCoverage = coverage.parseReports(new File(System.getProperty("user.dir") + "\\stableVersion"), new HashMap<String, coverageResults>());
            		pullRequestCoverage = coverage.parseReports(new File(System.getProperty("user.dir") + "\\PRVersion"), new HashMap<String, coverageResults>());
            		oc.output(stableCoverage, "stableCoverage.json");
            		oc.output(pullRequestCoverage, "pullRequestCoverage.json");

            		coverageDifference = coverage.difference(stableCoverage, pullRequestCoverage);
            		oc.output(coverageDifference, "coverageDifference.json");
                	
                	areChangesCovered = coverage.checkIfChangesAreCovered(repo, number);
                	//areChangesCovered = coverage.checkIfChangesAreCovered("https://github.com/bonigarcia/webdrivermanager", 414);
                	
                	//gitService.retrieveRepositoryFilesNames(null, "Preferences.java");
                	//gitService.retrieveRepositoryFilesNames(null, "HttpClient.java");
                	
                	//System.out.println(gitService.getChangedFiles());
                	//System.out.println(coverageDifference);
                	//System.out.println("-----------------");
                	//System.out.println(areChangesCovered);
                	
                	relatedAreas = coverage.findDeadCode(coverageDifference, areChangesCovered, gitService, repo);

                	oc.outputRelatedFiles(relatedAreas, repo);
                	
                	for(String s : relatedAreas) {
                		String file[] = s.split("/");
                		int i=1;
                		Map<Integer, String> fileContents = new HashMap<Integer, String>(); 
                		BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\stableVersion\\" + s));
            			String line = reader.readLine();
            			while (line != null) {
            				//System.out.println(i + "   " + line);
            				fileContents.put(i, line);
            				i++;
            				// read next line
            				line = reader.readLine();
            			}
            			reader.close();
                		//System.out.println(s);
                		//System.out.println(fileContents.get(73));
                		String filename = file[file.length-1];
                		
                		List<ChangedLine> lines = new ArrayList<ChangedLine>();
                		List<ChangedLine> outputStable = coverage.differenceInFile(new File(System.getProperty("user.dir") + "\\stableVersion"), filename, lines);
                		
                		List<ChangedLine> lines2 = new ArrayList<ChangedLine>();
                		List<ChangedLine> outputPR = coverage.differenceInFile(new File(System.getProperty("user.dir") + "\\PRVersion"), filename, lines2);
                		
                		List<ChangedLine> coverageChanges = new ArrayList<ChangedLine>();
                		for(ChangedLine c : outputStable) {
                			for(ChangedLine c2 : outputPR) {
                				if(c.getLineNumber() == c2.getLineNumber()) {
                					ChangedLine o = c2.difference(c);
                					if(o.getCoveredBranches() != 0 || o.getCoveredInstructions() != 0 || o.getMissedBranches() != 0 || o.getMissedInstructions() != 0) {
                						coverageChanges.add(o);
                					}
                				}
                			}
                		}
                		//System.out.println(coverageChanges);
                		
                		Map<Integer, ChangedLine> toOutput = new HashMap<Integer, ChangedLine>();
                		for(ChangedLine c : coverageChanges) {
                			ChangedLine change = new ChangedLine();
            				change.setChange(fileContents.get(c.getLineNumber()));
            				change.setFilename(filename);
            				change.setLineNumber(c.getLineNumber());
            				change.setCoveredBranches(c.getCoveredBranches());
            				change.setCoveredInstructions(c.getCoveredInstructions());
            				change.setMissedBranches(c.getMissedBranches());
            				change.setMissedInstructions(c.getMissedInstructions());
            				toOutput.put(c.getLineNumber(), change);

                			
                		}
                		for(ChangedLine c : coverageChanges) {
                			int aux = -3;
                			while(aux < 4) {
                				ChangedLine change = new ChangedLine();
                				change.setChange(fileContents.get(c.getLineNumber() + aux));
                				change.setFilename(filename);
                				change.setLineNumber(c.getLineNumber() + aux);
                				change.setCoveredBranches(0);
                				change.setCoveredInstructions(0);
                				change.setMissedBranches(0);
                				change.setMissedInstructions(0);
                				toOutput.putIfAbsent(c.getLineNumber() + aux, change);
                    			aux++;

                				
                				
                			}
                		}
                		System.out.println(toOutput);
                		
                	}
                	
        	}

        	/*
			gitService.runStableVersionTests();
        	gitService.runPRVersionTests();
        	*/
        	//coreService.stableVersionTestPerformance();
        	//coreService.pullRequestVersionTestPerformance();
        	//coreService.compareTestTimes();
        	
			//System.out.println(coreService.getStableVersionTestTimes());
			//System.out.println(coreService.getPullRequestTestTimes());
			//System.out.println(coreService.getTestDifferences());
			
			//ott.output(coreService.getStableVersionTestTimes(), "stableTestTimes.json");
        	//ott.output(coreService.getTestDifferences(), "timeDifferences.json");
			
        	//coreService.generateTrace(System.getProperty("user.dir") + "\\PRVersion\\recording.jfr", "prprofile");
        	//coreService.generateTrace(System.getProperty("user.dir") + "\\stableVersion\\recording.jfr", "stableprofile");
        	

        	        	
        	
        	//coverage.generateReports();
        	/*stableCoverage = coverage.parseReports(new File(System.getProperty("user.dir") + "\\stableVersion"), new HashMap<String, coverageResults>());
    		pullRequestCoverage = coverage.parseReports(new File(System.getProperty("user.dir") + "\\PRVersion"), new HashMap<String, coverageResults>());
    		oc.output(stableCoverage, "stableCoverage.json");
    		oc.output(pullRequestCoverage, "pullRequestCoverage.json");
    		//System.out.println(pullRequestCoverage);
    		coverageDifference = coverage.difference(stableCoverage, pullRequestCoverage);
    		oc.output(coverageDifference, "coverageDifference.json");
        	
        	//areChangesCovered = coverage.checkIfChangesAreCovered(repo, pullRequestNumber);
        	areChangesCovered = coverage.checkIfChangesAreCovered("https://github.com/bonigarcia/webdrivermanager", 414);
        	
        	//gitService.retrieveRepositoryFilesNames(null, "Preferences.java");
        	//gitService.retrieveRepositoryFilesNames(null, "HttpClient.java");
        	
        	//System.out.println(gitService.getChangedFiles());
        	/*System.out.println(coverageDifference);
        	System.out.println("-----------------");
        	System.out.println(areChangesCovered);
        	*/
        	//coverage.findDeadCode(coverageDifference, areChangesCovered, gitService);
        	//coreService.runDduMetric();
        	
        	
        	
    	}
        catch (Exception e) {
			e.printStackTrace();
		}
    }
}
