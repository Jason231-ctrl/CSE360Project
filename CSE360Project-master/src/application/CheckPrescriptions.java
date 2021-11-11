package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CheckPrescriptions {
    @FXML
    private TableView<Prescription> Table;

    @FXML
    private void back() throws IOException {
        Scene newScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("patient-portal.fxml"))), 600, 400);
        Stage window = (Stage) Table.getScene().getWindow();
        window.setScene(newScene);
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
