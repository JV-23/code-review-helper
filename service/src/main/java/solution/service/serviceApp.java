package solution.service;

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
	
	public void printPubRepositoryPRs() throws Exception {
		Repository pubRepo = repService.getRepository("eclipse", "egit-github");
		List<RepositoryCommit> prCommits = new ArrayList<RepositoryCommit>();
		
		for(PullRequest pr : prs.getPullRequests(pubRepo, "open")) {
			System.out.println(pr.getTitle());
			System.out.println(pr.getId() + " ------------------------ " + pr.getTitle());
			prCommits = prs.getCommits(pubRepo, pr.getNumber());
			
			for(RepositoryCommit rc : prCommits) {
				System.out.println(rc.getSha());
				RepositoryCommit anotherCommit = commitService.getCommit(pubRepo, rc.getSha());
				for(CommitFile f : anotherCommit.getFiles()) {
					System.out.println(f.getFilename());
					System.out.println(f.getPatch());
				}
				
			}
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
