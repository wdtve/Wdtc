package org.WdtcUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class AppMain extends Application {
    private static final Logger logmaker = Logger.getLogger(AppMain.class);
    public static Stage MainStage = new Stage();

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        AppMain.MainStage = stage;
        Pane main_pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/stage.fxml")));
        MainStage.setWidth(615);
        MainStage.setHeight(440);
        MainStage.getIcons().add(new Image("ico.jpg"));
        try {
            URL url = new URL("https://www.bilibili.com");
            try {
                InputStream in = url.openStream();
                in.close();
                MainStage.setTitle("Wdtc");
            } catch (IOException e) {
                MainStage.setTitle("Wdtc (无网络)");
                logmaker.error("* 当前无网络连接,下载功能无法正常使用!");
            }
        } catch (MalformedURLException e) {
            ErrorWin.setErrorWin(e);
        }
        MainStage.setResizable(false);
        Scene scene = new Scene(main_pane);
        MainStage.setScene(scene);
        logmaker.info("* 程序开始运行");
        MainStage.show();
        MainStage.setOnCloseRequest(windowEvent -> {
            logmaker.info("* 程序已退出");
            System.exit(0);
        });
    }
}