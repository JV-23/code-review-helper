package solution.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Hello world!
 *
 */
public class serviceApp 
{
	public void downloadStableVersion() throws Exception{
		Console cnsl = null;
		String username = null;
		char[] pwd = null;
		String repoUrl = null;
	      
		try {
			// creates a console object
			cnsl = System.console();
			
			if (cnsl != null) {
			   // read line from the user input
				username = cnsl.readLine("Insert your GitHub Username: ");
				// read password into the char array
				pwd = cnsl.readPassword("Insert your GitHub Password: ");
				//read the url of the repository
				repoUrl = cnsl.readLine("Insert the url of the repository you want to access:");
			} 
			     
		} 
		catch(Exception ex) {
			ex.printStackTrace();      
		}
		
		CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, pwd);
		String cloneDirectoryPath = System.getProperty("user.dir") + "\\stableVersion";
				
		try {
		    System.out.println("Cloning "+ repoUrl +" into stableVersion");
		    Git.cloneRepository()
	        	.setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
		        .setURI(repoUrl)
		        .setDirectory(Paths.get(cloneDirectoryPath).toFile())
		        .setCredentialsProvider(credentialsProvider)
		        .call();
		    System.out.println("Completed Cloning");
		} catch (GitAPIException e) {
		    System.out.println("Exception occurred while cloning repo (probably a wrong password, or you"
		    		+ " don't have the necessary permissions to acess the repo)");
		}
	}
	
	
	public void runStableVersionTests() throws Exception{
        //String command = "cmd.exe /c cd stableVersion && mvn test";
        
        ProcessBuilder proc = new ProcessBuilder();
        proc.command("cmd.exe", "/c", "cd stableVersion && mvn test -fae");
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
	}
	
	
	public void downloadPRVersion() throws Exception{
		 	String command = "cmd.exe /c robocopy stableVersion PRVersion /MIR";
	        
	        Process proc = Runtime.getRuntime().exec(command);

	        System.out.println("Copying files:");

	        // Read the output
	        BufferedReader reader =  new BufferedReader(new InputStreamReader(proc.getInputStream()));

	        String line = "";
	        while((line = reader.readLine()) != null) {
	            System.out.println(line);
	        }
	        
	        proc.waitFor();
	        
	        Scanner scan = new Scanner(System.in);
	        System.out.println("Enter the number of the pull request you want to analyze:");
	        
	        String myNumber = scan.nextLine();
	  
	        ProcessBuilder processBuilder = new ProcessBuilder();
	        processBuilder.command("cmd.exe", "/c", "cd PRVersion && git fetch origin pull/" + myNumber + "/head && git checkout -b pull-request FETCH_HEAD");
	        
	        try {
	        	processBuilder.redirectErrorStream(true);
	        	Process process = processBuilder.start();
	        	reader =  new BufferedReader(new InputStreamReader(process.getInputStream()));
	        	line = "";
		        while((line = reader.readLine()) != null) {
		            System.out.println(line);
		        }
		        
		        process.waitFor();

	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
	}
	
	
	public void runPRVersionTests() throws Exception{
        //String command = "cmd.exe /c cd PRVersion && mvn test";
        
        //Process proc = Runtime.getRuntime().exec(command);
        ProcessBuilder proc = new ProcessBuilder();
        proc.command("cmd.exe", "/c", "cd PRVersion && mvn test -fae");
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
	}
	
    public static void main( String[] args ){
    	/*criar autenticaçao de utilizador*/
		serviceApp api = new serviceApp();	
    }
}
