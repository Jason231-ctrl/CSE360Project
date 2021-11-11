package application;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
		String name = docNurseDropdown.getValue();
		toTextLabel.setText("To: " + name);
	}
	
	public void sendMessage() {
		File log = new File("Messages.txt");
		try {
			if(!log.exists()) {
				log.createNewFile();
			}
			FileWriter writer = new FileWriter(log, true);
			message = toTextLabel.getText() + "\n" + messageTextArea.getText() + "\n";
			writer.write(message);
			writer.close();
		} catch (IOException e) {
			System.out.println("ERROR!");
		}
		Stage stage = (Stage) sendMessageButton.getScene().getWindow();
		stage.close();
	}
}