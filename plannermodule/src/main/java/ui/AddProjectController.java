package ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import server.Connector;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProjectController implements Initializable {

    @FXML
    private JFXButton createBtn;

    @FXML
    private JFXTextField textField;

    private ProjectController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        createBtn.setOnAction(event -> {
            if (!textField.getText().equals("")) {
                Connector.insertProject(textField.getText());
                Stage stage = (Stage) createBtn.getScene().getWindow();
                stage.close();
                controller.fillListView();
            } else {
                System.out.println("Name can't be empty.");
            }
        });
    }

    public void setController(ProjectController controller) {
        this.controller = controller;
    }
}
