package app;

import database.Database;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.kohsuke.github.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class GithubManager {
	static GitHub github;
	static GHRepository cRepo; 
	
	private static String user = Database.getString("Username");
	private static String token = Database.getString("Token");
	private static String repoName = Database.getString("RepoName");
	private static String branch = Database.getString("Branch");
	private static String repo = user + "/" + repoName;
	
	public static void updateSettings() {
		user = Database.getString("Username");
		token = Database.getString("Token");
		repoName = Database.getString("RepoName");
		branch = Database.getString("Branch");
		repo = user + "/" + repoName;
	}
	
	public static void authenticate() throws IOException {
		github = GitHub.connect(user, token);
		cRepo = github.getRepository(repo);
	}
	
	public static HashMap<String, HashMap<String, Object>> getRepositoryFiles(String repositoryName) throws IOException, ParserConfigurationException, SAXException, TransformerException, XPathExpressionException {
		
		//Check if file is of type .xml
		
		List<GHContent> dirContents = cRepo.getDirectoryContent("/");
		
		HashMap<String, HashMap<String, Object>> gitHubProfiles = new HashMap<String, HashMap<String, Object>>();
		
		for (GHContent content : dirContents) {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> profile = XMLParser.parseProfile(new InputSource(content.read()));
	
			profile.put("contentObj", content);
			gitHubProfiles.put((String) profile.get("name"), profile);
		}
		
		return gitHubProfiles;
	}
	
	public static void uploadFile(String path, String fileContent, String commitMessage) throws IOException {
		//Upload xml string
		
		GHContentBuilder builder = cRepo.createContent();
		
		builder.branch(branch);
		builder.content(fileContent);
		builder.message(commitMessage);
		builder.path(path.replaceAll(" ", "_"));
		
		builder.commit();
	}
	
	public static void updateFile(String fileContent, GHContent content, String commitMessage) throws IOException {
		content.update(fileContent, commitMessage);
	}
	
	public static void uploadFile(String path, String fileContent) throws IOException {
		uploadFile(path, fileContent, "Updated config file");
	}
	
	@SuppressWarnings("unused")
	private static void listRepositories() throws IOException {
		GHMyself myself = github.getMyself();
		
		Map<String, GHRepository> repositories = myself.getAllRepositories();
		
		for (Map.Entry<String, GHRepository> entry : repositories.entrySet()) {
			GHRepository repo = entry.getValue();
			
			System.out.println(repo.getName());
		}
	}
}
