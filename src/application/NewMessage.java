package application;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteDataSource;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class NewMessage {
	
	@FXML
	public Button sendMessageButton;
	
	@FXML
	private Label toTextLabel;
	
	@FXML
	private TextArea messageTextArea;
	
	@FXML
	private ChoiceBox<String> docNurseDropdown = new ChoiceBox<>();

    private String[] placeholderNames = {"John Smith", "Pete Williams", "Michael Scott"};
	String toText;
	String message = "";
	
	
	public void initialize() {
		docNurseDropdown.getItems().addAll(placeholderNames);
		docNurseDropdown.setOnAction(this::getName);
	}
	
	public void getName(ActionEvent event) {
		toText = docNurseDropdown.getValue();
		toTextLabel.setText("To: " + toText);
	}
	
	public void sendMessage() {
		message = messageTextArea.getText() + "\n";
		if (message == "" || toText == null) {
			Stage stage = (Stage) sendMessageButton.getScene().getWindow();
			stage.close();
		} else {
			insertMessage("User", toText, message);
			Stage stage = (Stage) sendMessageButton.getScene().getWindow();
			stage.close();
		}
	}
	
	public static void insertMessage(String from, String to, String text) {
		String query = "INSERT INTO MessagesDB(Sender, Receiver, Messages) "
				+ "VALUES('" + from + "','" + to + "','" + text + "')";
		SQLiteDataSource ds = null;
		ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:info.db");
		 try (Connection conn = ds.getConnection();
				Statement stmt = conn.createStatement();) {
				stmt.executeUpdate(query);
		 } catch ( SQLException e) {
			 e.printStackTrace();
		 }	
	}
}