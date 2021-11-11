package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Controller implements Initializable{
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Label prescNameLabel;
	
	@FXML
	private ChoiceBox<String> prescChoiceBox = new ChoiceBox<>();
	
	@FXML 
	private ListView<String> prescListView = new ListView<>();
	
	private String[] placeholderNames = {"John Smith", "Pete Williams", "Michael Scott"};
	String[] placeholderPresc = {"Tylenol", "Chicken Noodle Soup", "Advil"};
	String currentPresc;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		prescChoiceBox.getItems().addAll(placeholderNames);
		prescChoiceBox.setOnAction(this::getName);
		prescListView.getItems().addAll(placeholderPresc);
	}
	
	public void getName(ActionEvent event) {
		String name = prescChoiceBox.getValue();
		prescNameLabel.setText(name);
	}
	
	
	public void toEditUser(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("EditUser.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toResults(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Results.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toPatientPortal(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Patient Portal.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toPrescriptions(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Prescriptions.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toMessages(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("check-messages.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void signOut(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toDoctorPortal(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Doctor Portal.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toViewPatients(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("ViewPatients.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toCheckVitals(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("CheckVitals.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toSendPrescriptions(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SendPrescriptions.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toNursePortal(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Nurse Portal.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toTakeVitals(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("TakeVitals.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toNursePatients(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("NursePatients.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toCreateAccount(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("create-account.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
	
}
 