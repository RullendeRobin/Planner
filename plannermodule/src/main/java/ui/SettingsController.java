package ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import core.Properties;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    private Properties properties;
    private OverviewController controller;

    @FXML
    JFXCheckBox sortGroupsBox;

    @FXML
    TextField daysField;

    @FXML
    JFXButton saveBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveBtn.setOnAction(event -> {
            String[] props = new String[2];
            props[0] = daysField.getText();
            props[1] = String.valueOf(sortGroupsBox.isSelected());
            properties.saveProperties(props);
            controller.setSummaryDays(props[0]);
            controller.setSortGroupsAlphabetically(sortGroupsBox.isSelected());
            controller.buildBottomMenu();
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

    private boolean getBooleanValue(String text) {
        return text.equals("true");
    }


}
