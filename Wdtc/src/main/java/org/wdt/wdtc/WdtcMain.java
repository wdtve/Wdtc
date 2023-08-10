package org.wdt.wdtc;


import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONArray;
import org.wdt.wdtc.auth.Yggdrasil.AuthlibInjector;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.launch.GetGamePath;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.Starter;
import org.wdt.wdtc.utils.*;

import java.io.IOException;

public class WdtcMain extends JavaFxUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(WdtcMain.class);

    public static void main(String[] args) throws Exception {
        CkeckJavaFX();
        logmaker.info("===== Wdtc - " + Starter.getLauncherVersion() + " =====");
        logmaker.info("* Java Version:" + System.getProperty("java.version"));
        logmaker.info("* Java VM Version:" + System.getProperty("java.vm.name"));
        logmaker.info("* Java Home:" + System.getProperty("java.home"));
        logmaker.info("* Wdtc User Path:" + FilePath.getWdtcConfig());
        logmaker.info("* Setting File:" + FilePath.getSettingFile());
        logmaker.info("* Here:" + GetGamePath.getDefaultHere());
        AboutSetting.GenerateSettingFile();
        StartTask();
        Ergodic();
        AuthlibInjector.UpdateAuthlibInjector();
        GameConfig.writeConfigJsonToAllVersion();
        ThreadUtils.StartThread(() -> JavaHomePath.main(args)).setName("Found Java");
        AppMain.main(args);
    }

    public static void StartTask() throws IOException {
        String LlbmpipeLoader = "https://maven.aliyun.com/repository/public/org/glavo/llvmpipe-loader/1.0/llvmpipe-loader-1.0.jar";
        if (PlatformUtils.FileExistenceAndSize(FilePath.getLlbmpipeLoader())) {
            DownloadTask.StartDownloadTask(LlbmpipeLoader, FilePath.getLlbmpipeLoader());
        }
    }

    public static void Ergodic() throws IOException {
        AboutSetting.Setting setting = AboutSetting.getSetting();
        JSONArray JavaList = new JSONArray(setting.getJavaPath());
        for (int i = 0; i < JavaList.size(); i++) {
            if (PlatformUtils.FileExistenceAndSize(JavaList.getString(i))) {
                logmaker.info("* " + JavaList.getString(i) + " 无效");
                JavaList.remove(i);
            }
        }
        setting.setJavaPath(JavaList.getJsonArrays());
        AboutSetting.putSettingToFile(setting);
    }
}
