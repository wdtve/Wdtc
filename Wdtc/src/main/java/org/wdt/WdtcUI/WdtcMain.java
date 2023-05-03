package org.wdt.WdtcUI;

import com.alibaba.fastjson2.JSONObject;
import org.apache.log4j.Logger;
import org.wdt.AboutSetting;
import org.wdt.FilePath;
import org.wdt.StringUtil;
import org.wdt.WdtcDownload.DownloadTask;
import org.wdt.WdtcDownload.FileUrl;

import java.io.IOException;

public class WdtcMain {
    private static final Logger logger = Logger.getLogger(WdtcMain.class);

    public static void main(String[] args) throws IOException {
        logger.info("* Java Version:" + System.getProperty("java.version"));
        logger.info("* Java VM Version:" + System.getProperty("java.vm.name"));
        logger.info("* Java Home:" + System.getProperty("java.home"));
        StartTask();
        AboutSetting.GenerateSettingFile();
        logger.info("* 程序开始运行");
        AppMain.main(args);
    }

    public static void StartTask() throws IOException {
        if (!FilePath.getAuthlibInjector().exists()) {
            String authlib_injector_url = JSONObject.parseObject(StringUtil.GetUrlContent(FileUrl.getBmclAuthlibInjector() + "/artifact/latest.json")).getString("download_url");
            DownloadTask.StartWGetDownloadTask(authlib_injector_url, FilePath.getAuthlibInjector());
        }
        if (!FilePath.getLlbmpipeLoader().exists()) {
            DownloadTask.StartWGetDownloadTask(FileUrl.getLlbmpipeLoader(), FilePath.getLlbmpipeLoader());
        }
    }
}
