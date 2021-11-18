package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.sql.ResultSet;

import org.sqlite.SQLiteDataSource;

public class CreateAccountController implements Initializable{
	@FXML
	private ChoiceBox<String> DoctorSelection;
    @FXML
    private CheckBox DoctorNurseBox;
    @FXML
    private TextField IDInput, Username, Password, FirstName, LastName, DoB, Address, EmailAddress, PhoneNumber;
    @Override
    public void initialize(URL arg0,ResourceBundle hi) {
    	SQLiteDataSource ds = null;
		ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:info.db");
    	try (Connection conn = ds.getConnection();
				Statement stmt = conn.createStatement();) {
    		ArrayList<String> doctors = new ArrayList<String>();
			 	ResultSet doctoring = stmt.executeQuery("SELECT First_name,Last_name FROM DoctorInfoDb");
			 	while(doctoring.next()) {
			 		doctors.add(doctoring.getString("First_name") + " " + doctoring.getString("Last_name"));
			 	}
			 	DoctorSelection.getItems().addAll(doctors);
				DoctorSelection.setValue(doctors.get(0));
		 } catch ( SQLException e) {
			 e.printStackTrace();
		 }
    }

    @FXML
    protected void cancelCreation() throws IOException {
    	String scene = "Main.fxml";
        Scene newScene = new Scene(FXMLLoader.load(getClass().getResource(scene)));
        Stage window = (Stage) Username.getScene().getWindow();
        window.setScene(newScene);
        System.out.println(DoctorSelection.getSelectionModel().isEmpty());
    }

    @FXML
    protected void createAccount() throws IOException {
    	String createAccountPatientQuery;
    	String createAccountDoctorQuery;
    	String createAccountNurseQuery;
    	String createAccountQuery;
    	SQLiteDataSource ds = null;
		ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:info.db");
		 try (Connection conn = ds.getConnection();
				Statement stmt = conn.createStatement();) {
			 	int smallestNurse = Integer.MAX_VALUE;
			 	int patientNumber = Integer.MAX_VALUE;
			 	int doctorIdInt;
			 	int counted;
			 	ResultSet count = stmt.executeQuery("SELECT COUNT(Id) FROM PatientInfoDb");
			 	counted = count.getInt("Count(Id)");
			 	String[] name = DoctorSelection.getValue().split(" ");
			 	ResultSet doctorFind = stmt.executeQuery("SELECT Id,Patients FROM DoctorInfoDb WHERE First_name = '" + name[0] + "' AND Last_name = '" + name[1] + "'");
			 	doctorIdInt = doctorFind.getInt("Id");
			 	String patientDoctorList =  doctorFind.getString("Patients");
			 	ResultSet nursePatients = stmt.executeQuery("SELECT Patients,Id FROM NurseInfoDb WHERE Doctor = " + doctorIdInt);
			 	String patientNurseList = nursePatients.getString("Patients");
			 	// this do-while loop will find the nurses that are working with the doctors
			 	// and assign the nurse with the lowest amount of patients to the new patient.
			 	do {
			 		int temp = patientNurseList.split(",").length;
			 		if(temp < patientNumber) {
			 			smallestNurse = nursePatients.getInt("Id");
			 			patientNumber = temp;
			 		}
			 	} while (nursePatients.next());
			 	// if the table is empty then first id starts at 1000.
			 	// creates a new patient.
			 		createAccountPatientQuery = "INSERT INTO PatientInfoDb (First_name,Last_name,Username,Password,Dob,DoctorID,Nurse,Address,Email_add,Phone_num,Id) VALUES('" + 
			 			FirstName.getText() + "','" + 
			 			LastName.getText() + "','" + 
			 			Username.getText() + "','" + 
			 			Password.getText() + "','" + 
			 			DoB.getText() + "'," +
			 			doctorIdInt + ",'" + 
			 			smallestNurse + "','" + 
			 			Address.getText() + "','" + 
			 			EmailAddress.getText() + "'," + 
			 			PhoneNumber.getText() + "," + 
			 			(1000+counted) + 
			 			")";
			 		createAccountQuery = "INSERT INTO AccountDb (Username,Password,Type) VALUES('" + 
				 			Username.getText() + "','" + 
				 			Password.getText() + "'," + 
				 			"0)";
			 		createAccountDoctorQuery = "UPDATE DoctorInfoDb SET Patients = '" + (patientDoctorList + "," + (1000 + counted)) + "' WHERE Id = '" + doctorIdInt + "'";
			 		createAccountNurseQuery = "UPDATE NurseInfoDb SET Patients = '" + (patientNurseList + "," + (1000 + counted)) + "' WHERE Id = '" + smallestNurse + "'";
			 		
			 	stmt.executeUpdate(createAccountPatientQuery);
			 	stmt.executeUpdate(createAccountQuery);
		 } catch ( SQLException e) {
			 e.printStackTrace();
		 }
        String scene = "Patient Portal.fxml";
        Scene newScene = new Scene(FXMLLoader.load(getClass().getResource(scene)), 1000, 1000);
        Stage window = (Stage) Username.getScene().getWindow();
        window.setScene(newScene);
    }

    @FXML
    protected void hover(MouseEvent event) {
        Button hoveredButton = (Button)event.getSource();
        hoveredButton.setStyle("" +
                "-fx-background-color: #a4d5fc;\n" +
                "-fx-text-fill: white;\n" +
                "-fx-font-weight: bolder;\n" +
                "-fx-font-size: 14px;\n" +
                "-fx-background-radius: 10;");
    }

    @FXML
    protected void unHover(MouseEvent event) {
        Button hoveredButton = (Button)event.getSource();
        hoveredButton.setStyle("" +
                "-fx-background-color: #78c4ff;\n" +
                "-fx-text-fill: white;\n" +
                "-fx-font-weight: bolder;\n" +
                "-fx-font-size: 14px;\n" +
                "-fx-background-radius: 10;");
    }
}
