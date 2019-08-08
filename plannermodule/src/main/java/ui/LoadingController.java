package ui;

import core.DataEntry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoadingController extends Preloader {

    private Stage stage;
    private java.util.TimerTask queryTask;
    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

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

        queryTask = new java.util.TimerTask() {
            @Override
            public void run() {
                Platform.runLater(stage::close);
                executorService.shutdown();
            }
        };
        // executorService runs queryTask every 0.5 seconds


    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            executorService.schedule(queryTask, 1200, TimeUnit.MILLISECONDS);

        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
