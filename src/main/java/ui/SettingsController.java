package ui;

import com.jfoenix.controls.JFXButton;
import core.Properties;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    private Properties properties;
    private OverviewController controller;

    @FXML
    TextField daysField;

    @FXML
    JFXButton saveBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveBtn.setOnAction(event -> {
            String days = daysField.getText();
            properties.saveProperties(days);
            controller.setSummaryDays(days);
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();
        });

    }

    protected void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setController(OverviewController controller) {
        this.controller = controller;
    }

    protected void setDaysField(String text) {
        daysField.setText(text);
    }
}
