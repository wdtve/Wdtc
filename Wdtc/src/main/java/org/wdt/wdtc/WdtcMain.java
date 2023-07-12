package org.wdt.wdtc;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.log4j.Logger;
import org.wdt.wdtc.WdtcUI.ErrorWin;
import org.wdt.wdtc.auth.Yggdrasil.AuthlibInjector;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.PlatformUtils;
import org.wdt.wdtc.platform.Starter;
import org.wdt.wdtc.platform.java.JavaHomePath;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

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
        new Thread(() -> {
            try {
                JavaHomePath.main(args);
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        }).start();
        logmaker.info("* 程序开始运行");
        AppMain.main(args);
    }

    public static void StartTask() throws IOException {
        String LLBMPIPE_LOADER = "https://ghdl.feizhuqwq.cf/https://github.com/Glavo/llvmpipe-loader/releases/download/v1.0/llvmpipe-loader-1.0.jar";
        if (PlatformUtils.FileExistenceAndSize(FilePath.getLlbmpipeLoader())) {
            DownloadTask.StartWGetDownloadTask(LLBMPIPE_LOADER, FilePath.getLlbmpipeLoader());
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
