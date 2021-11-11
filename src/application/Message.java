package application;

import javafx.beans.property.SimpleStringProperty;

public class Message {
    private final SimpleStringProperty message = new SimpleStringProperty("");
    private final SimpleStringProperty from = new SimpleStringProperty("");

    public Message() {
        this("", "");
    }

    public Message(String from, String message) {
        setMessage(message);
        setFrom(from);
    }

    public void setMessage(String newMessage) {
        message.set(newMessage);
    }

    public String getMessage() {
        return message.get();
    }

    public void setFrom(String newFrom) {
        from.set(newFrom);
    }

    public String getFrom() {
        return from.get();
    }
}
