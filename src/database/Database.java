package database;

import java.util.prefs.Preferences;

public class Database {
	private static Preferences prefs = Preferences.userRoot().node("com/Simplify3DAutoSave/settings");
	public static void setString(String key, String value) {
		prefs.put(key, value);
	}
	
	public static void setBool(String key, boolean value) {
		prefs.putBoolean(key, value);
	}
	
	public static String getString(String key) {
		return prefs.get(key, "");
	}
	
	public static boolean getBool(String key) {
		return prefs.getBoolean(key, false);
	}

}
