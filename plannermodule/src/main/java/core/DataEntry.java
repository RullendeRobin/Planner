package core;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
    private DoubleProperty progress;
    private IntegerProperty repeatId;
    private StringProperty repeat;

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
        this.progress = new SimpleDoubleProperty(calculateProgress(start, plannedEnd));
    }

    public DataEntry(int id, String group, String activity, String mandatory, Date start, Date plannedEnd, Date end, String responsible, String status, int repeatId, String repeat) {
        this.id = new SimpleIntegerProperty(id);
        this.group = new SimpleStringProperty(group);
        this.activity = new SimpleStringProperty(activity);
        this.mandatory = new SimpleStringProperty(mandatory);
        this.start = new SimpleObjectProperty<>(start);
        this.plannedEnd = new SimpleObjectProperty<>(plannedEnd);
        this.end = new SimpleObjectProperty<>(end);
        this.responsible = new SimpleStringProperty(responsible);
        this.status = new SimpleStringProperty(status);
        this.progress = new SimpleDoubleProperty(calculateProgress(start, plannedEnd));
        this.repeatId = new SimpleIntegerProperty(repeatId);
        this.repeat = new SimpleStringProperty(repeat);
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

    public ObjectProperty<Date> startProperty() {
        return start;
    }

    public void setStart(Date start) {
        this.start.set(start);
    }

    public Date getPlannedEnd() {
        return plannedEnd.get();
    }

    public ObjectProperty<Date> plannedEndProperty() {
        return plannedEnd;
    }

    public void setPlannedEnd(Date plannedEnd) {
        this.plannedEnd.set(plannedEnd);
    }

    public Date getEnd() {
        return end.get();
    }

    public ObjectProperty<Date> endProperty() {
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

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(double l) {
        this.progress.set(l);
    }

    public int getRepeatId() {
        return repeatId.get();
    }

    public IntegerProperty repeatIdProperty() {
        return repeatId;
    }

    public void setRepeatId(int repeatId) {
        this.repeatId.set(repeatId);
    }

    public String getRepeat() {
        return repeat.get();
    }

    public StringProperty repeatProperty() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat.set(repeat);
    }


    @Override
    public String toString() {
        return getGroup() + " " + getActivity() + " " + getMandatory() + " " + getRepeat() + " " + formatDate(getStart()) + " "
                + formatDate(getPlannedEnd()) + " " + formatDate(getEnd()) + " " + getResponsible() + " " + getStatus();
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


    private double calculateProgress(Date start, Date plannedEnd) {
        if (start == null || plannedEnd == null) {
            return 0;
        }
        long totalMinutes = plannedEnd.getTime() - start.getTime();
        long currentMinutes = new Date().getTime() - start.getTime();
        double percentage = totalMinutes == 0 ? 1 : (double) currentMinutes/totalMinutes;
        if (percentage < 0 && getEnd() == null) {
            return 0;
        } else if (percentage > 1 || getEnd() != null) {
            return 1;
        } else {
            return percentage;
        }
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }


}

