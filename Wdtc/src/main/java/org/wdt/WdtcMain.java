package org.wdt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.log4j.Logger;
import org.wdt.auth.Yggdrasil.AuthlibInjector;
import org.wdt.download.DownloadTask;
import org.wdt.platform.PlatformUtils;
import org.wdt.platform.Starter;

import java.io.IOException;

public class WdtcMain {
    private static final Logger logger = Logger.getLogger(WdtcMain.class);

    public static void main(String[] args) throws Exception {
        logger.info("===== Wdtc - " + Starter.getLauncherVersion() + " =====");
        logger.info("* Java Version:" + System.getProperty("java.version"));
        logger.info("* Java VM Version:" + System.getProperty("java.vm.name"));
        logger.info("* Java Home:" + System.getProperty("java.home"));
        logger.info("* Wdtc User Path:" + FilePath.getWdtcConfig());
        logger.info("* Setting File:" + FilePath.getSettingFile());
        logger.info("* Here:" + System.getProperty("user.dir"));
        AboutSetting.GenerateSettingFile();
        StartTask();
        Ergodic();
        AuthlibInjector.UpdateAuthlibInjector();
        logger.info("* 程序开始运行");
        AppMain.main(args);
    }

    public static void StartTask() throws IOException {
        String LLBMPIPE_LOADER = "https://ghdl.feizhuqwq.cf/https://github.com/Glavo/llvmpipe-loader/releases/download/v1.0/llvmpipe-loader-1.0.jar";
        if (PlatformUtils.FileExistenceAndSize(FilePath.getLlbmpipeLoader())) {
            DownloadTask.StartWGetDownloadTask(LLBMPIPE_LOADER, FilePath.getLlbmpipeLoader());
        }
    }

    public static void Ergodic() throws IOException {
        JSONObject SettingObject = AboutSetting.SettingObject();
        JSONArray JavaList = SettingObject.getJSONArray("JavaPath");
        for (int i = 0; i < JavaList.size(); i++) {
            if (PlatformUtils.FileExistenceAndSize(JavaList.getString(i))) {
                JavaList.remove(i);
                logger.info("* " + JavaList.getString(i) + " 无效");
            }
        }
        PlatformUtils.PutKeyToFile(AboutSetting.GetSettingFile(), SettingObject, "JavaPath", JavaList);
    }
}
