package kr.ac.sch.oopsla;

import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ConnectServer extends Service<String> {
    static final String host = "http://192.168.0.19/APIs/3rdknock/";

    final String boundary = "*****";
    final String crlf = "\r\n";
    final String twoHyphens = "--";


    URL url = null;
    HttpURLConnection conn = null;
    BufferedWriter bw;
    BufferedReader br;

    boolean imageCaptured = false;
    byte[] imageBytes = null;

    public ConnectServer() throws IOException {
        /*
        //옛날 방법 header를 직접만듦
        url = new URL(host);
        conn = (HttpURLConnection) url.openConnection();

        //연결 설정값들
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Content-Type", "multipart/form-data ;boundary=" + this.boundary);

         */

        //Service 클래스 설정
        setOnSucceeded(s -> {
            System.out.println("Success To send Image");
        });

    }

    public void setImageCaptured() {
        imageCaptured = !imageCaptured;
    }

    public void setImageBytes(byte[] bytes) {
        imageBytes = bytes;
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            protected String call() throws IOException, UnirestException, InterruptedException, ExecutionException {
                String result  = "";

                        byte[] tmpImage = imageBytes;
                        System.out.println("start");

                        /*
                        //이미지 리사이징
                        //서버에서 3000x4000 밖에 안받음
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(tmpImage));
                        Image scaled = img.getScaledInstance(3000, 4000, Image.SCALE_SMOOTH);
                        BufferedImage imageBuff = new BufferedImage(3000, 4000, BufferedImage.TYPE_INT_RGB);
                        imageBuff.getGraphics().drawImage(scaled, 0, 0, new Color(0, 0, 0), null);
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                        //ImageIO.write(imageBuff, "jpg", buffer);
                        //tmpImage = buffer.toByteArray();

                        ImageIO.write(imageBuff, "jpg", new File("Resized_test.jpg"));
                        System.out.println("Resized Bytes Length : " + tmpImage.length + "\n");

                         */


                        Unirest.setTimeouts(0, 0);
                        Future<HttpResponse<JsonNode>> future = Unirest.post(host)
                                .field("file", new File("test.jpg"))
                                .asJsonAsync(new Callback<JsonNode>() {

                                    public void failed(UnirestException e) {
                                        // Do something if the request failed
                                        System.out.println("Response Failed!");
                                    }

                                    public void completed(HttpResponse<JsonNode> response) {
                                        // Do something if the request is successful
                                        System.out.println("Response Completed!");
                                    }

                                    public void cancelled() {
                                        // Do something if the request is cancelled
                                        System.out.println("Response Cancelled!");
                                    }
                                });
                        result = future.get().getBody().toString();


                        imageBytes = null;
                        setImageCaptured();

                    /*
                    //옛날 방법 body를 직접 만듦
                    DataOutputStream request = new DataOutputStream(conn.getOutputStream());
                    request.writeBytes(twoHyphens + boundary + crlf);
                    request.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"test.jpg\"" + crlf);
                    request.writeBytes("Content-Type: image/jpg" + crlf);
                    request.writeBytes(crlf);
                    request.write(tmpImage);

                    request.writeBytes(crlf);
                    request.writeBytes(twoHyphens + boundary +
                            twoHyphens + crlf);
                    request.flush();
                    request.close();
                     result.append(conn.getResponseCode()).append("\n");

                    InputStream responseStream = new BufferedInputStream(conn.getInputStream());
                    BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                    String line = "";
                    while ((line = responseStreamReader.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                    responseStreamReader.close();

                    System.out.println(result.toString());
                     */




                return result;
            }
        };
    }
}
