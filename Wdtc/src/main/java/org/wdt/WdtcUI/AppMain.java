package org.wdt.WdtcUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class AppMain extends Application {
    private static final Logger logmaker = Logger.getLogger(AppMain.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage MainStage) {
        try {
            try {
                URL url = new URL("https://www.bilibili.com");
                try {
                    InputStream in = url.openStream();
                    in.close();
                    MainStage.setTitle("Wdtc - Demo");
                } catch (IOException e) {
                    MainStage.setTitle("Wdtc - Demo (无网络)");
                    logmaker.error("* 当前无网络连接,下载功能无法正常使用!");
                }
            } catch (MalformedURLException e) {
                ErrorWin.setErrorWin(e);
            }
            Platform.runLater(() -> {
                MainStage.setWidth(616.0);
                MainStage.setHeight(489.0);
                MainStage.getIcons().add(new Image("ico.jpg"));
                MainStage.setResizable(false);
                HomeWin.setHome(MainStage);
                MainStage.show();
            });
            MainStage.setOnCloseRequest(windowEvent -> {
                logmaker.info("===== 程序已退出 =====");
                System.exit(0);
            });
        } catch (Exception e) {
            ErrorWin.setErrorWin(e);
        }
    }
}
