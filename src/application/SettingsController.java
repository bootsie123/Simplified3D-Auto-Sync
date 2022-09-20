package application;

import database.Database;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;

import app.App;
import app.GithubManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SettingsController {
	@FXML
	private JFXTextField username;
	
	@FXML
	private JFXTextField token;
	
	@FXML
	private JFXTextField repoName;
	
	@FXML
	private JFXTextField branch;
	
	@FXML
	private JFXCheckBox autoRun;
	
	@FXML
	private JFXCheckBox autoStart;
	
	private UI ui = App.getUI();
	
	public void initialize() {
		username.setText(Database.getString("Username"));
		token.setText(Database.getString("Token"));
		repoName.setText(Database.getString("RepoName"));
		branch.setText(Database.getString("Branch"));
		
		autoRun.setSelected(Database.getBool("AutoRun"));
		autoStart.setSelected(Database.getBool("AutoStart"));
	}
	
	public void updateAutoRun() {
		autoRun.setSelected(Database.getBool("AutoRun"));
	}
	
	public void updateAutoStart() {
		autoStart.setSelected(Database.getBool("AutoStart"));
	}
	
	public void save(ActionEvent event) {
		Database.setString("Username", username.getText());
		Database.setString("Token", token.getText());
		Database.setString("RepoName", repoName.getText());
		Database.setString("Branch", branch.getText());
		
		Database.setBool("AutoRun", autoRun.isSelected());
		Database.setBool("AutoStart", autoStart.isSelected());
		
		ui.setAutoUpdateStatus(autoRun.isSelected());
		ui.setOnStartStatus(autoStart.isSelected());
		
		Settings.hideStage();
		GithubManager.updateSettings();
	}
}
