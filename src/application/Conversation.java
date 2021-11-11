package application;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Conversation {
    public ArrayList<Message> messages = new ArrayList<Message>();
    private Scanner scanner;
    private final SimpleStringProperty with = new SimpleStringProperty("");
    private final SimpleStringProperty lastMessage = new SimpleStringProperty("");
    		
    public Conversation() {
    	this("","");
    }
    
    public Conversation(String with, String lastMessage) {
    	openFile();
    	readFile();
    	setWith(with);
    	setLastMessage(lastMessage);
    	
    }
    
    public void openFile() {
    	try {
    		scanner = new Scanner(new File("Messages.txt"));
    	} catch (FileNotFoundException e) {
    		System.out.println("file not found");
    	}
    }
    
    public void readFile() {
    	while (scanner.hasNextLine()) {
			String name = scanner.nextLine();
			String text = scanner.nextLine();
			messages.add(new Message(name, text));
		}
		scanner.close();
    }
    

    
    public Message getMessage(int index) {
    	return messages.get(index);
    }
    
    public int getSize() {
    	return messages.size();
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
        messages.add(new Message(message, from));
    }

    
 
    
    
}
    
