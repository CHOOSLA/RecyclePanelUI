package kr.ac.sch.oopsla;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;


public class PrimaryController implements Initializable {
    @FXML
    public Pane cameraPane;

    @FXML
    public Button backButton;

    @FXML
    public Button titleButton;

    @FXML
    public Label timeLabel;


    Timeline timer;
    AtomicInteger time;
    ConnectServer server = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final SwingNode swingNode = new SwingNode();
        createGui(swingNode);

        try {
            server = new ConnectServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.valueProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue!=null){
                titleButton.setText(newValue);
            }
        });

        server.setOnSucceeded(s ->{
        });

        time = new AtomicInteger(30);
        timer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                if (time.getAcquire() == 0) {
                                    //App.webcam.close();
                                    try {
                                        timer.stop();
                                        App.setRoot("start");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //App.setRoot("start");
                                }


                                /*
                                byte[] bytes = WebcamUtils.getImageBytes(App.webcam, "jpeg");

                                System.out.println("Bytes length: " + bytes.length);

                                 */


                                /*
                                // get image
                                BufferedImage image = App.webcam.getImage();


                                // save image to PNG file
                                try {
                                    ImageIO.write(image, "PNG", new File("test.png"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                 */
                                WebcamUtils.capture(App.webcam, "test", ImageUtils.FORMAT_JPG);
                                server.reset();
                                server.start();

                                /*
                                server.cancel();
                                server.setImageCaptured();
                                server.restart();

                                 */
                                timeLabel.setText(time.getAndDecrement() + "");
                            }
                        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();




        cameraPane.getChildren().add(swingNode);
    }

    public void backTostart() throws IOException {
        timer.stop();
        server.cancel();
        // App.webcam.close();
        App.setRoot("start");

    }


    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGui(swingNode);
            }
        });
    }


    private void createGui(final SwingNode swingNode) {

        //여기서 계속 새로운 패널이 백그라운드로 돌아가면서 렉 유발 + 강제 뒤로가기
        swingNode.setContent(App.panel);
    }


}
