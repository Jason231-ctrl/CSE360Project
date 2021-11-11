package application;

import javafx.beans.property.SimpleStringProperty;

public class Message {
    private final SimpleStringProperty message = new SimpleStringProperty("");
    private final SimpleStringProperty from = new SimpleStringProperty("");
    private final SimpleStringProperty time = new SimpleStringProperty("");

    public Message() {
        this("", "", "");
    }

    public Message(String message, String from, String time) {
        setMessage(message);
        setFrom(from);
        setTime(time);
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

    public void setTime(String newTime) {
        time.set(newTime);
    }

    public String getTime() {
        return time.get();
    }
}
