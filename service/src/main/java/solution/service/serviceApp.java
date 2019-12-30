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
	private GitHubClient client = new GitHubClient();
	private RepositoryService repService = new RepositoryService(client);
	private CommitService commitService = new CommitService(client);
	private PullRequestService prs = new PullRequestService(client);
	
	public serviceApp() {
		client.setOAuth2Token("3d64bbf822b565f5cdce10c24ca090fef080bff1");
	}
	
	public String downloadStableVersion() throws Exception{
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
		
		return repoUrl;
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
	
	
	public int downloadPRVersion() throws Exception{
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
	        
	        return Integer.parseInt(myNumber);
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
	
	
	public List<changedLines> getPullRequestChanges(String repo, int pullRequestNumber) throws Exception {
		String[] parts = repo.split("/");
		List<changedLines> lines = new ArrayList<changedLines>();
		Repository pubRepo = repService.getRepository(parts[3], parts[4]);
		List<RepositoryCommit> prCommits = new ArrayList<RepositoryCommit>();
		//System.out.println("------");
		for(PullRequest pr : prs.getPullRequests(pubRepo, "open")) {
			//System.out.println(pr.getTitle());
			//System.out.println(" ------------------------ ");
			prCommits = prs.getCommits(pubRepo, pr.getNumber());
			if(pr.getNumber() == pullRequestNumber) {
				for(RepositoryCommit rc : prCommits) {
					RepositoryCommit anotherCommit = commitService.getCommit(pubRepo, rc.getSha());
					for(CommitFile f : anotherCommit.getFiles()) {
						//System.out.println(f.getFilename());
						//System.out.println(f.getChanges());
						//System.out.println("------------------------------------");
						lines.add(filterChangedFilePatchAdditions(f.getPatch(), f.getFilename()));
					}
				}
				break;
			}
			
		}
		
		/*for(PullRequest pr : prs.getPullRequests(pubRepo, "closed")) {
			//System.out.println(pr.getTitle());
			//System.out.println(pr.getNumber() + " ------------------------ " + pr.getTitle());
			prCommits = prs.getCommits(pubRepo, pr.getNumber());
			if(pr.getNumber() == pullRequestNumber) {
				for(RepositoryCommit rc : prCommits) {
					RepositoryCommit anotherCommit = commitService.getCommit(pubRepo, rc.getSha());
					for(CommitFile f : anotherCommit.getFiles()) {
						//System.out.println(f.getFilename());
						//System.out.println(f.getPatch());
						//System.out.println("------------------------------------");
						lines.add(filterChangedFilePatchAdditions(f.getPatch(), f.getFilename()));
					}
				}
			}
			
		}*/
		return lines;
	}
	
	public changedLines filterChangedFilePatchAdditions(String changedFilePatch, String filename) {

		String[] splitData = changedFilePatch.split("(?=\\n\\+)|(?=\\n\\-)|\n"); //splits the (patch) by "\n+" or "\n-" while keeping them. removes "\n"
		Map<Integer, String> dataToProcess = new HashMap<Integer, String>();
		int lineNr = 0;
		String lineNumber ="";
		boolean b = false;
		
		for(String s : splitData) {
			if(s.startsWith("\n+")) {
				s = s.replace("\n+", ""); //removes the "+" sign of patch lines to compare with source file content
				dataToProcess.put(lineNr, s);
			}
			if(!(s.startsWith("\n-"))) //only increments if line wasn't deleted
				lineNr++;
			if(s.startsWith("@@ -")) {
				for (int i = 0, n = s.length(); i < n; i++) {
				    char c = s.charAt(i);
				    if(c == ',' || c == ' ') {
				    	b = false;
				    }
				    if(b == true) {
				    	lineNumber += c;
				    }
				    if(c == '+') {
				    	b = true;
				    }
				}
				lineNr = Integer.parseInt(lineNumber);
				lineNumber = "";
			}
		}
			
		//prints for testing purposes
		/*System.out.println(filename);
		for(Map.Entry<Integer, String> entry:dataToProcess.entrySet()) {
			System.out.println(entry);
		}
		System.out.println("----------------");
		*/
		changedLines change = new changedLines(filename, dataToProcess);
		return change;
	}

	
    public static void main( String[] args ){
    	/*criar autenticaçao de utilizador*/
		serviceApp api = new serviceApp();	
		
    }
}
