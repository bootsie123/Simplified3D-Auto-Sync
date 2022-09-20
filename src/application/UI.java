package application;

import app.App;
import database.Database;
import application.Settings;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.prefs.BackingStoreException;
import javafx.application.Application;
import javafx.application.Platform;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class UI {
	private static TrayIcon trayIcon;
	private static Image logo;
	private static Image updating;
	
	private CheckboxMenuItem autoUpdate;
	private CheckboxMenuItem onStart;
	
	public static void main(String[] args) throws XPathExpressionException, BackingStoreException, IOException, InterruptedException, ParserConfigurationException, SAXException, TransformerException, AWTException {
		App.main(null);
	}
	
	public void launchSettings() {
		Settings.showStage();
	}	
	
	public void initializeUI() {
		Application.launch(Settings.class);
	}
	
	public void initializeTray() throws AWTException {
		if (!SystemTray.isSupported()) {
			System.out.println("System Tray not supported");
			return;
		}
		
		URL logoUrl = getClass().getResource("/simplify3d-logo.png");
		logo = Toolkit.getDefaultToolkit().createImage(logoUrl).getScaledInstance(-1, 16, Image.SCALE_SMOOTH);
		
		URL updatingUrl = getClass().getResource("/updating.png");
		updating = Toolkit.getDefaultToolkit().createImage(updatingUrl).getScaledInstance(16, -1, Image.SCALE_SMOOTH);
		
		PopupMenu popup = new PopupMenu();
		trayIcon = new TrayIcon(logo, "Simplify 3D Auto Save");
		
		SystemTray tray = SystemTray.getSystemTray();
		
		MenuItem about = new MenuItem("About");
		
		Menu settings = new Menu("Settings");
		autoUpdate = new CheckboxMenuItem("Auto Update Profiles");
		onStart = new CheckboxMenuItem("Run on Startup");
		
		autoUpdate.setState(Database.getBool("AutoRun"));
		onStart.setState(Database.getBool("AutoStart"));
		
		autoUpdate.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Database.setBool("AutoRun", autoUpdate.getState());
				Settings.updateAutoRun();
				
				if (autoUpdate.getState()) {
					App.startSchedule();
				} else {
					App.stopSchedule();
				}
			}
		});
		
		onStart.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Database.setBool("AutoStart", onStart.getState());
				Settings.updateAutoStart();
			}
		});
		
		MenuItem openConfig = new MenuItem("Config");
		
		openConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					launchSettings();
				} catch(Exception error) {
					System.out.println("Unable to open settings");
					error.printStackTrace();
				}
			}
		});
		
		MenuItem manualUpdate = new MenuItem("Update Profiles"); 
		
		manualUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				try {
					App.updateProfiles();
				} catch (Exception error) {
					System.out.println("Unable to update profiles");
					error.printStackTrace();
				}
				
				
			}
		});
		
		MenuItem exit = new MenuItem("Exit");
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				App.shutdownPool();
				Platform.exit();
				System.exit(0);
			}
		});
		
		popup.add(about);
		popup.addSeparator();
		popup.add(settings);
		popup.add(openConfig);
		popup.addSeparator();
		popup.add(manualUpdate);
		popup.addSeparator();
		popup.add(exit);
		
		settings.add(autoUpdate);
		settings.add(onStart);
		
		trayIcon.setPopupMenu(popup);
		
		tray.add(trayIcon);
	}
	
	public void setAutoUpdateStatus(Boolean status) {
		autoUpdate.setState(status);
	}
	
	public void setOnStartStatus(Boolean status) {
		onStart.setState(status);
	}
	
	public static void setTrayIcon(String status) {
		if (status.equals("logo")) {
			trayIcon.setImage(logo);
		} else if (status.equals("updating")) {
			trayIcon.setImage(updating);
		}
	}
}
