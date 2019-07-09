package core;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Date;

//Model-class for data that will be displayed in the table
public class DataEntry extends RecursiveTreeObject<DataEntry> {

    private StringProperty group;
    private StringProperty activity;
    private StringProperty mandatory;
    private ObjectProperty start;
    private ObjectProperty plannedEnd;
    private ObjectProperty end;
    private StringProperty responsible;
    private StringProperty status;

    public DataEntry(String group, String activity, String mandatory, Date start, Date plannedEnd, Date end, String responsible, String status) {
        this.group = new SimpleStringProperty(group);
        this.activity = new SimpleStringProperty(activity);
        this.mandatory = new SimpleStringProperty(mandatory);
        this.start = new SimpleObjectProperty<>(start);
        this.plannedEnd = new SimpleObjectProperty<>(plannedEnd);
        this.end = new SimpleObjectProperty<>(end);
        this.responsible = new SimpleStringProperty(responsible);
        this.status = new SimpleStringProperty(status);
    }

    //Below are all the getters and setters for the properties
    public String getGroup() {
        return group.get();
    }

    public StringProperty groupProperty() {
        return group;
    }

    public void setGroup(String group) {
        this.group.set(group);
    }

    public String getActivity() {
        return activity.get();
    }

    public StringProperty activityProperty() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity.set(activity);
    }

    public String getMandatory() {
        return mandatory.get();
    }

    public StringProperty mandatoryProperty() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory.set(mandatory);
    }

    public Object getStart() {
        return start.get();
    }

    public ObjectProperty startProperty() {
        return start;
    }

    public void setStart(Object start) {
        this.start.set(start);
    }

    public Object getPlannedEnd() {
        return plannedEnd.get();
    }

    public ObjectProperty plannedEndProperty() {
        return plannedEnd;
    }

    public void setPlannedEnd(Object plannedEnd) {
        this.plannedEnd.set(plannedEnd);
    }

    public Object getEnd() {
        return end.get();
    }

    public ObjectProperty endProperty() {
        return end;
    }

    public void setEnd(Object end) {
        this.end.set(end);
    }

    public String getResponsible() {
        return responsible.get();
    }

    public StringProperty responsibleProperty() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible.set(responsible);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setNull(String id) {
        int i = Integer.parseInt(id);
        switch (i) {
            case 0:
                System.out.println("Group can't be deleted.");
                break;
            case 1:
                setActivity(null);
                break;
            case 2:
                System.out.println("Mandatory can't be deleted.");
                break;
            case 3:
                setStart(null);
                break;
            case 4:
                setPlannedEnd(null);
                break;
            case 5:
                setEnd(null);
                break;
            case 6:
                setResponsible(null);
                break;
            case 7:
                System.out.println("Status can't be deleted.");
                break;
        }
    }
}

