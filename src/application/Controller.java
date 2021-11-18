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
import javafx.scene.PerspectiveCamera;
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
import java.util.stream.Collectors;

import org.sqlite.SQLiteDataSource;

import javax.xml.transform.Result;

public class Controller implements Initializable{
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private ChoiceBox<String> prescChoiceBox = new ChoiceBox<>();

	@FXML
	private ChoiceBox<String> DVitalsPatient = new ChoiceBox<>();
	
	@FXML 
	private ListView<String> prescListView = new ListView<>();
	@FXML
	private ListView<String> patientPrescName = new ListView<>();
	@FXML
	private ListView<String> patientPrescType = new ListView<>();
	@FXML
	private ListView<String> patientPrescDesc = new ListView<>();
	@FXML
	private ListView<String> patientPrescDose = new ListView<>();

	@FXML
	private TextField prescDose;

	@FXML
	private TextField PatientEditDOB, PatientEditAddress, PatientEditEmail, PatientEditPhoneNumber;

	@FXML
	private TextField VitalsFirstName, VitalsLastName, VitalsTemperature, VitalsPulse, VitalsBloodPressure, VitalsWeight, VitalsHeight, VitalsOxygen;

	@FXML
	private Label PatientOxygen, PatientBP, PatientHeight, PatientWeight, PatientTemp, PatientPulse;
	@FXML
	private Label DPatientOxygen, DPatientBP, DPatientHeight, DPatientWeight, DPatientTemp, DPatientPulse;
	
	@FXML
	private TextField username;
	
	@FXML
	private TextField password;

