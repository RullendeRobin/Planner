package ui;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadingController extends Preloader {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Loading.fxml"));
        this.stage = stage;
        stage.setTitle("Planner");
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("zephyr_logo16x16.png")),
                new Image(getClass().getResourceAsStream("zephyr_logo32x32.png")),
                new Image(getClass().getResourceAsStream("zephyr_logo64x64.png")));
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stage.hide();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
