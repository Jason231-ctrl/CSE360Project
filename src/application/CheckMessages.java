package application;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CheckMessages {
    @FXML
    private TableView<Conversation> Table;

    private ArrayList<Conversation> conversations = new ArrayList<Conversation>(java.util.List.of(new Conversation("Dr. House", "Thanks Doc!"))); //Replace this later with getting the conversations from a DB

    public void initialize() {
        Table.setRowFactory(tableView -> {
            TableRow<Conversation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())) {
                    Conversation rowData = row.getItem();
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("messaging.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
                        Stage stage = new Stage();
                        stage.setResizable(false);
                        stage.setTitle("MedGo: Messaging ".concat(rowData.getWith()));
                        stage.setScene(scene);
                        Messaging messagingController = fxmlLoader.getController();
                        messagingController.loadMessages(rowData);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    @FXML
    private void back() throws IOException {
        Scene newScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Patient Portal.fxml"))));
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
