package ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import server.Connector;

import java.net.URL;
import java.util.ResourceBundle;

public class GroupController implements Initializable {

    @FXML
    private JFXListView<String> listView;

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private JFXButton closeBtn;

    private OverviewController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                //setFont(Font.font(16));
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(name);
                    ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("group_icon16x16.png")));
                    setGraphic(iv);
                }
            }
        });
        fillListView();

        addBtn.setOnAction(event -> {
            showAddGroup();
        });

        deleteBtn.setOnAction(event -> {
            Connector.deleteGroup(listView.getSelectionModel().getSelectedItem());
            fillListView();
        });


        closeBtn.setOnAction(event -> {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            controller.buildBottomMenu();
            stage.close();
        });
    }

    private void showAddGroup() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddGroup.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stage = new Stage();
            AddGroupController controller = fxmlLoader.getController();
            controller.setController(this);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add group");
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
        ObservableList<String> groups = Connector.getGroups(true);
        listView.setItems(groups);
    }

    public void setController(OverviewController controller) {
        this.controller = controller;
    }
}
