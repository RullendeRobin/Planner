package ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import server.Connector;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEmployeeController implements Initializable {

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXTextField textField;

    private EmployeeController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBtn.setOnAction(event -> {
            if (!textField.getText().equals("")) {
                Connector.insertEmployee(textField.getText());
                Stage stage = (Stage) addBtn.getScene().getWindow();
                stage.close();
                controller.fillListView();
            } else {
                System.out.println("Name can't be empty.");
            }
        });
    }

    public void setController(EmployeeController controller) {
        this.controller = controller;
    }
}
