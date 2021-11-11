package application;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Messaging {
    @FXML
    private TableView<Message> Table;

    private Conversation conversation = new Conversation();

    public void loadMessages(Conversation messages) {
        conversation = messages;
        for(int i = 0; i < conversation.getSize(); i++) {
            Table.getItems().add(conversation.getMessage(i));
        }

    }
}
