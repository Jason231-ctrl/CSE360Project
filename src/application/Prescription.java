package application;

import javafx.beans.property.SimpleStringProperty;

public class Prescription {
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty description = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private float dose = 0;

    public Prescription() {
        this("", "", "", 0);
    }

    public Prescription(String name, String description, String type, float dose) {
        setName(name);
        setDescription(description);
        setType(type);
        this.dose = dose;
    }

    public Prescription(String allInOne) {
        String[] split = allInOne.split(",");
        setName(split[0]);
        setDescription(split[1]);
        setType(split[2]);
        setDose(Integer.parseInt(split[3]));
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

    public float getDose() { return dose; }

    public void setDose(float newDose) { dose = newDose; }
}
