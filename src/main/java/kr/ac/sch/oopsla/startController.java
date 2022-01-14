package kr.ac.sch.oopsla;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class startController implements Initializable {

    @FXML
    public Button startBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
