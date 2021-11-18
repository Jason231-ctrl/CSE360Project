package application;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

    private ArrayList<String> receiverNames = new ArrayList<String>();
	String toText;
	String message = "";
	
	
	public void initialize() {
		insertTable();
		docNurseDropdown.getItems().addAll(receiverNames);
		docNurseDropdown.setOnAction(this::getName);
	}
	
	public void insertTable() {
		SQLiteDataSource ds = new SQLiteDataSource();
    	ds.setUrl("jdbc:sqlite:info.db");
    	ResultSet rs = null;
    	PreparedStatement ps = null;
    	try {
    		Connection con = ds.getConnection();
    		String sql = "";
    		String name;
    		if (Main.user.getType() =="Doctor") {
    			sql = "SELECT b.First_name, b.Last_name from PatientInfoDb b, DoctorInfoDb c where b.DoctorID = c.Id AND c.Id = " + Main.user.getId();
    			ps = con.prepareStatement(sql);
    	    	rs = ps.executeQuery();
    	    	while(rs.next()) {
    	    		name = rs.getString("First_name") + " " + rs.getString("Last_name");
    	    		System.out.println(name);
    	    		receiverNames.add(name);
    	    	}
    		} else if (Main.user.getType() =="Nurse") {
    			sql = "SELECT b.First_name, b.Last_name from PatientInfoDb b, NurseInfoDb c where b.Nurse = c.Id AND c.Id = " + Main.user.getId();
    			ps = con.prepareStatement(sql);
    	    	rs = ps.executeQuery();
    	    	while(rs.next()) {
    	    		name = rs.getString("First_name") + " " + rs.getString("Last_name");
    	    		System.out.println(name);
    	    		receiverNames.add(name);
    	    	}
    		} else if (Main.user.getType() =="Patient") {
    			sql = "SELECT b.First_name, b.Last_name from DoctorInfoDb b, PatientInfoDb c where c.DoctorID = b.Id AND c.Id = " + Main.user.getId();
    			ps = con.prepareStatement(sql);
    	    	rs = ps.executeQuery();
    	    	while(rs.next()) {
    	    		name = rs.getString("First_name") + " " + rs.getString("Last_name");
    	    		System.out.println(name);
    	    		receiverNames.add(name + "(Doctor)");
    	    	}
    	    	sql = "SELECT b.First_name, b.Last_name from NurseInfoDb b, PatientInfoDb c where c.Nurse = b.Id AND c.Id = " + Main.user.getId();
    	    	ps = con.prepareStatement(sql);
    	    	rs = ps.executeQuery();
    	    	while(rs.next()) {
    	    		name = rs.getString("First_name") + " " + rs.getString("Last_name");
    	    		System.out.println(name);
    	    		receiverNames.add(name + "(Nurse)");
    	    	}
    		}
    	} catch (SQLException e) {
    		System.out.println(e.toString());
    	} finally {
    		try {
    			rs.close();
    			ps.close();
    		} catch(SQLException e) {
    			System.out.println(e.toString());
    		}
    	}
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
			insertMessage(Main.user.getFirstName() + " " + Main.user.getLastName(), toText.replaceAll("(\\(Doctor\\)|\\(Nurse\\))", ""), message);
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