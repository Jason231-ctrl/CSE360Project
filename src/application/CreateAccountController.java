package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateAccountController {
    @FXML
    private CheckBox DoctorNurseBox;
    @FXML
    private TextField IDInput, Username, Password, FirstName, LastName, DoB;

    @FXML
    protected void changeDoctorNurse() {
        if(DoctorNurseBox.isSelected()) {
            IDInput.setManaged(true);
            IDInput.setVisible(true);
        } else {
            IDInput.setManaged(false);
            IDInput.setVisible(false);
        }
    }

    @FXML
    protected void cancelCreation() throws IOException {
    	String scene = "Main.fxml";
        Scene newScene = new Scene(FXMLLoader.load(getClass().getResource(scene)));
        Stage window = (Stage) Username.getScene().getWindow();
        window.setScene(newScene);
    }

    @FXML
    protected void createAccount() throws IOException {
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
