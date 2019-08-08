package ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import server.Connector;

import java.net.URL;
import java.util.ResourceBundle;

public class AddGroupController implements Initializable {

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXTextField textField;

    private GroupController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addBtn.setOnAction(event -> {
            if (!textField.getText().equals("")) {
                Connector.insertGroup(textField.getText());
                Stage stage = (Stage) addBtn.getScene().getWindow();
                stage.close();
                controller.fillListView();
            } else {
                System.out.println("Name can't be empty.");
            }
        });
    }

    public void setController(GroupController controller) {
        this.controller = controller;
    }
}
