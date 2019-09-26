package solution.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
		//TODO: repository and credentials added by user
		String repoUrl = "insert repository url";
		CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider( "ce086813cc6bed5e29875a94297d840eaa4d7828", "" );
		String cloneDirectoryPath = System.getProperty("user.dir") + "\\stableVersion";
		
		System.out.println(cloneDirectoryPath);
		
		try {
		    System.out.println("Cloning "+repoUrl+" into "+repoUrl);
		    Git.cloneRepository()
	        	.setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
		        .setURI(repoUrl)
		        .setDirectory(Paths.get(cloneDirectoryPath).toFile())
		        .setCredentialsProvider(credentialsProvider)
		        .call();
		    System.out.println("Completed Cloning");
		} catch (GitAPIException e) {
		    System.out.println("Exception occurred while cloning repo");
		    e.printStackTrace();
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
		//TODO: repository, branch and credentials added by user
		String repoUrl = "insert repository & branch url";
		CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider( "ce086813cc6bed5e29875a94297d840eaa4d7828", "" );
		String cloneDirectoryPath = System.getProperty("user.dir") + "\\stableVersion";
		
		System.out.println(cloneDirectoryPath);
		
		try {
		    System.out.println("Cloning "+repoUrl+" into "+repoUrl);
		    Git.cloneRepository()
	        	.setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
		        .setURI(repoUrl)
		        .setDirectory(Paths.get(cloneDirectoryPath).toFile())
		        .setCredentialsProvider(credentialsProvider)
		        .call();
		    System.out.println("Completed Cloning");
		} catch (GitAPIException e) {
		    System.out.println("Exception occurred while cloning repo");
		    e.printStackTrace();
		}
	}
	
	public void printPubRepositoryPRs() throws Exception {
		Repository pubRepo = repService.getRepository("OpenAPITools", "openapi-generator");
		List<RepositoryCommit> prCommits = new ArrayList<RepositoryCommit>();
		
		for(PullRequest pr : prs.getPullRequests(pubRepo, "open")) {
			System.out.println(pr.getTitle());
			System.out.println(pr.getUser().getLogin());
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
	
    public static void main( String[] args )
    {
    	/*criar autenticaçao de utilizador*/
		serviceApp api = new serviceApp();
		
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token("ce086813cc6bed5e29875a94297d840eaa4d7828");
		RepositoryService service = new RepositoryService(client);
		CommitService commits = new CommitService(client);
		PullRequestService prs = new PullRequestService(client);
		
    }
}
