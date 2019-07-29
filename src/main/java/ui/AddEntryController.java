package ui;

import com.jfoenix.controls.*;
import core.DataEntry;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import server.Connector;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class AddEntryController implements Initializable {

    @FXML
    private JFXComboBox<String> group;

    @FXML
    private JFXComboBox<String> responsible;

    @FXML
    private JFXTextArea activity;

    @FXML
    private JFXToggleButton mandatory;

    @FXML
    private JFXDatePicker start;

    @FXML
    private JFXDatePicker plannedEnd;

    @FXML
    private JFXButton addBtn;

    private ObservableList<String> groupComboList;
    private ObservableList<String> responsibleComboList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addBtn.setOnAction(event -> {
            Connector.insertData(generateEntry());
            Stage stage = (Stage) addBtn.getScene().getWindow();
            stage.close();
        });
    }

    private DataEntry generateEntry() {
        String group = this.group.getValue();
        String activity = this.activity.getText();
        String mandatory = this.mandatory.isSelected() ? "Yes" : "No";
        Date start = Date.from(this.start.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date plannedEnd = Date.from(this.plannedEnd.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        String responsible = this.responsible.getValue();
        String status = "Incomplete";

        return new DataEntry(group, activity, mandatory, start, plannedEnd, null, responsible, status);
    }

    public void setGroupComboList(ObservableList<String> groupComboList) {
        this.groupComboList = groupComboList;
        group.getItems().addAll(groupComboList);
    }

    public void setResponsibleComboList(ObservableList<String> responsibleComboList) {
        this.responsibleComboList = responsibleComboList;
        responsible.getItems().addAll(responsibleComboList);
    }
}
