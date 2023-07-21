package org.wdt.wdtc;

import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.WdtcUI.Consoler;
import org.wdt.wdtc.WdtcUI.ErrorWin;
import org.wdt.wdtc.WdtcUI.HomeWin;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.Starter;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class AppMain extends Application {
    private static final Logger logmaker = getWdtcLogger.getLogger(AppMain.class);

    public static void main(String[] args) {
        if (System.getProperty("wdtc.config.path") == null)
            System.setProperty("wdtc.config.path", System.getProperty("user.home"));
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
                    MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion());
                } catch (IOException e) {
                    MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion() + "(无网络)");
                    logmaker.error("* 当前无网络连接,下载功能无法正常使用!");
                }
            } catch (MalformedURLException e) {
                ErrorWin.setErrorWin(e);
            }
            Platform.runLater(() -> {
                MainStage.setMinWidth(Consoler.WindowsWidht);
                MainStage.setMinHeight(Consoler.WindowsHeight);
                if (AboutSetting.SettingObject().has("WindowsWidth")) {
                    MainStage.setWidth(AboutSetting.GetWindowsWidth());
                } else {
                    MainStage.setWidth(Consoler.WindowsWidht);
                }
                if (AboutSetting.SettingObject().has("WindowsHeight")) {
                    MainStage.setHeight(AboutSetting.GetWindowsHeight());
                } else {
                    MainStage.setHeight(Consoler.WindowsHeight);
                }
                MainStage.getIcons().add(new Image("ico.jpg"));
                HomeWin.setHome(MainStage);
                MainStage.show();
            });
            MainStage.setOnCloseRequest(windowEvent -> {
                try {
                    logmaker.info("======= exited ========");
                    JsonObject object = AboutSetting.SettingObject().getJsonObjects();
                    object.addProperty("WindowsWidth", MainStage.getWidth());
                    object.addProperty("WindowsHeight", MainStage.getHeight());
                    FileUtils.writeStringToFile(AboutSetting.GetSettingFile(), JSONObject.toJSONString(object), "UTF-8");
                    System.exit(0);
                } catch (IOException e) {
                    ErrorWin.setErrorWin(e);
                }
            });
        } catch (Exception e) {
            ErrorWin.setErrorWin(e);
        }
    }
}
