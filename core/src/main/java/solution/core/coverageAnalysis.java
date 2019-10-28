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
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	
	public coverageResults parseReports(final File folder, coverageResults results) throws Exception {
		Document doc;
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				parseReports(fileEntry, results);
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
							Element element = (Element) node;
							NodeList nL2 = node.getChildNodes();
							for(int j = 0; j < nL2.getLength(); j++) {
								Node node2 = nL2.item(j);
								NodeList nL3 = node2.getChildNodes();
								element = (Element) node2;
								for(int k = 0 ; k < nL3.getLength(); k++) {
									Node node3 = nL3.item(k);
									element = (Element) node3;
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
								}
							}
						}
					}
				}
			}
		}
		return results;
	}
}
