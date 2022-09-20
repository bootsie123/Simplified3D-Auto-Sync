package app;

import java.util.HashMap;

public class Utils {
	public static Boolean isHigherVersion(String v1, String v2) {
		//2017-09-14 17:06:52
		//Returns true if the first value is a higher version than the second; null if equal
		
		HashMap<String, Integer> m1 = versionToMap(v1);
		HashMap<String, Integer> m2 = versionToMap(v2);
		
		String[] testingOrder = {"year", "month", "day", "hours", "minutes", "seconds"};
		
		for (String test : testingOrder) {
			if (m1.get(test) > m2.get(test)) {
				return true;
			} else {
				return false;
			}
		}
		
		return null;
	}
	
	private static HashMap<String, Integer> versionToMap(String version) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		map.put("year", Integer.parseInt(version.substring(0, 4)));
		map.put("month", Integer.parseInt(version.substring(5, 7)));
		map.put("day", Integer.parseInt(version.substring(8, 10)));
		
		map.put("hours", Integer.parseInt(version.substring(11, 13)));
		map.put("minutes", Integer.parseInt(version.substring(14, 16)));
		map.put("seconds", Integer.parseInt(version.substring(17, 19)));
		
		return map;
	}
}
