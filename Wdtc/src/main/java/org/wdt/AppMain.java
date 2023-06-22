package org.wdt;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.WdtcUI.ErrorWin;
import org.wdt.WdtcUI.HomeWin;
import org.wdt.platform.Starter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class AppMain extends Application {
    private static final Logger logmaker = Logger.getLogger(AppMain.class);
    private static final Thread FoundJavaThread = AboutSetting.RunGetJavaHome();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage MainStage) {
//        FoundJavaThread.start();
        try {
            try {
                URL url = new URL("https://www.bilibili.com");
                try {
                    InputStream in = url.openStream();
                    in.close();
                    MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion());
                } catch (IOException e) {
                    MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion() + "(无网络)");
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
                FoundJavaThread.interrupt();
                logmaker.info("===== 程序已退出 =====");
                System.exit(0);
            });
        } catch (Exception e) {
            ErrorWin.setErrorWin(e);
        }
    }
}