	String currentPresc;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SQLiteDataSource ds = null; //Get the database file
		ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:info.db");
		//Start Edit Patient Info
		if(Main.user.getType().compareTo("Patient") == 0) { //If the current user is a patient
			try (Connection conn = ds.getConnection();
				 Statement stmt = conn.createStatement();) {
				ResultSet patientInfo;

				patientInfo = stmt.executeQuery("SELECT Dob, Address, Email_add, Phone_num FROM PatientInfoDb WHERE Id = " + Main.user.getId()); //Get the current user's information

				if(PatientEditAddress != null) PatientEditAddress.setText(patientInfo.getString("Address")); //If the field is initialized then set the text
				if(PatientEditEmail != null) PatientEditEmail.setText(patientInfo.getString("Email_add"));
				if(PatientEditPhoneNumber != null) PatientEditPhoneNumber.setText("" + patientInfo.getInt("Phone_num"));
				if(PatientEditDOB != null) PatientEditDOB.setText(patientInfo.getString("Dob"));
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		}
		//End Edit Patient Info
		//Start Patient Check Results
		if(Main.user.getType().compareTo("Patient") == 0) { //If the current user is a patient
			try (Connection conn = ds.getConnection();
				 Statement stmt = conn.createStatement();) {
				ResultSet patientInfo;

				patientInfo = stmt.executeQuery("SELECT * FROM ResultDb WHERE Id = " + Main.user.getId()); //Get all fo the user's result information
				if(patientInfo.next()) {
					if (PatientBP != null) PatientBP.setText(patientInfo.getString("BP")); //If the label is initialized set the text
					if (PatientPulse != null) PatientPulse.setText(patientInfo.getInt("Pulse") + " BPM");
					if (PatientTemp != null) PatientTemp.setText(patientInfo.getInt("Temp") + " F");
					if (PatientHeight != null) PatientHeight.setText(patientInfo.getInt("HEIGHT") + " cm");
					if (PatientWeight != null) PatientWeight.setText(patientInfo.getInt("WEIGHT") + " lbs");
					if (PatientOxygen != null) PatientOxygen.setText(patientInfo.getInt("O2") + "%");
				}
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		}
		//End Patient Check Results
		//Start Doctor Check Results
		if(Main.user.getType().compareTo("Doctor") == 0) { //If the user is a doctor
			try (Connection conn = ds.getConnection();
				 Statement stmt = conn.createStatement();) {
				ResultSet namesresult = null;
				namesresult = stmt.executeQuery("SELECT First_name,Last_name FROM PatientInfoDb WHERE DoctorID = " + Main.user.getId()); //Get the names of all the doctor's patients

				ArrayList<String> AllPatientNames = new ArrayList<String>(); //Create a new arraylist to store the full names of patients
				while (namesresult.next()) {
					String presFullName = namesresult.getString("First_name") + " " + namesresult.getString("Last_name");
					AllPatientNames.add(presFullName); //Add the full name of each patient to the arraylist
				}
				namesresult.close();

				DVitalsPatient.getItems().addAll(AllPatientNames); //Add all the names to the choicebox
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		}
		//End Doctor Check Results
		//Start Prescription
		if(Main.user.getType().compareTo("Doctor") == 0) { //If the user is a doctor
			try (Connection conn = ds.getConnection();
				 Statement stmt = conn.createStatement();) {
				ResultSet namesresult = null;
				ResultSet drugsresult = null;
				namesresult = stmt.executeQuery("SELECT First_name,Last_name FROM PatientInfoDb WHERE DoctorID = " + Main.user.getId()); //Get the full name of each doctor's patient

				ArrayList<String> presAllPatientNames = new ArrayList<String>(); //Arraylist to hold all the names
				while (namesresult.next()) {
					String presFullName = namesresult.getString("First_name") + " " + namesresult.getString("Last_name");
					presAllPatientNames.add(presFullName); //adding the full names
				}
				namesresult.close();

				drugsresult = stmt.executeQuery("SELECT * FROM DrugDb"); //Get every drug in the database

				ArrayList<String> presDrugNames = new ArrayList<String>(); //Arraylist to hold the names of each drug
				while (drugsresult.next()) {
					String presDrugName = drugsresult.getString("Name");
					presDrugNames.add(presDrugName); //Add every drug's name to the list
				}
				drugsresult.close();

				prescChoiceBox.getItems().addAll(presAllPatientNames); //Add the patient and drug names to the respective choice box and list view
				prescListView.getItems().addAll(presDrugNames);
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		} else if(Main.user.getType().compareTo("Patient") == 0){ //Else if the user is a patient
			try (Connection conn = ds.getConnection();
				 Statement stmt = conn.createStatement();) {
				ResultSet prescResult = null;
				prescResult = stmt.executeQuery("SELECT Perscriptions FROM PatientInfoDb WHERE Id = " + Main.user.getId()); //Get the patient' current prescriptions

				ArrayList<Prescription> prescriptions = new ArrayList<Prescription>(); //Arraylist to hold prescriptions
				String[] prescString = prescResult.getString("Perscriptions") == null ? new String[]{}:prescResult.getString("Perscriptions").split("\\|"); //Break each stringified prescription into a prescription
				for(String s : prescString) {
					prescriptions.add(new Prescription(s)); //Add every prescription to the arraylist
				}
				prescResult.close();

				patientPrescName.getItems().addAll(prescriptions.stream().map(Prescription::getName).collect(Collectors.toList())); //Add all the prescription information into the respective listviews
				patientPrescType.getItems().addAll(prescriptions.stream().map(Prescription::getType).collect(Collectors.toList()));
				patientPrescDesc.getItems().addAll(prescriptions.stream().map(Prescription::getDescription).collect(Collectors.toList()));
				patientPrescDose.getItems().addAll(prescriptions.stream().map(Prescription::getDose).collect(Collectors.toList()));
			} catch (SQLException e) {
				//
			}
		}
		//End Prescription
	}

	public void changeCurrentPatientVitals(ActionEvent event) {
		SQLiteDataSource ds = null;
		ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:info.db");

		try (Connection conn = ds.getConnection();
			 Statement stmt = conn.createStatement();) {
			String[] name = DVitalsPatient.getValue().split(" "); //Get the selected patient's name
			ResultSet patient = stmt.executeQuery("SELECT * FROM ResultDb WHERE Id = (SELECT Id FROM PatientInfoDb WHERE First_name = '" + name[0] + "' AND Last_name = '" + name[1] + "')"); //Get the patient's results

			if(patient.next()) {
				if (DPatientBP != null) DPatientBP.setText(patient.getString("BP")); //If the label is initialized then set the text
				if (DPatientPulse != null) DPatientPulse.setText(patient.getInt("Pulse") + " BPM");
				if (DPatientTemp != null) DPatientTemp.setText(patient.getInt("Temp") + " F");
				if (DPatientHeight != null) DPatientHeight.setText(patient.getInt("HEIGHT") + " cm");
				if (DPatientWeight != null) DPatientWeight.setText(patient.getInt("WEIGHT") + " lbs");
				if (DPatientOxygen != null) DPatientOxygen.setText(patient.getInt("O2") + "%");
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}
	}

	public void toEditUser(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("EditUser.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void updateVitals(ActionEvent event) throws IOException {
		String FirstName = VitalsFirstName.getText(); //Get the inputs for all of the new vitals
		String LastName = VitalsLastName.getText();
		String Temperature = VitalsTemperature.getText();
		String BloodPressure = VitalsBloodPressure.getText();
		String Height = VitalsHeight.getText();
		String Weight = VitalsWeight.getText();
		String Oxygen = VitalsOxygen.getText();
		String Pulse = VitalsPulse.getText();

		SQLiteDataSource ds = null;
		ResultSet result;
		ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:info.db");

		try ( Connection conn = ds.getConnection();
			  Statement stmt = conn.createStatement(); ) {
			stmt.execute("" + //Inserts the patient into the vitals table if they aren't there, otherwise replaces the old vitals with the new
					"INSERT OR REPLACE INTO ResultDb (Id, Temp, Pulse, BP, HEIGHT, WEIGHT, O2) VALUES (" +
					"(SELECT Id FROM PatientInfoDb WHERE First_name = '" + FirstName + "' AND Last_name = '" + LastName + "')," +
					Temperature + "," +
					Pulse + ",'" +
					BloodPressure + "'," +
					Height + "," +
					Weight + "," +
					Oxygen + ");"
			);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		root = FXMLLoader.load(getClass().getResource("Nurse Portal.fxml")); //Return to nurse portal
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void sendPrescription(ActionEvent event) throws IOException {
		String selectedPatient = prescChoiceBox.getValue(); //Get the current patient
		String selectedDrug = prescListView.getSelectionModel().getSelectedItem(); //Get the selected prescription
		String currentDose = prescDose.getText(); //Get the chosen dosage
		String newPrescriptionsString = null;
		int id = 0;

		if(selectedPatient == null || selectedDrug == null || currentDose == null || selectedPatient.compareTo("") == 0 || selectedDrug.compareTo("") == 0 || currentDose.compareTo("") == 0) { //Send an alert if something isn't filled out
			Alert alert = new Alert(Alert.AlertType.INFORMATION); //Send an alert
			alert.setTitle("Missing Info");
			alert.setHeaderText(null);
			alert.setContentText("Make sure you included a patient, drug, and dosage.");

			alert.showAndWait();
		} else {
			SQLiteDataSource ds = null;
			ResultSet patientresult = null;
			ResultSet drugresult = null;
			ds = new SQLiteDataSource();
			ds.setUrl("jdbc:sqlite:info.db");
			try(Connection conn = ds.getConnection();
				Statement stmt = conn.createStatement();) {
				patientresult = stmt.executeQuery("SELECT Perscriptions, Id FROM PatientInfoDb WHERE First_name = '" + selectedPatient.split(" ")[0] + "' AND Last_name = '" + selectedPatient.split(" ")[1] + "'"); //Get the prescriptions the patient already has
				id = patientresult.getInt("Id"); //Get the patient's id

				String[] prescriptionsString = patientresult.getString("Perscriptions") == null ? new String[]{}:patientresult.getString("Perscriptions").split("\\|"); //If the prescriptions are empty then create an empty list, otherwise split the prescriptions
				ArrayList<Prescription> prescriptions = new ArrayList<Prescription>(); //Arraylist to hold prescriptions
				for (String s : prescriptionsString) {
					prescriptions.add(new Prescription(s)); //Add each prescription to the arraylist
				}
				patientresult.close();

				drugresult = stmt.executeQuery("SELECT * FROM DrugDb WHERE Name = '" + selectedDrug + "'"); //Get the current drug from the database
				Prescription newPrescription = new Prescription(selectedDrug, drugresult.getString("Desc"), drugresult.getString("Type"), currentDose); //Create a new prescription for said drug

				prescriptions.add(newPrescription); //Add the new prescription to the patient

				ArrayList<String> prescriptionsStringified = new ArrayList<String>(); //Restringify all of the Prescriptions
				for(Prescription p : prescriptions) {
					prescriptionsStringified.add(p.toString());
				}
				drugresult.close();

				newPrescriptionsString = String.join("|", prescriptionsStringified); //Join them with a Vertical Bar to separate each prescription

				stmt.executeUpdate("UPDATE PatientInfoDb SET Perscriptions = '" + newPrescriptionsString + "' WHERE Id = " + id); //Update the patient's prescriptions
			} catch (SQLException e) {
				e.printStackTrace();
			}

			root = FXMLLoader.load(getClass().getResource("Doctor Portal.fxml")); //Return to doctor portal
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
	
	public void toResults(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Results.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void toPortal(ActionEvent event) throws IOException {
		
		if(username.getText().compareTo("") == 0 || password.getText().compareTo("") == 0) { //If the username and/or password are empty send an alert
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
		result = stmt.executeQuery("SELECT Type FROM AccountDb WHERE Username = '" + username.getText() + "' AND Password = '" + password.getText() + "'"); //Get the type of user logging in
		type = result.getInt( "Type" );
		conn.close();
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		if (type == 0) { //If it's a patient go to the patient portal and set Main.user
			portal = "Patient Portal.fxml";
			Main.user.setAll("Patient", username.getText());
		}
		else if (type == 1) { //If it's a nurse go to the nurse portal and set Main.user
			portal = "Nurse Portal.fxml";
			Main.user.setAll("Nurse", username.getText());
		}
		else if (type == 2) { //If it's a doctor go to the doctor portal and set Main.user
			portal = "Doctor Portal.fxml";
			Main.user.setAll("Doctor", username.getText());
		}
		else {
			portal = "Main.fxml"; //If the login information is wrong send an alert
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Information Entered");
            alert.setHeaderText(null);
            alert.setContentText("Something you entered is incorrect, please look over your information and try again.");

            alert.showAndWait();
			
		}
		
		root = FXMLLoader.load(getClass().getResource(portal)); //Go to the correct portal
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

	public void savePatientEdits(ActionEvent event) throws IOException {
		String Dob = PatientEditDOB.getText(); //Get the text from all of the TextFields
		String Address = PatientEditAddress.getText();
		String PhoneNumber = PatientEditPhoneNumber.getText();
		String Email = PatientEditEmail.getText();
		boolean validated = true;

		if(!Pattern.compile("(?:0[1-9]|1[0-2]|[1-9])\\/(?:[1-9]|[12][0-9]|0[1-9]|3[01])\\/[0-9]{4}").matcher(Dob).find()) { //Matches a date of birth in the form MM/DD/YYYY M/DD/YYYY MM/D/YYYY M/D/YYYY
			validated = false;
		} else if(!Pattern.compile("\\d{1,5}(\\s[\\w-.,]*){1,6}").matcher(Address).find()) { //Somehow matches any US address
			validated = false;
		} else if(!Pattern.compile("\\d{10}").matcher(PhoneNumber).find()) { //Matches 10 digits, or a "Phone Number"
			validated = false;
		} else if(!Pattern.compile("(?:^|\\s)[\\w!#$%&'*+/=?^`{|}~-](\\.?[\\w!#$%&'*+/=?^`{|}~-]+)*@\\w+[.-]?\\w*\\.[a-zA-Z]{2,3}\\b").matcher(Email).find()) { //Somehow matches any email
			validated = false;
		}

		if(!validated) { //If something was in the wrong format send an alert
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Invalid Information Entered");
			alert.setHeaderText(null);
			alert.setContentText("Please make sure all of your information is in the correct format");

			alert.showAndWait();
			return;
		}

		SQLiteDataSource ds = null;
		ResultSet result;
		ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:info.db");

		try ( Connection conn = ds.getConnection();
			  Statement stmt = conn.createStatement(); ) {
			stmt.executeUpdate("" +
					"UPDATE PatientInfoDb " + //Update the patient's information
					"SET Dob = '" + Dob + "', Email_add = '" + Email + "', Address = '" + Address + "', Phone_num = '" + PhoneNumber + "' " +
					"WHERE Id = " + Main.user.getId() + ";"
			);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
	
	public void hover(MouseEvent event) { //Make the back arrows look fancy when hovered
		Button hoveredButton = (Button)event.getSource();
		hoveredButton.setStyle("""
                -fx-background-radius: 0;
                -fx-border: none;
                -fx-background-color: #bfbfbf;""");
	}

	public void unHover(MouseEvent event) { //Make the back arrows look normal when unhovered
		Button hoveredButton = (Button)event.getSource();
		hoveredButton.setStyle("""
                -fx-background-radius: 0;
                -fx-border: none;
                -fx-background-color: none;""");
	}
}
 