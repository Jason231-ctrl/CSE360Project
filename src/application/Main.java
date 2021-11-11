package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.sqlite.SQLiteDataSource;





public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Login Screen");
			
		
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException {
		launch(args);	
		// creates the data base for information of Doctor/Nurse/Patient
		infoDb();
		//this is a test to add people. Comment out if already did once.
		//insertPatient("Andy", "Wang", "awa214", "1234", "12301978", 1024, "CharlesPenelope", "987 E. Prince St.", "andy12@yahoo.com", "Tyenol", 114321234, 10134);
		//insertPatient("Charles", "tamb", "cheetah33", "Charles11", "02211975", 1025, "CandiceAshley", "1234 W. Ash St.", "Cheet12@gmail.com", "Advil", 114321234, 10112);
	}
	
	public static void infoDb() {
		//SQLiteDataSource is a interface for the database
		SQLiteDataSource ds = null;
		//this setUrl tells the interface where to look
		ds = new SQLiteDataSource();
		//if it can't find it, it will make a new database.
		//do this for the different types of data bases.
		// this is the relative path for info.db
		ds.setUrl("jdbc:sqlite:info.db");
		//This is how the database was created
		// If it doesn't exist it creates a new one.
		String createPatient = "CREATE TABLE IF NOT EXISTS PatientInfoDb ( " +
                "First_name TEXT NOT NULL, " +
                "Last_name TEXT NOT NULL, " +
                "Username TEXT NOT NULL, " +
                "Password TEXT NOT NULL, " +
                "Dob TEXT NOT NULL, " +   // dob is ##/##/#### number can be parsed out
                "DoctorID Integer, " +  // pick the doctor 
                "Nurse TEXT, " +   // assigned after picking doctor, can be NULL
                "Address TEXT NOT NULL, " +
                "Email_add TEXT NOT NULL, " +
                "Perscriptions TEXT, " + //can be null, doctor adds it in.
                "Phone_num INTEGER NOT NULL," +
                "Id INTEGER NOT NULL PRIMARY KEY )";
		
		//depending on which radial button you pick, insert a new doctor/nurse
		String createDoctor = "CREATE TABLE IF NOT EXISTS DoctorInfoDb ( " +
                "First_name TEXT NOT NULL, " +
                "Last_name TEXT NOT NULL, " +
                "Username TEXT NOT NULL, " +
                "Password TEXT NOT NULL, " +
                "Patients TEXT NOT NULL, " +
                "Nurses TEXT NOT NULL, " +   // nurses already assigned.
                "Dob TEXT NOT NULL, " +   // dob is ##/##/#### number can be parsed out
                "Id INTEGER NOT NULL PRIMARY KEY )";
                
        String createNurse = "CREATE TABLE IF NOT EXISTS NurseInfoDb ( " +
                "First_name TEXT NOT NULL, " +
                "Last_name TEXT NOT NULL, " +
                "Username TEXT NOT NULL, " +
                "Password TEXT NOT NULL, " +
                //SQLite doesn't support array as data type
                //so its going to be a large test separated by , and parse out info
                "Patients TEXT NOT NULL, " +
                "Dob TEXT NOT NULL, " + 
                "Id INTEGER NOT NULL PRIMARY KEY )";
        
        String createResult = "CREATE TABLE IF NOT EXISTS ResultDb ( " +
        		//data base that as the results of vital test per patient.
        		//have a preset defined range, example: O2 levels
        		// switch:
        		// case x > 98 //Normal; case 97 > x > 95 //Insufficent; case x < 90 //Critical;
        		"Id INTEGER NOT NULL PRIMARY KEY, " +
        		"Temp INTEGER NOT NULL, " +
        		"Pulse INTEGER NOT NULL, " +
        		"BPsys INTEGER NOT NULL, " +
        		"BPdis INTEGER NOT NULL, " +
        		"HEIGHT INTEGER NOT NULL, " +
        		"WEIGHT INTEGER NOT NULL, " +
        		"O2 INTEGER NOT NULL)";
        
        String drugDB = "CREATE TABLE IF NOT EXISTS DrugDb ( " +
        		"Name TEXT NOT NULL PRIMARY KEY, " +
        		"Desc TEXT NOT NULL)";
        
        String accountDb = "CREATE TABLE IF NOT EXISTS AccountDb ( " +
        		"Username TEXT NOT NULL PRIMARY KEY, " +
        		"Password TEXT NOT NULL, " +
        		// 0 - Patient, 1 - Nurse, 2 - Doctor
        		"Type TEXT NOT NULL)";
        
        try (Connection conn = ds.getConnection()) {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(createNurse);
			stmt.executeUpdate(createDoctor);
			stmt.executeUpdate(createPatient);
			stmt.executeUpdate(createResult);
			stmt.executeUpdate(drugDB);
			stmt.executeUpdate(accountDb);
			
			/* testing if it works:
			 *  use .executeUpdate() for CREATE, INSERT, DELETE, or UPDATE statement.
			 *  use .executeQuery() for SELECT
			 */
			
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}
	
	public static void insertPatient(String first, String last, String username, String password,
			String dob, int doctor, String nurse, String address, String email, String prescription, int phone, int id) {
		
		String query = "INSERT INTO PatientInfoDb(First_name,Last_name,Username,Password,Dob,DoctorID,Nurse,Address,Email_add,Perscriptions,Phone_num,Id) "
				+ "VALUES('" + first + "','" + last + "','" + username + "','" + password + "','" + dob + "',"
				 + doctor + ",'" + nurse + "','" + address + "','" + email + "','" + prescription + "'," + phone + "," + id + ")";
		
		String query1 = "INSERT INTO AccountDb(Username,Password,Type) "
				+ "VALUES('" + username + "','" + password + "'," + 0 + ")";
		
		SQLiteDataSource ds = null;
		ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:info.db");
		 try (Connection conn = ds.getConnection()) {
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(query);
				stmt.executeUpdate(query1);
		 } catch ( SQLException e) {
			 e.printStackTrace();
		 }
		
	}
}
