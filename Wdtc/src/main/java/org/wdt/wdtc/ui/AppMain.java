package org.wdt.wdtc.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.manger.SettingManger;
import org.wdt.wdtc.core.manger.VMManger;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.URLUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.ui.image.Consoler;
import org.wdt.wdtc.ui.image.ErrorWindow;
import org.wdt.wdtc.ui.image.HomeWindow;
import org.wdt.wdtc.ui.image.WindwosSizeManger;

import java.io.IOException;

public class AppMain extends Application {
    private static final Logger logmaker = WdtcLogger.getLogger(AppMain.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage MainStage) {
        try {
            WindwosSizeManger size = new WindwosSizeManger(MainStage);
            if (URLUtils.isOnline()) {
                MainStage.setTitle(Consoler.getWindowsTitle());
            } else {
                MainStage.setTitle(Consoler.getWindowsTitle("无网络"));
                logmaker.warn("当前无网络连接,下载功能无法正常使用!");
            }
            MainStage.setMinWidth(WindwosSizeManger.WindowsWidht);
            MainStage.setMinHeight(WindwosSizeManger.WindowsHeight);
            size.setWindwosSize();
            MainStage.getIcons().add(new Image("ico.jpg"));
            MainStage.setResizable(VMManger.isDebug());
            HomeWindow win = new HomeWindow();
            win.setHome(MainStage);
            MainStage.show();
            logmaker.info("Window Show");
            MainStage.setOnCloseRequest(windowEvent -> {
                logmaker.info(size);
                try {
                    SettingManger.Setting setting = SettingManger.getSetting();
                    setting.setWindowsWidth(MainStage.getWidth()).setWindowsHeight(MainStage.getHeight());
                    FileUtils.touch(DownloadUtils.StopProcess);
                    SettingManger.putSettingToFile(setting);
                } catch (IOException e) {
                    logmaker.error(WdtcLogger.getErrorMessage(e));
                }
                Platform.exit();
                logmaker.info("======= Wdtc Stop ========");
            });
        } catch (Exception e) {
            ErrorWindow.setErrorWin(e);
        }
    }
}
