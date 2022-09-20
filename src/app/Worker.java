package app;

import java.util.HashMap;
import org.kohsuke.github.GHContent;

import application.UI;

public class Worker implements Runnable {
	private static String profilePath = "HKCU\\Software\\Simplify3D\\S3D-Software\\FFFWindow";
	private static String profileValue = "profileDataBaseContents";
	
	public void run() {
		System.out.println("Starting job...");
		
		UI.setTrayIcon("updating");
		
		try {
			String[] profiles = RegManager.getValue(profilePath, profileValue).replace("\\0", "").split("\\<\\?xml version=\"1.0\"\\?\\>");
			
			HashMap<String, HashMap<String, Object>> localProfiles = new HashMap<String, HashMap<String, Object>>();
			
			for (int i = 1; i < profiles.length; i++) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> profile = XMLParser.parseProfileFromString(profiles[i]);
				
				localProfiles.put((String) profile.get("name"), profile);
			}
			
			GithubManager.authenticate();
			
			HashMap<String, HashMap<String, Object>> gitHubProfiles = GithubManager.getRepositoryFiles("test");
			
			for (HashMap<String, Object> localProfile : localProfiles.values()) {
				if (gitHubProfiles.get(localProfile.get("name")) != null && localProfile != null) {
					if (Utils.isHigherVersion((String) localProfile.get("version"), (String) gitHubProfiles.get(localProfile.get("name")).get("version")) == true) {
						String updateMessage = "Updated from version " + gitHubProfiles.get(localProfile.get("name")).get("version") + " to " + localProfile.get("version");
						
						GithubManager.updateFile((String) localProfile.get("xml"), (GHContent) gitHubProfiles.get(localProfile.get("name")).get("contentObj"), updateMessage);
						
						System.out.println("Updated: " + localProfile.get("name") + " - " + updateMessage);
					}
				} else {
					GithubManager.uploadFile(localProfile.get("name") + ".xml", (String) localProfile.get("xml"));
				}
			}
			
			System.out.println("Job finished!");
		} catch (Exception error) {
			System.out.println("Job stopped...");
			error.printStackTrace();
		}
		
		UI.setTrayIcon("logo");
	}
}
