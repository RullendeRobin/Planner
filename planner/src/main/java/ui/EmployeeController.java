package ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import server.Connector;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private JFXListView<String> listView;

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private JFXButton closeBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillListView();

        addBtn.setOnAction(event -> showAddEmployee());

        deleteBtn.setOnAction(event -> {
            Connector.deleteEmployee(listView.getSelectionModel().getSelectedItem());
            fillListView();
        });

        closeBtn.setOnAction(event -> {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        });
    }

    private void showAddEmployee() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddEmployee.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            AddEmployeeController controller = fxmlLoader.getController();
            controller.setController(this);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add employee");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().addAll(
                    new Image(getClass().getResourceAsStream("zephyr_logo16x16.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo32x32.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo64x64.png")));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void fillListView() {
        ObservableList<String> employees = Connector.getEmployees();
        listView.setItems(employees);
    }
}
