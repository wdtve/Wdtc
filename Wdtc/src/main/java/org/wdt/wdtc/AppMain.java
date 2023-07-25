package org.wdt.wdtc;

import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModList;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.Starter;
import org.wdt.wdtc.ui.ErrorWin;
import org.wdt.wdtc.ui.HomeWindow;
import org.wdt.wdtc.ui.WindwosSize;
import org.wdt.wdtc.utils.PlatformUtils;
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
            WindwosSize size = new WindwosSize(MainStage);
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
                MainStage.setMinWidth(WindwosSize.WindowsWidht);
                MainStage.setMinHeight(WindwosSize.WindowsHeight);
                size.SettingSize();
                MainStage.getIcons().add(new Image("ico.jpg"));
                HomeWindow win;
                if (AboutSetting.SettingObject().has("PreferredVersion")) {
                    win = new HomeWindow(ModList.getModTask(new Launcher(AboutSetting.getPreferredVersion())));
                } else {
                    win = new HomeWindow(null);
                }
                win.setHome(MainStage);
                MainStage.show();
            });
            MainStage.setOnCloseRequest(windowEvent -> {
                logmaker.info(size);
                JsonObject object = AboutSetting.SettingObject().getJsonObjects();
                object.addProperty("WindowsWidth", MainStage.getWidth());
                object.addProperty("WindowsHeight", MainStage.getHeight());
                PlatformUtils.PutJSONObject(AboutSetting.GetSettingFile(), object);
                logmaker.info("======= exited ========");
                System.exit(0);
            });
        } catch (Exception e) {
            ErrorWin.setErrorWin(e);
        }
    }
}
