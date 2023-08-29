package org.wdt.wdtc;


import org.apache.log4j.Logger;
import org.wdt.wdtc.auth.UserList;
import org.wdt.wdtc.auth.Yggdrasil.AuthlibInjector;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.game.FileManger;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.launch.GamePath;
import org.wdt.wdtc.platform.SettingManger;
import org.wdt.wdtc.platform.VMManger;
import org.wdt.wdtc.utils.JavaUtils;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.ThreadUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;

public class WdtcMain extends JavaFxUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(WdtcMain.class);

    public static void main(String[] args) throws Exception {
        CkeckJavaFX();
        logmaker.info("===== Wdtc - " + VMManger.getLauncherVersion() + " =====");
        logmaker.info("* Java Version:" + System.getProperty("java.version"));
        logmaker.info("* Java VM Version:" + System.getProperty("java.vm.name"));
        logmaker.info("* Java Home:" + System.getProperty("java.home"));
        logmaker.info("* Wdtc User Path:" + FileManger.getWdtcConfig());
        logmaker.info("* Setting File:" + FileManger.getSettingFile());
        logmaker.info("* Here:" + GamePath.getDefaultHere());
        SettingManger.GenerateSettingFile();
        StartTask();
        JavaUtils.InspectJavaPath();
        RemovePreferredVersion();
        UserList.printUserList();
        AuthlibInjector.UpdateAuthlibInjector();
        GameConfig.writeConfigJsonToAllVersion();
        ThreadUtils.StartThread(() -> JavaUtils.main(args)).setName("Found Java");
        AppMain.main(args);
    }

    public static void StartTask() throws IOException {
        String LlbmpipeLoader = "https://maven.aliyun.com/repository/public/org/glavo/llvmpipe-loader/1.0/llvmpipe-loader-1.0.jar";
        if (PlatformUtils.FileExistenceAndSize(FileManger.getLlbmpipeLoader())) {
            DownloadTask.StartDownloadTask(LlbmpipeLoader, FileManger.getLlbmpipeLoader());
        }
    }



    public static void RemovePreferredVersion() throws IOException {
        SettingManger.Setting setting = SettingManger.getSetting();
        if (setting.getPreferredVersion() != null) {
            Launcher launcher = new Launcher(setting.getPreferredVersion());
            if (PlatformUtils.FileExistenceAndSize(launcher.getVersionJson())) {
                setting.setPreferredVersion(null);
                SettingManger.putSettingToFile(setting);
            }
        }
    }
}
