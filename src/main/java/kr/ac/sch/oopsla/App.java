package kr.ac.sch.oopsla;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Webcam webcam;
    public static Dimension frameSize;
    public static WebcamPanel panel;
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("start"), 800, 800);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent evt) {
                //창이 종료될  때, 카메라도 꺼주어야함
                webcam.close();
            }
        });
    }

    @Override
    public void init(){
        
    }

    public App(){
        frameSize = WebcamResolution.VGA.getSize(); //웹캠 사이즈 받아오기;
        webcam = Webcam.getDefault();
        webcam.setCustomViewSizes(new Dimension[] { frameSize });
        webcam.setViewSize(frameSize);
        webcam.open();
        panel = new WebcamPanel(webcam);
    }



    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}