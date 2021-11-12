package application;

import javafx.beans.property.SimpleStringProperty;

public class Prescription {
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty description = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty dose = new SimpleStringProperty("");

    public Prescription() {
        this("", "", "", "");
    }

    public Prescription(String name, String description, String type, String dose) {
        setName(name);
        setDescription(description);
        setType(type);
        setDose(dose);
    }

    public Prescription(String allInOne) {
        String[] split = allInOne.split(",");
        setName(split[0]);
        setDescription(split[1]);
        setType(split[2]);
        setDose(split[3]);
    }

    @Override
    public String toString() {
        return getName() + "," + getDescription() + "," + getType() + "," + getDose();
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

    public String getDose() { return dose.get(); }

    public void setDose(String newDose) { dose.set(newDose); }
}
