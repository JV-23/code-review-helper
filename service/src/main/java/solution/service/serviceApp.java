package solution.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
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

import org.apache.commons.io.FileUtils;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.DataService;
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

import org.yaml.snakeyaml.Yaml;

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
	private List<String> changedFiles = new ArrayList<String>();
	private List<String> repoFiles = new ArrayList<String>();
	private String repoUrl = null;
	
	public serviceApp() {
		//client.setOAuth2Token("7fa72cf41cb0c3cc8230197852cfd24663e04d03 ");
		Yaml yaml = new Yaml();
		boolean b = false;
		try{
			System.out.println(System.getProperty("user.dir"));
			InputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "\\config.yml");		
			Map<String, Object> obj = yaml.load(inputStream);
			for(Map.Entry<String, Object> entry : obj.entrySet()){
				if(entry.getKey().equals("token")){
					//System.out.println(entry.getValue().toString());
					client.setOAuth2Token(entry.getValue().toString());
					b = true;
				}
			}
		}
		catch(Exception e){
			System.out.println("exception caught -----------------------");
		}

		if(b == false){
			System.out.println("Client not authenticated due to possible problem with config.yml file, this reduces the amount of git requests available.");
		}
	}
	
	public List<String> getChangedFiles(){
		return changedFiles;
	}
	
	public List<String> getRepoFiles(){
		return repoFiles;
	}	

	public void setAppCredentials(String username, String password){
		client.setCredentials(username, password);
	}

	public String downloadStableVersion() throws Exception{
		Console cnsl = null;
		String username = null;
		char[] pwd = null;
	      
		try {
			// creates a console object
			cnsl = System.console();
			
			if (cnsl != null) {
			   // read line from the user input
				//username = cnsl.readLine("Insert your GitHub Username: ");
				// read password into the char array
				//pwd = cnsl.readPassword("Insert your GitHub Password: ");
				//read the url of the repository
				repoUrl = cnsl.readLine("Insert the url of the repository you want to access:");
			} 
			     
		} 
		catch(Exception ex) {
			ex.printStackTrace();      
		}

		//client.setCredentials(username, String.valueOf(pwd));

		new File(System.getProperty("user.dir") + "\\stableVersion").mkdir();

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("cmd.exe", "/c", "cd stableVersion && git clone " + repoUrl + " .");

		try {
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			BufferedReader reader =  new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			process.waitFor();

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		/*CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, pwd);
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
		}*/
		
		return repoUrl;
	}
	
	
	public void runStableVersionTests() throws Exception{
        //String command = "cmd.exe /c cd stableVersion && mvn test";
        
        ProcessBuilder proc = new ProcessBuilder();
        proc.command("cmd.exe", "/c", "cd stableVersion && mvn test -fn");
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
		 	new File(System.getProperty("user.dir") + "\\PRVersion").mkdir();

			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd.exe", "/c", "cd PRVersion && git clone " + repoUrl + " .");

			try {
				processBuilder.redirectErrorStream(true);
				Process process = processBuilder.start();
				BufferedReader reader =  new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";
				while((line = reader.readLine()) != null) {
					System.out.println(line);
				}

				process.waitFor();

			}
			catch(Exception e) {
				e.printStackTrace();
			}
	        
	        Scanner scan = new Scanner(System.in);
	        System.out.println("Enter the number of the pull request you want to analyze:");
	        
	        String myNumber = scan.nextLine();
	  
	        processBuilder = new ProcessBuilder();
	        processBuilder.command("cmd.exe", "/c", "cd PRVersion && git pull " + repoUrl + " refs/pull/" + myNumber + "/head");
	        
	        
	        try {
	        	processBuilder.redirectErrorStream(true);
	        	Process process = processBuilder.start();
	        	BufferedReader reader =  new BufferedReader(new InputStreamReader(process.getInputStream()));
	        	String line = "";
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
        proc.command("cmd.exe", "/c", "cd PRVersion && mvn test -fn");
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
		String result = "";
		String result2 = "";
		Map<String, String> diff = new HashMap<String, String>();
		Integer i = 1;
		int j = 1;
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
						//System.out.println(f.getPatch());
						lines.add(filterChangedFilePatchAdditions(f.getPatch(), f.getFilename()));
						result = parseChange(new File(System.getProperty("user.dir") + "\\PRVersion"), filterChangedFilePatchAdditions(f.getPatch(), f.getFilename()), result2);
						//System.out.println(result);
						if(result.equals("")){
							result = "No Coverage" + j;
							j++;
						}
						diff.put(result, "--- " + f.getFilename() + "\n+++ " + f.getFilename() +"\n" + f.getPatch());
						i++;
					}
				}
				break;
			}
			
		}
		
		FileOutputStream f = new FileOutputStream(new File("diffs"));
		ObjectOutputStream o = new ObjectOutputStream(f);

		// Write objects to file
		o.writeObject(diff);

		o.close();
		f.close();
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
	
	public String parseChange(File folder, changedLines changed, String result) throws Exception {
		Document doc;
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				result = parseChange(fileEntry, changed, result);
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
										/*ChangedLine changed2 = new ChangedLine();
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
										result.add(changed2);*/
										//System.out.println("here");
										result += " Line Number: " + Integer.parseInt(element2.getAttribute("nr")) + 
												" Covered Instructions: " +  Integer.parseInt(element2.getAttribute("ci")) +
												" Missed Instructions: " + Integer.parseInt(element2.getAttribute("mi")) +
												" Covered Branches: " + Integer.parseInt(element2.getAttribute("cb")) +
												" Missed Branches: " + Integer.parseInt(element2.getAttribute("mb")) +
												"<br>";
										//System.out.println(result);
									}
								}

							}
							return result;
						}
					}

				}

			}
		}
		//System.out.println(result);
		return result;
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
		//System.out.println(change);
		return change;
	}

public void retrieveRepositoryFilesNames(String path, String fileToFind, String repo) {
		
		try {
			String[] s = repo.split("/");
			Repository pubRepo = repService.getRepository(s[s.length-2], s[s.length-1]);
			ContentsService content = new ContentsService(this.client);
			DataService dataService = new DataService(this.client);
			//System.out.println(pubRepo.getName());
			boolean hasDirs = false;
			List<RepositoryContents> rootContent = null;
			if(path == null) {
				rootContent = content.getContents(pubRepo);
			}else {
				rootContent = content.getContents(pubRepo, path);
			}
			
			for(RepositoryContents repContent : rootContent) {
				//System.out.println(repContent.getPath());
				//System.out.println("content is: " + repContent.getType());
				if(repContent.getType().equals("dir")) {
					hasDirs = true;
					while(hasDirs) {
						//System.out.println("Dir: " + repContent.getPath());
						hasDirs = false;
						retrieveRepositoryFilesNames(repContent.getPath(), fileToFind, repo);
					}
				}
				else {
					this.repoFiles.add(repContent.getPath());
					System.out.println("Loading repo files...");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String findFile(String file) {
		for(String s : this.repoFiles) {
			if(s.endsWith(file)) {
				return s;
			}
		}
		return null;
	}
	
    public static void main( String[] args ){
    	/*criar autenticaçao de utilizador*/
		serviceApp api = new serviceApp();	
		
    }
}
