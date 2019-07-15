package core;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

import java.util.Date;
import java.util.Objects;

//Model-class for data that will be displayed in the table
public class DataEntry extends RecursiveTreeObject<DataEntry> {

    private IntegerProperty id;
    private StringProperty group;
    private StringProperty activity;
    private StringProperty mandatory;
    private ObjectProperty<Date> start;
    private ObjectProperty<Date> plannedEnd;
    private ObjectProperty<Date> end;
    private StringProperty responsible;
    private StringProperty status;

    public DataEntry(String group, String activity, String mandatory, Date start, Date plannedEnd, Date end, String responsible, String status) {
        this.id = new SimpleIntegerProperty(0);
        this.group = new SimpleStringProperty(group);
        this.activity = new SimpleStringProperty(activity);
        this.mandatory = new SimpleStringProperty(mandatory);
        this.start = new SimpleObjectProperty<>(start);
        this.plannedEnd = new SimpleObjectProperty<>(plannedEnd);
        this.end = new SimpleObjectProperty<>(end);
        this.responsible = new SimpleStringProperty(responsible);
        this.status = new SimpleStringProperty(status);
    }

    public DataEntry(int id, String group, String activity, String mandatory, Date start, Date plannedEnd, Date end, String responsible, String status) {
        this.id = new SimpleIntegerProperty(id);
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
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

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

    public Date getStart() {
        return start.get();
    }

    public ObjectProperty startProperty() {
        return start;
    }

    public void setStart(Date start) {
        this.start.set(start);
    }

    public Date getPlannedEnd() {
        return plannedEnd.get();
    }

    public ObjectProperty plannedEndProperty() {
        return plannedEnd;
    }

    public void setPlannedEnd(Date plannedEnd) {
        this.plannedEnd.set(plannedEnd);
    }

    public Date getEnd() {
        return end.get();
    }

    public ObjectProperty endProperty() {
        return end;
    }

    public void setEnd(Date end) {
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

    @Override
    public String toString() {
        return getGroup() + " " + getActivity() + " " + getMandatory() + " " + getStart() + " "
                + getPlannedEnd() + " " + getEnd() + " " + getResponsible() + " " + getStatus();
    }


    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof DataEntry)) {
            return false;
        }
        DataEntry entry = (DataEntry) o;
        return Objects.equals(group.getValue(), entry.group.getValue()) &&
                Objects.equals(activity.getValue(), entry.activity.getValue()) &&
                Objects.equals(mandatory.getValue(), entry.mandatory.getValue()) &&
                Objects.equals(start.get(), entry.start.get()) &&
                Objects.equals(plannedEnd.get(), entry.plannedEnd.get()) &&
                Objects.equals(end.get(), entry.end.get()) &&
                Objects.equals(responsible.getValue(), entry.responsible.getValue()) &&
                Objects.equals(status.getValue(), entry.status.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, activity, mandatory, start, plannedEnd, end, responsible, status);
    }
}

