package org.wdt.wdtc;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.log4j.Logger;
import org.wdt.wdtc.auth.Yggdrasil.AuthlibInjector;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.Starter;
import org.wdt.wdtc.utils.JavaHomePath;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.ThreadUtils;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.IOException;

public class WdtcMain {
    private static final Logger logmaker = getWdtcLogger.getLogger(WdtcMain.class);

    public static void main(String[] args) throws Exception {
        logmaker.info("===== Wdtc - " + Starter.getLauncherVersion() + " =====");
        logmaker.info("* Java Version:" + System.getProperty("java.version"));
        logmaker.info("* Java VM Version:" + System.getProperty("java.vm.name"));
        logmaker.info("* Java Home:" + System.getProperty("java.home"));
        logmaker.info("* Wdtc User Path:" + FilePath.getWdtcConfig());
        logmaker.info("* Setting File:" + FilePath.getSettingFile());
        logmaker.info("* Here:" + System.getProperty("user.dir"));
        AboutSetting.GenerateSettingFile();
        StartTask();
        Ergodic();
        AuthlibInjector.UpdateAuthlibInjector();
        GameConfig.writeConfigJsonToAllVersion();
        ThreadUtils.StartThread(() -> JavaHomePath.main(args)).setName("Found Java");
        logmaker.info("* 程序开始运行");
        AppMain.main(args);
    }

    public static void StartTask() throws IOException {
        String LlbmpipeLoader = "https://download.fastgit.ixmu.net/Wd-t/llvmpipe-loader/releases/download/v1.0/llvmpipe-loader-1.0.jar";
        if (PlatformUtils.FileExistenceAndSize(FilePath.getLlbmpipeLoader())) {
            DownloadTask.StartWGetDownloadTask(LlbmpipeLoader, FilePath.getLlbmpipeLoader());
        }
    }

    public static void Ergodic() throws IOException {
        JSONObject SettingObject = AboutSetting.SettingObject().getFastJSONObject();
        JSONArray JavaList = SettingObject.getJSONArray("JavaPath");
        for (int i = 0; i < JavaList.size(); i++) {
            if (PlatformUtils.FileExistenceAndSize(JavaList.getString(i))) {
                JavaList.remove(i);
                logmaker.info("* " + JavaList.getString(i) + " 无效");
            }
        }
        PlatformUtils.PutKeyToFile(AboutSetting.GetSettingFile(), SettingObject, "JavaPath", JavaList);
    }
}
