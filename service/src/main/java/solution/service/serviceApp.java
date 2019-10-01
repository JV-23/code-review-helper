package solution.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

/**
 * Hello world!
 *
 */
public class serviceApp 
{
	private GitHubClient client;
	private RepositoryService repService;
	private CommitService commitService;
	private PullRequestService prs;
	
	
	public serviceApp(){

		client = new GitHubClient();
		client.setOAuth2Token("ce086813cc6bed5e29875a94297d840eaa4d7828");
		repService = new RepositoryService(client);
		commitService = new CommitService(client);
		prs = new PullRequestService(client);
	}
	
	
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
        String command = "cmd.exe /c cd stableVersion && mvn test";
        
        Process proc = Runtime.getRuntime().exec(command);

        // Read the output

        BufferedReader reader =  
              new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
        }

        proc.waitFor();
	}
	
	
	public void downloadPRVersion() throws Exception{
			//TODO: branch added by user
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
	  
	        ProcessBuilder processBuilder = new ProcessBuilder();
	        processBuilder.command("cmd.exe", "/c", "cd PRVersion && git fetch origin pull/1/head && git checkout -b pull-request FETCH_HEAD");
	        
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
        String command = "cmd.exe /c cd PRVersion && mvn test";
        
        Process proc = Runtime.getRuntime().exec(command);

        // Read the output

        BufferedReader reader =  
              new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
        }

        proc.waitFor();
	}
	
	public void printPubRepositoryPRs() throws Exception {
		Repository pubRepo = repService.getRepository("OpenAPITools", "openapi-generator");
		List<RepositoryCommit> prCommits = new ArrayList<RepositoryCommit>();
		
		for(PullRequest pr : prs.getPullRequests(pubRepo, "open")) {
			System.out.println(pr.getTitle());
			System.out.println(pr.getUser().getLogin());
			System.out.println(pr.getUrl());
			System.out.println(pr.getId() + " ------------------------ ");
			/*prCommits = prs.getCommits(pubRepo, pr.getNumber());
			
			for(RepositoryCommit rc : prCommits) {
				System.out.println(rc.getSha());
				RepositoryCommit anotherCommit = commitService.getCommit(pubRepo, rc.getSha());
				for(CommitFile f : anotherCommit.getFiles()) {
					System.out.println(f.getFilename());
					System.out.println(f.getPatch());
				}
							
			}*/
			System.out.println("------------------------------------------------------------------------------------------------");
			
		}
	}
	
	
	public void printUserRepositories(GitHubClient client, RepositoryService repService, CommitService commitService) throws Exception{
		
		for (Repository repo : repService.getRepositories()) {
			System.out.println("repo name: " + repo.getName() + "  -----  " + repo.getDescription());
		}
		
	}
	
	
    public static void main( String[] args ){
    	/*criar autenticaçao de utilizador*/
		serviceApp api = new serviceApp();
		
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token("ce086813cc6bed5e29875a94297d840eaa4d7828"); //token no longer exists, find alternative if needed
		RepositoryService service = new RepositoryService(client);
		CommitService commits = new CommitService(client);
		PullRequestService prs = new PullRequestService(client);
		
    }
}
