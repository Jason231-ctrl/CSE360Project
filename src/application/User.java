package application;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User { //User is who is currently logged in
    private String type, firstName, lastName, dob, address, phoneNumber, email;
    private int doctorID, id;
    private ArrayList<Integer> nurseID;
    private ArrayList<Prescription> prescriptions;

    public User() { //Default empty user
        type = "";
        firstName = "";
        lastName = "";
        dob = "";
        address = "";
        phoneNumber = "";
        email = "";
        doctorID = 0;
        id = 0;
        nurseID = new ArrayList<Integer>();
        prescriptions = new ArrayList<Prescription>();
    }

    public void setAll(String type, String username) { //Set all of the information based on username
        this.type = "";
        firstName = "";
        lastName = "";
        dob = "";
        address = "";
        phoneNumber = "";
        email = "";
        doctorID = 0;
        id = 0;
        nurseID = new ArrayList<Integer>();
        prescriptions = new ArrayList<Prescription>();

        setType(type);

        SQLiteDataSource ds = null;
        ResultSet result = null;
        ds = new SQLiteDataSource();
        ds.setUrl("jdbc:sqlite:info.db");
        try(Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();) {

            setType(type); //Set the type of user
            if(type.compareTo("Patient") == 0) { //If it's a patient
                result = stmt.executeQuery("SELECT * FROM PatientInfoDb WHERE Username = '" + username + "'"); //Get all of the user information

                setDoctorID(result.getInt("DoctorID")); //Set all of said information
                setAddress(result.getString("Address"));
                setEmail(result.getString("Email_add"));
                setPhoneNumber(result.getString("Phone_num"));

                String[] nurseStringArr = result.getString("Nurse").split(","); //Split nurses into an arrayList
                for (String s : nurseStringArr) {
                    nurseID.add(Integer.parseInt(s));
                }

                if(result.getString("Perscriptions") != null) { //Parse prescriptions
                    String[] prescriptionStringArr = result.getString("Perscriptions").split("\\|");
                    if(result.getString("Perscriptions").split("|").length == 1) {
                        prescriptions.add(new Prescription(result.getString("Perscriptions")));
                    } else {
                        for (String s : prescriptionStringArr) {
                            prescriptions.add(new Prescription(s));
                        }
                    }
                }

            } else if(type.compareTo("Nurse") == 0) { //Nurses and doctors have no real special things about them other than their Id, so nothing is done here
                result = stmt.executeQuery("SELECT * FROM NurseInfoDb WHERE Username = '" + username + "'");
            } else {
                result = stmt.executeQuery("SELECT * FROM DoctorInfoDb WHERE Username = '" + username + "'");
            }

            setFirstName(result.getString("First_name")); //Sets the common stuff between every user type
            setLastName(result.getString("Last_name"));
            setDob(result.getString("Dob"));
            setId(result.getInt("Id"));
        } catch (SQLException e) {
            //e.printStackTrace();
        }

    }

    //Getters and setters
    public int getDoctorID() { return doctorID; }

    public void setDoctorID(int newDoctorID) { doctorID = newDoctorID; }

    public int getId() { return id; }

    public void setId(int newId) { id = newId; }

    public String getType() { return type; }

    public void setType(String newType) { type = newType; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
