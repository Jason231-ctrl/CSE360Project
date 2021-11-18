package application;

import javafx.beans.property.SimpleStringProperty;

public class Prescription {
    private final SimpleStringProperty name = new SimpleStringProperty(""); //4 String values
    private final SimpleStringProperty description = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty dose = new SimpleStringProperty("");

    public Prescription() {
        this("", "", "", "");
    } //Default constructor for an empty call

    public Prescription(String name, String description, String type, String dose) { //One constructor for passing all the data individually
        setName(name);
        setDescription(description);
        setType(type);
        setDose(dose);
    }

    public Prescription(String allInOne) { //One constructor for passing a string delimited by commas
        String[] split = allInOne.split(",");
        setName(split[0]);
        setDescription(split[1]);
        setType(split[2]);
        setDose(split[3]);
    }

    @Override
    public String toString() { //Method for converting a Prescription to a String
        return getName() + "," + getDescription() + "," + getType() + "," + getDose();
    }

    //Getters and Setters
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

    public String getDose() { return dose.get(); }

    public void setDose(String newDose) { dose.set(newDose); }
}
