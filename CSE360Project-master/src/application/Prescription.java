package application;

import javafx.beans.property.SimpleStringProperty;

public class Prescription {
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty description = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");

    public Prescription() {
        this("", "", "");
    }

    public Prescription(String name, String description, String type) {
        setName(name);
        setDescription(description);
        setType(type);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String newName) {
        name.set(newName);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String newDescription) {
        description.set(newDescription);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String newType) {
        type.set(newType);
    }
}
