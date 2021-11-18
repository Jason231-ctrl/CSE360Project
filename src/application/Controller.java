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
		SQLiteDataSource ds = null;
		ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:info.db");
		//Start Edit Patient Info
		if(Main.user.getType().compareTo("Patient") == 0) {
			try (Connection conn = ds.getConnection();
				 Statement stmt = conn.createStatement();) {
				ResultSet patientInfo;

				patientInfo = stmt.executeQuery("SELECT Dob, Address, Email_add, Phone_num FROM PatientInfoDb WHERE Id = " + Main.user.getId());

				if(PatientEditAddress != null) PatientEditAddress.setText(patientInfo.getString("Address"));
				if(PatientEditEmail != null) PatientEditEmail.setText(patientInfo.getString("Email_add"));
				if(PatientEditPhoneNumber != null) PatientEditPhoneNumber.setText("" + patientInfo.getInt("Phone_num"));
				if(PatientEditDOB != null) PatientEditDOB.setText(patientInfo.getString("Dob"));
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		}
		//End Edit Patient Info
		//Start Patient Check Results
		if(Main.user.getType().compareTo("Patient") == 0) {
			try (Connection conn = ds.getConnection();
				 Statement stmt = conn.createStatement();) {
				ResultSet patientInfo;

				patientInfo = stmt.executeQuery("SELECT * FROM ResultDb WHERE Id = " + Main.user.getId());
				if(patientInfo.next()) {
					if (PatientBP != null) PatientBP.setText(patientInfo.getString("BP"));
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
		if(Main.user.getType().compareTo("Doctor") == 0) {
			try (Connection conn = ds.getConnection();
				 Statement stmt = conn.createStatement();) {
				ResultSet namesresult = null;
				namesresult = stmt.executeQuery("SELECT First_name,Last_name FROM PatientInfoDb WHERE DoctorID = " + Main.user.getId());

				ArrayList<String> AllPatientNames = new ArrayList<String>();
				while (namesresult.next()) {
					String presFullName = namesresult.getString("First_name") + " " + namesresult.getString("Last_name");
					AllPatientNames.add(presFullName);
				}
				namesresult.close();

				DVitalsPatient.getItems().addAll(AllPatientNames);
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		}
		//End Doctor Check Results
		//Start Prescription
		if(Main.user.getType().compareTo("Doctor") == 0) {
			try (Connection conn = ds.getConnection();
				 Statement stmt = conn.createStatement();) {
				ResultSet namesresult = null;
				ResultSet drugsresult = null;
				namesresult = stmt.executeQuery("SELECT First_name,Last_name FROM PatientInfoDb WHERE DoctorID = " + Main.user.getId());

				ArrayList<String> presAllPatientNames = new ArrayList<String>();
				while (namesresult.next()) {
					String presFullName = namesresult.getString("First_name") + " " + namesresult.getString("Last_name");
					presAllPatientNames.add(presFullName);
				}
				namesresult.close();

				drugsresult = stmt.executeQuery("SELECT * FROM DrugDb");

				ArrayList<String> presDrugNames = new ArrayList<String>();
				while (drugsresult.next()) {
					String presDrugName = drugsresult.getString("Name");
					presDrugNames.add(presDrugName);
				}
				drugsresult.close();

				prescChoiceBox.getItems().addAll(presAllPatientNames);
				prescListView.getItems().addAll(presDrugNames);
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		} else if(Main.user.getType().compareTo("Patient") == 0){
			try (Connection conn = ds.getConnection();
				 Statement stmt = conn.createStatement();) {
				ResultSet prescResult = null;
				prescResult = stmt.executeQuery("SELECT Perscriptions FROM PatientInfoDb WHERE Id = " + Main.user.getId());

				ArrayList<Prescription> prescriptions = new ArrayList<Prescription>();
				String[] prescString = prescResult.getString("Perscriptions") == null ? new String[]{}:prescResult.getString("Perscriptions").split("\\|");
				for(String s : prescString) {
					prescriptions.add(new Prescription(s));
				}
				prescResult.close();

				patientPrescName.getItems().addAll(prescriptions.stream().map(Prescription::getName).collect(Collectors.toList()));
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
			String[] name = DVitalsPatient.getValue().split(" ");
			ResultSet patient = stmt.executeQuery("SELECT * FROM ResultDb WHERE Id = (SELECT Id FROM PatientInfoDb WHERE First_name = '" + name[0] + "' AND Last_name = '" + name[1] + "')");

			if(patient.next()) {
				if (DPatientBP != null) DPatientBP.setText(patient.getString("BP"));
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
		String FirstName = VitalsFirstName.getText();
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
			stmt.execute("" +
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

		root = FXMLLoader.load(getClass().getResource("Nurse Portal.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void sendPrescription(ActionEvent event) throws IOException {
		String selectedPatient = prescChoiceBox.getValue();
		String selectedDrug = prescListView.getSelectionModel().getSelectedItem();
		String currentDose = prescDose.getText();
		String newPrescriptionsString = null;
		int id = 0;

		if(selectedPatient == null || selectedDrug == null || currentDose == null || selectedPatient.compareTo("") == 0 || selectedDrug.compareTo("") == 0 || currentDose.compareTo("") == 0) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
				patientresult = stmt.executeQuery("SELECT Perscriptions, Id FROM PatientInfoDb WHERE First_name = '" + selectedPatient.split(" ")[0] + "' AND Last_name = '" + selectedPatient.split(" ")[1] + "'");
				id = patientresult.getInt("Id");

				String[] prescriptionsString = patientresult.getString("Perscriptions") == null ? new String[]{}:patientresult.getString("Perscriptions").split("\\|");
				ArrayList<Prescription> prescriptions = new ArrayList<Prescription>();
				for (String s : prescriptionsString) {
					prescriptions.add(new Prescription(s));
				}
				patientresult.close();

				drugresult = stmt.executeQuery("SELECT * FROM DrugDb WHERE Name = '" + selectedDrug + "'");
				Prescription newPrescription = new Prescription(selectedDrug, drugresult.getString("Desc"), drugresult.getString("Type"), currentDose);

				prescriptions.add(newPrescription);

				ArrayList<String> prescriptionsStringified = new ArrayList<String>();
				for(Prescription p : prescriptions) {
					prescriptionsStringified.add(p.toString());
				}
				drugresult.close();

				newPrescriptionsString = String.join("|", prescriptionsStringified);

				stmt.executeUpdate("UPDATE PatientInfoDb SET Perscriptions = '" + newPrescriptionsString + "' WHERE Id = " + id);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			root = FXMLLoader.load(getClass().getResource("Doctor Portal.fxml"));
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
		conn.close();
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

	public void savePatientEdits(ActionEvent event) throws IOException {
		String Dob = PatientEditDOB.getText();
		String Address = PatientEditAddress.getText();
		String PhoneNumber = PatientEditPhoneNumber.getText();
		String Email = PatientEditEmail.getText();
		boolean validated = true;

		if(!Pattern.compile("(?:0[1-9]|1[0-2]|[1-9])\\/(?:[1-9]|[12][0-9]|0[1-9]|3[01])\\/[0-9]{4}").matcher(Dob).find()) {
			validated = false;
		} else if(!Pattern.compile("\\d{1,5}(\\s[\\w-.,]*){1,6}").matcher(Address).find()) {
			validated = false;
		} else if(!Pattern.compile("\\d{10}").matcher(PhoneNumber).find()) {
			validated = false;
		} else if(!Pattern.compile("(?:^|\\s)[\\w!#$%&'*+/=?^`{|}~-](\\.?[\\w!#$%&'*+/=?^`{|}~-]+)*@\\w+[.-]?\\w*\\.[a-zA-Z]{2,3}\\b").matcher(Email).find()) {
			validated = false;
		}

		if(!validated) {
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
					"UPDATE PatientInfoDb " +
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
 