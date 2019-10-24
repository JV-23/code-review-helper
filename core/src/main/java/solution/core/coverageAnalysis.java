package solution.core;

import java.io.BufferedReader;
import java.io.File;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	
	public void parseReports() throws Exception {
		Utilities util = new Utilities();
		List<File> folders = new ArrayList<File>();
		folders = util.getSubFolders(System.getProperty("user.dir") + "\\stableVersion");
		
		for(File f : folders) {
			Path startPath = Paths.get(f.getAbsolutePath());
			
			Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() { 
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
				try {
					//System.out.println(file.getParent().toString() + file.getFileName().toString());
					if(file.getFileName().toString().endsWith("jacoco.xml")) {
						System.out.println(file.getParent().toString() + file.getFileName().toString());
						File xmlFile = new File(file.toString());
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document doc = dBuilder.parse(xmlFile);
						doc.normalize();
						
						int aux = 0;
						
						NodeList nodeList = doc.getElementsByTagName("class");
						int missed = 0;
						int covered = 0;
						for(int i = 0; i < nodeList.getLength(); i++) {
							Node node = nodeList.item(i);
														
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								Element element = (Element) node;
								//if(element.getAttribute("name").equals("io/github/bonigarcia/wdm/Shell")){
									NodeList nL2 = node.getChildNodes();
									for(int j = 0; j < nL2.getLength(); j++) {
										Node node2 = nL2.item(j);
										NodeList nL3 = node2.getChildNodes();
										element = (Element) node2;
										//System.out.println(element.getAttribute("name").toString());
										for(int k = 0 ; k < nL3.getLength(); k++) {
											Node node3 = nL3.item(k);
											element = (Element) node3;
											if(element.getAttribute("type").equals("INSTRUCTION")){
												missed += Integer.parseInt(element.getAttribute("missed"));
												covered += Integer.parseInt(element.getAttribute("covered"));
											}
										}
									}
								//}
							}
						}
						System.out.println("covered" + covered);
						System.out.println("missed" + missed);
						System.out.println("aux" + aux);
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
}
