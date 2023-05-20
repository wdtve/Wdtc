package org.wdt.WdtcUI;

import com.alibaba.fastjson2.JSONArray;
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
        logger.info("===== Wdtc - Demo =====");
        logger.info("* Java Version:" + System.getProperty("java.version"));
        logger.info("* Java VM Version:" + System.getProperty("java.vm.name"));
        logger.info("* Java Home:" + System.getProperty("java.home"));
        logger.info("* Wdtc User Path:" + FilePath.getWdtcConfig());
        logger.info("* Setting File:" + FilePath.getSettingFile());
        logger.info("* Here:" + System.getProperty("user.dir"));
        StartTask();
        Ergodic();
        AboutSetting.GenerateSettingFile();
        logger.info("* 程序开始运行");
        AppMain.main(args);
    }

    public static void StartTask() throws IOException {
        if (StringUtil.FileExistenceAndSize(FilePath.getAuthlibInjector())) {
            String authlib_injector_url = JSONObject.parseObject(StringUtil.GetUrlContent(FileUrl.getBmclAuthlibInjector() + "/artifact/latest.json")).getString("download_url");
            DownloadTask.StartWGetDownloadTask(authlib_injector_url, FilePath.getAuthlibInjector());
        }
        if (StringUtil.FileExistenceAndSize(FilePath.getLlbmpipeLoader())) {
            DownloadTask.StartWGetDownloadTask(FileUrl.getLlbmpipeLoader(), FilePath.getLlbmpipeLoader());
        }
    }

    public static void Ergodic() throws IOException {
        JSONObject SettingObject = AboutSetting.SettingObject();
        JSONArray JavaList = SettingObject.getJSONArray("JavaPath");
        for (int i = 0; i < JavaList.size(); i++) {
            if (StringUtil.FileExistenceAndSize(JavaList.getString(i))) {
                JavaList.remove(i);
                logger.info("* " + JavaList.getString(i) + " 无效");
            }
        }
        SettingObject.put("JavaPath", JavaList);
        StringUtil.PutJSONObject(AboutSetting.GetSettingFile(), SettingObject);
    }
}
