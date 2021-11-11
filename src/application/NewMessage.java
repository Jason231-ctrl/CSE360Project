package application;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
		toTextLabel.setText(name);
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
			
			Stage stage1 = (Stage) sendMessageButton.getScene().getWindow();
    		stage1.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Patient Portal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage2 = new Stage();
            stage2.setResizable(false);
            stage2.setScene(scene);
            stage2.show();
		} catch (IOException e) {
			System.out.println("ERROR!");
		}
	}
}