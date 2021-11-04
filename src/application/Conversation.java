package application;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class Conversation {
    private final SimpleStringProperty with = new SimpleStringProperty("");
    private final SimpleStringProperty lastMessage = new SimpleStringProperty("");
    private ArrayList<Message> messages = new ArrayList<Message>(Arrays.asList(
            new Message("Eat Vegetables!", "Dr. House", "2021-10-18T12:33:05.114235"),
            new Message("NO!", "John Freeman", "2021-10-18T12:37:12.556874"),
            new Message("Please?", "Dr. House", "2021-10-18T12:38:07.453528"),
            new Message("I SAID NO", "John Freeman", "2021-10-18T12:38:16.457823"),
            new Message("Free lollipops", "Dr. House", "2021-10-18T12:38:20.321854"),
            new Message("Ok fine", "John Freeman", "2021-10-18T12:33:26.789545"),
            new Message("Thanks Doc!", "John Freeman", "2021-10-19T14:06:32.876452")
    ));

    public Conversation() {
        this("", "");
    }

    public Conversation(String with, String lastMessage) {
        setWith(with);
        setLastMessage(lastMessage);
    }

    public void setWith(String newWith) {
        with.set(newWith);
    }

    public String getWith() {
        return with.get();
    }

    public void setLastMessage(String newMessage) {
        lastMessage.set(newMessage);
    }

    public String getLastMessage() {
        return lastMessage.get();
    }

    public void addMessage(String from, String message) {
        messages.add(new Message(message, from, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
    }

    public Message getMessage(int index) {
        return messages.get(index);
    }

    public int getSize() {
        return messages.size();
    }
}
