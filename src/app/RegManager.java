package app;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class RegManager {
	private static Runtime rt = Runtime.getRuntime();
	
	public static String getValue(String path, String key) throws IOException, InterruptedException {
		Process proc = rt.exec("reg query \"" + path + "\" /v " + key);
		
		InputStreamReader stream = new InputStreamReader(proc.getInputStream());
		BufferedReader reader = new BufferedReader(stream);
		
		String line;
		String combinedString = "";
		
		while((line = reader.readLine()) != null) {
			combinedString += line;
		}

		return combinedString.substring(107); //Format | path, key, type, string
	}
	
	public static void writeValue(String path, String name, String type, String value) throws IOException {
		Process proc = rt.exec("reg add \"" + path + "\" /v " + name + " /t " + type + " /d " + value);
	}
	
	public static void deleteValue(String path, String value) throws IOException {
		Process proc = rt.exec("reg deleted \"" + path + "\" /v " + value);
	}
}
