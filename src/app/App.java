package app;

import application.UI;
import database.Database;

import java.awt.AWTException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class App {
	private static int maxThreads = 2;
	
	private static int scheduleTime = 5;
	private static int scheduleDelay = 0;
	
	private static ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	private static ScheduledFuture<?> schedule;
	
	private static UI ui;
	
	private static String autoRunPath = "HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run";
	
	public static void main(String[] args) throws AWTException {
		if (Database.getBool("AutoRun")) {
			startSchedule();
		}
		
		ui = new UI();
		ui.initializeTray();
		ui.initializeUI();
	}
	
	public static UI getUI() {
		return ui;
	}
	
	public static void updateProfiles() {
		Runnable updateProfile = new Worker();
		
		pool.execute(updateProfile);
	}
	
	public static void shutdownPool() {
		System.out.println("Shutting Down...");
		
		scheduler.shutdown();
		pool.shutdown();
		
		System.out.println("Shutdown");
	}
	
	public static void stopSchedule() {
		schedule.cancel(true);
	}
	
	public static void startSchedule() {
		if (Database.getBool("AutoRun")) {
			schedule = scheduler.scheduleAtFixedRate((Runnable)new Worker(), scheduleDelay, scheduleTime, TimeUnit.MINUTES);
		}
	}
	
	public static void setAutoStart(Boolean status) throws IOException {
		if (status) {
			Path path = FileSystems.getDefault().getPath("Simplify3DAutoSave.exe").toAbsolutePath();
			RegManager.writeValue(autoRunPath, "Simplify3DAutoSave", "REG_SZ", path.toString());
		} else {
			RegManager.deleteValue(autoRunPath, "Simplify3DAutoSave");
		}
	}
}
