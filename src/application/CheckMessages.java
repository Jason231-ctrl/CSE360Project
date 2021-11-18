package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.sqlite.SQLiteDataSource;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CheckMessages {
    @FXML
    private TableView<Conversation> Table;
    

    public void initialize() {
    	initializeMessageTable();
    }
    
    
    private void initializeMessageTable() {
    	SQLiteDataSource ds = new SQLiteDataSource();
    	ds.setUrl("jdbc:sqlite:info.db");
    	ResultSet rs = null;
    	PreparedStatement ps = null;
    	try {
    	Connection con = ds.getConnection();
    	String user = Main.user.getFirstName() + " " + Main.user.getLastName();
    	System.out.println(user);
    	String sql = "SELECT Receiver, Messages from MessagesDB where Sender = '" + user + "'";
    	ps = con.prepareStatement(sql);
    	rs = ps.executeQuery();
    	while(rs.next()) {
    		String sender = rs.getString("Receiver");
    		String message = rs.getString("Messages");
    		Table.getItems().add(new Conversation(sender, message));
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
    

    @FXML
    private void back() throws IOException {
    	Scene newScene;
    	if (Main.user.getType() == "Patient") {
    		newScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Patient Portal.fxml"))));
    	} else if (Main.user.getType() == "Doctor") {
    		newScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Doctor Portal.fxml"))));
    	} else if (Main.user.getType() == "Nurse") {
    		newScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Nurse Portal.fxml"))));
    	} else {
    		newScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml"))));
    	}
        Stage window = (Stage) Table.getScene().getWindow();
        window.setScene(newScene);
    }
    
    @FXML
    private void newMessage() throws IOException {
    	try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("New-Message.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void hover(MouseEvent event) {
        Button hoveredButton = (Button)event.getSource();
        hoveredButton.setStyle("""
                -fx-background-radius: 0;
                -fx-border: none;
                -fx-background-color: #bfbfbf;""");
    }

    @FXML
    protected void unHover(MouseEvent event) {
        Button hoveredButton = (Button)event.getSource();
        hoveredButton.setStyle("""
                -fx-background-radius: 0;
                -fx-border: none;
                -fx-background-color: none;""");
    }
}

