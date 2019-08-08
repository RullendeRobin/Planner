package ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import core.DataEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.Connector;

import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ResourceBundle;


public class RepeatController implements Initializable {

    @FXML
    private TextField timeframeField;

    @FXML
    private TextField repetitionsField;

    @FXML
    private JFXComboBox<String> timeframeBox;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton cancelBtn;

    private DataEntry entry;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> timeframeComboList = FXCollections.observableArrayList();
        timeframeComboList.add("Week(s)");
        timeframeComboList.add("Month(s)");
        timeframeComboList.add("Year(s)");

        timeframeBox.getItems().addAll(timeframeComboList);

        saveBtn.setOnAction(event -> {
            int timeframe = Integer.parseInt(timeframeField.getText());
            String tempUnit = timeframeBox.getValue();
            int repetitions = Integer.parseInt(repetitionsField.getText());
            TemporalUnit unit;
            switch (tempUnit) {
                case "Week(s)":
                    unit = ChronoUnit.WEEKS;
                    break;
                case "Month(s)":
                    unit = ChronoUnit.MONTHS;
                    break;
                case "Year(s)":
                    unit = ChronoUnit.YEARS;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + tempUnit);
            }
            Connector.insertBatchData(entry, timeframe, unit, repetitions);
            Connector.updateData("Yes", entry, Connector.repeatUpdate);
            close();
        });

        cancelBtn.setOnAction(event -> close());
    }

    protected void setEntry(DataEntry entry) {
        this.entry = entry;
    }

    private void close() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
}
