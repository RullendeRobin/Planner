package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class Controller {
    @FXML private Label label;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        label.setText("Sign in button pressed");
    }

}