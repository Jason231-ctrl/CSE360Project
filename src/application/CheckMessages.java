package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.sqlite.SQLiteDataSource;

import java.io.IOException;
import java.util.Objects;

public class CheckMessages {
    @FXML
    private TableView<Conversation> Table;

    public void initialize() {
    	initializeTable();
    }
    
    private void initializeTable() {
    	SQLiteDataSource ds = new SQLiteDataSource();
    	ds.setUrl("jdbc:sqlite:info.db");
    	ResultSet rs = null;
    	PreparedStatement ps = null;
    	try {
    	Connection con = ds.getConnection();
    	String sql = "SELECT Receiver, Messages from MessagesDB where Sender = 'User'";
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
        Scene newScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Patient Portal.fxml"))));
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

