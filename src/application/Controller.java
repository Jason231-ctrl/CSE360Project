package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.sqlite.SQLiteDataSource;

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
	
	@FXML
	private TextField username;
	
	@FXML
	private TextField password;
	
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
	
	public void toPortal(ActionEvent event) throws IOException {
		
		if(username.getText().compareTo("") == 0 || password.getText().compareTo("") == 0) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Information Entered");
            alert.setHeaderText(null);
            alert.setContentText("Something you entered is incorrect, please look over your information and try again.");

            alert.showAndWait();
		}
		else {
		String portal = null;
		SQLiteDataSource ds = null;
		ResultSet result;
		int type = 5;
		ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:info.db");
		try ( Connection conn = ds.getConnection();
			  Statement stmt = conn.createStatement(); ) {
		result = stmt.executeQuery("SELECT Type FROM AccountDb WHERE Username = '" + username.getText() + "' AND Password = '" + password.getText() + "'");
		type = result.getInt( "Type" );
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		if (type == 0) {
			portal = "Patient Portal.fxml";
		}
		else if (type == 1) {
			portal = "Nurse Portal.fxml";
		}
		else if (type == 2) {
			portal = "Doctor Portal.fxml";
		}
		else {
			portal = "Main.fxml";
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Information Entered");
            alert.setHeaderText(null);
            alert.setContentText("Something you entered is incorrect, please look over your information and try again.");

            alert.showAndWait();
			
		}
		
		root = FXMLLoader.load(getClass().getResource(portal));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		}
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
	
	/*public void toDoctorPortal(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Doctor Portal.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}*/
	
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
	
	/*public void toNursePortal(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Nurse Portal.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	} */
	
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
 