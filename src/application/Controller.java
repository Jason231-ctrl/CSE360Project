package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.input.MouseEvent;
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

	String currentPresc;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Start Prescription
		SQLiteDataSource ds = null;
		ResultSet namesresult = null;
		ResultSet drugsresult = null;
		ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:info.db");
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			namesresult = stmt.executeQuery("SELECT First_name,Last_name FROM PatientInfoDb WHERE DoctorID = " + Main.user.getId());

			ArrayList<String> presAllPatientNames = new ArrayList<String>();
			while(namesresult.next()) {
				String presFullName = namesresult.getString("First_name") + " " + namesresult.getString("Last_name");
				presAllPatientNames.add(presFullName);
			}

			drugsresult = stmt.executeQuery("SELECT * FROM DrugDb");

			ArrayList<String> presDrugNames = new ArrayList<String>();
			while(drugsresult.next()) {
				String presDrugName = drugsresult.getString("Name");
				presDrugNames.add(presDrugName);
			}

			prescChoiceBox.getItems().addAll(presAllPatientNames);
			prescListView.getItems().addAll(presDrugNames);
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		//End Prescription
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
			Main.user.setAll("Patient", username.getText());
		}
		else if (type == 1) {
			portal = "Nurse Portal.fxml";
			Main.user.setAll("Nurse", username.getText());
		}
		else if (type == 2) {
			portal = "Doctor Portal.fxml";
			Main.user.setAll("Doctor", username.getText());
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
	
	public void toDoctorPortal(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Doctor Portal.fxml"));
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
	
	public void hover(MouseEvent event) {
		Button hoveredButton = (Button)event.getSource();
		hoveredButton.setStyle("""
                -fx-background-radius: 0;
                -fx-border: none;
                -fx-background-color: #bfbfbf;""");
	}

	public void unHover(MouseEvent event) {
		Button hoveredButton = (Button)event.getSource();
		hoveredButton.setStyle("""
                -fx-background-radius: 0;
                -fx-border: none;
                -fx-background-color: none;""");
	}
}
 