package ui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class AboutController implements Initializable {

    @FXML
    Label versionLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String version = "Version: ";
        try {
            Manifest manifest = new Manifest(this.getClass().getResourceAsStream("/META-INF/MANIFEST.MF"));
            Attributes at = manifest.getMainAttributes();
            String value = at.getValue("Implementation-Version");
            if (value != null) {
                version += value;
            } else {
                version += "unknown";
            }

        } catch (Exception e) {
            version += "unknown";
            e.printStackTrace();
        }
        versionLabel.setText(version);

    }
}
