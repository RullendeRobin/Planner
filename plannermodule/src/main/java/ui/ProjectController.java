package ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import server.Connector;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectController extends Application implements Initializable {

    @FXML
    private JFXListView<String> listView;

    @FXML
    private JFXButton newBtn;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private JFXButton openBtn;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ManageProjects.fxml"));
        primaryStage.setTitle("Project Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("zephyr_logo16x16.png")),
                new Image(getClass().getResourceAsStream("zephyr_logo32x32.png")),
                new Image(getClass().getResourceAsStream("zephyr_logo64x64.png")));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

    }

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", "ui.LoadingController");
        Application.launch(args);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                setFont(Font.font(16));
                if (isEmpty()) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(name);
                    setGraphic(new ImageView(new Image(getClass().getResourceAsStream("windmill_icon.png"))));
                }
            }
        });

        fillListView();
        listView.getSelectionModel().select(0);

        openBtn.setOnAction(event -> openProject());

        newBtn.setOnAction(event -> showAddProject());

        listView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                openProject();
            }
        });
        /*
        deleteBtn.setOnAction(event -> {
            Connector.deleteEmployee(listView.getSelectionModel().getSelectedItem());
            fillListView();
        });
         */
    }

    private void showOverview(String project) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlannerOverview.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stage = new Stage();
            OverviewController controller = fxmlLoader.getController();
            controller.setProject(project);
            stage.setTitle("Planner");
            stage.setScene(new Scene(root1));
            stage.getIcons().addAll(
                    new Image(getClass().getResourceAsStream("zephyr_logo16x16.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo32x32.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo64x64.png")));
            stage.show();
            stage.setOnCloseRequest(event -> controller.close());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddProject() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddProject.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stage = new Stage();
            AddProjectController controller = fxmlLoader.getController();
            controller.setController(this);
            stage.setTitle("Create project");
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

    @FXML
    private void handleOnKeyPressed(KeyEvent event) {

        if (event.getCode().equals(KeyCode.ENTER)) {
            openBtn.fire();
        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
            Platform.exit();
            System.exit(0);
        }
    }

    void fillListView() {
        ObservableList<String> employees = Connector.getProjects();
        listView.setItems(employees);
    }

    private void openProject() {
        String project = listView.getSelectionModel().getSelectedItem();
        if (project != null) {
            Stage stage = (Stage) openBtn.getScene().getWindow();
            stage.close();
            Connector.setProject(project);
            showOverview(project);
        } else {
            System.out.println("Please select a project");
        }
    }
}
