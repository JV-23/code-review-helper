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
	Map<String, String> stableTestTimes = new HashMap<String, String>();
	Map<String, String> pullRequestTestTimes = new HashMap<String, String>();
	Map<String, String> testDifferences = new HashMap<String, String>();
	static List<File> stableVersionFolders = new ArrayList<File>();
	static List<File> PRVersionFolders = new ArrayList<File>();
	
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
		System.out.println("-----------");
		for(Map.Entry<String, String> pullRequestEntry:pullRequestTestTimes.entrySet()){
			if(stableTestTimes.containsKey(pullRequestEntry.getKey())){
				;
			}
			else {
				System.out.println(pullRequestEntry);
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
        	coreService.stableVersionTestPerformance();
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
