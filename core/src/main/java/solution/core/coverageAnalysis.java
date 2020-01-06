package solution.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import solution.service.changedLines;
import solution.service.serviceApp;

public class coverageAnalysis {
	
	public void generateReports() throws Exception{
		ProcessBuilder proc = new ProcessBuilder();
        proc.command("cmd.exe", "/c", "cd stableVersion && mvn jacoco:report");
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
        proc.command("cmd.exe", "/c", "cd PRVersion && mvn jacoco:report");
        proc.redirectErrorStream(true);
        process = proc.start();

        // Read the output

        reader =  
              new BufferedReader(new InputStreamReader(process.getInputStream()));

        line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
        }

        process.waitFor();
	}
	
	public Map<String, coverageResults> parseReports(final File folder, Map<String, coverageResults> coverage) throws Exception {
		Document doc;
		coverageResults results = new coverageResults();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				parseReports(fileEntry, coverage);
			} 
			else {
				if(fileEntry.getName().endsWith("jacoco.xml")) {
					File xmlFile = new File(fileEntry.toString());
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					try{
						doc = dBuilder.parse(xmlFile);
					}
					catch(FileNotFoundException e) {
						File source = new File(System.getProperty("user.dir") + "\\report.dtd");
						File dest = new File(fileEntry.getParent() + "\\report.dtd");
					    FileUtils.copyFile(source, dest);
					    doc = dBuilder.parse(xmlFile);
					}
					doc.normalize();
											
					NodeList nodeList = doc.getElementsByTagName("class");

					for(int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
													
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element e = (Element) node;
							NodeList nL2 = node.getChildNodes();
							results = new coverageResults();
							for(int j = 0; j < nL2.getLength(); j++) {
								Node node2 = nL2.item(j);
								NodeList nL3 = node2.getChildNodes();
								for(int k = 0 ; k < nL3.getLength(); k++) {
									Node node3 = nL3.item(k);
									Element element = (Element) node3;
									if(element.getAttribute("type").equals("INSTRUCTION")){
										results.setMissedInstructions(results.getMissedInstructions() + Integer.parseInt(element.getAttribute("missed")));
										results.setCoveredInstructions(results.getCoveredInstructions() + Integer.parseInt(element.getAttribute("covered")));
									}
									if(element.getAttribute("type").equals("BRANCH")){
										results.setMissedBranches(results.getMissedBranches() + Integer.parseInt(element.getAttribute("missed")));
										results.setCoveredBranches(results.getCoveredBranches() + Integer.parseInt(element.getAttribute("covered")));
									}
									if(element.getAttribute("type").equals("COMPLEXITY")){
										results.setMissedComplexity(results.getMissedComplexity() + Integer.parseInt(element.getAttribute("missed")));
										results.setCoveredComplexity(results.getCoveredComplexity() + Integer.parseInt(element.getAttribute("covered")));
									}
									if(element.getAttribute("type").equals("LINE")){
										results.setMissedLines(results.getMissedLines() + Integer.parseInt(element.getAttribute("missed")));
										results.setCoveredLines(results.getCoveredLines() + Integer.parseInt(element.getAttribute("covered")));
									}
									if(element.getAttribute("type").equals("METHOD")){
										results.setMissedMethods(results.getMissedMethods() + Integer.parseInt(element.getAttribute("missed")));
										results.setCoveredMethods(results.getCoveredMethods() + Integer.parseInt(element.getAttribute("covered")));
									}
									//System.out.println(e.getAttribute("name"));
								}
								coverage.put(e.getAttribute("name"), results);
							}
						}
					}
				}
			}
		}
		return coverage;
	}
	
	
	public List<ChangedLine> checkIfChangesAreCovered(String repo, int pullRequestNumber) {
		List<ChangedLine> result = new ArrayList<ChangedLine>();
		serviceApp gitService = new serviceApp();
		List<changedLines> lines = null;
		try {
			lines = gitService.getPullRequestChanges(repo, pullRequestNumber);
			//System.out.println(lines);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(lines);
		/*try {
			parseChanges(new File(System.getProperty("user.dir") + "\\PRVersion"), lines, result);
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return result;
	}
	
	public List<ChangedLine> parseChanges(File folder, List<changedLines> lines, List<ChangedLine> result) throws Exception {
		Document doc;
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				parseChanges(fileEntry, lines, result);
			} 
			else {
				if(fileEntry.getName().endsWith("jacoco.xml")) {
					File xmlFile = new File(fileEntry.toString());
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					try{
						doc = dBuilder.parse(xmlFile);
					}
					catch(FileNotFoundException e) {
						File source = new File(System.getProperty("user.dir") + "\\report.dtd");
						File dest = new File(fileEntry.getParent() + "\\report.dtd");
					    FileUtils.copyFile(source, dest);
					    doc = dBuilder.parse(xmlFile);
					}
					doc.normalize();
					
					for(changedLines changed : lines) {
						String file = changed.getFilename();
						Map<Integer, String> changes = changed.getChanges();
						String[] s = file.split("/");
						file = s[s.length-1];

						NodeList nodeList = doc.getElementsByTagName("sourcefile");
						for(int i = 0; i < nodeList.getLength(); i++) {
							Node node = nodeList.item(i);
							Element element = (Element) node;
							if(element.getAttribute("name").equals(file)) {
								for(Map.Entry<Integer, String> entry : changed.getChanges().entrySet()) {
									NodeList nodeList2 = node.getChildNodes();
									for(int j = 0; j < nodeList2.getLength(); j++) {
										Node node2 = nodeList2.item(j);
										Element element2 = (Element) node2;
										if(element2.getAttribute("nr").equals(entry.getKey().toString())) {
											ChangedLine changed2 = new ChangedLine();
											changed2.setLineNumber(Integer.parseInt(element2.getAttribute("nr")));
											changed2.setCoveredInstructions(Integer.parseInt(element2.getAttribute("ci")));
											changed2.setMissedInstructions(Integer.parseInt(element2.getAttribute("mi")));
											changed2.setCoveredBranches(Integer.parseInt(element2.getAttribute("cb")));
											changed2.setMissedBranches(Integer.parseInt(element2.getAttribute("mb")));
											changed2.setFilename(file);
											for(Map.Entry<Integer,String> e : changes.entrySet()) {
												if(e.getKey().equals(Integer.parseInt(element2.getAttribute("nr")))) {
													changed2.setChange(e.getValue());
												}
												
											}
											result.add(changed2);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	public Map<String, coverageResults> difference(Map<String, coverageResults> beforeMap, Map<String, coverageResults> afterMap) {
		Map<String, coverageResults> diff = new HashMap<String, coverageResults>();
		Entry<String, coverageResults> newEntry;
		coverageResults results;
		
		Set<String> removedKeys = new HashSet<String>(beforeMap.keySet());
		removedKeys.removeAll(afterMap.keySet());

		Set<String> addedKeys = new HashSet<String>(afterMap.keySet());
		addedKeys.removeAll(beforeMap.keySet());

		Set<Entry<String, coverageResults>> changedEntries = new HashSet<Entry<String, coverageResults>>(afterMap.entrySet());
		changedEntries.removeAll(beforeMap.entrySet());

		for(Entry<String, coverageResults> entry : changedEntries) {
			for(Map.Entry<String, coverageResults> entry2 : beforeMap.entrySet()) {
				if(entry2.getKey().equals(entry.getKey())) {
					results = entry.getValue().difference(entry2.getValue());
					diff.put(entry2.getKey(), results);
				}
			}
		}	
		
		return diff;
			
		//System.out.println("added " + addedKeys);
		//System.out.println("removed " + removedKeys);
		//System.out.println("changed " + changedEntries);
	}

}
