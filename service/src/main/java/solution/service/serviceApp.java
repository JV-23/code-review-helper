package solution.service;

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
	public String testMethod(String s) {
		String r = s + "add";
		
		return r;
		
	}
    public static void main( String[] args )
    {
    	/*criar autenticaçao de utilizador*/
		serviceApp api = new serviceApp();
		
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token("a607293757bcfb90772a5c824bf67c38eeaa9212");
		RepositoryService service = new RepositoryService(client);
		CommitService commits = new CommitService(client);
		PullRequestService prs = new PullRequestService(client);
		
    }
}
