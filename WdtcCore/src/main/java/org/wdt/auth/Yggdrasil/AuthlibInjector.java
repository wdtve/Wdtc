package org.wdt.auth.Yggdrasil;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.wdt.FilePath;
import org.wdt.download.DownloadTask;
import org.wdt.platform.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class AuthlibInjector {
    private static final String BMCL_AUTHLIB_INJECTOR = "https://bmclapi2.bangbang93.com/mirrors/authlib-injector/";
    private static final String AUTHLIB_INJECTOR = "https://authlib-injector.yushi.moe/";

    public static void DownloadauthlibInjector() throws IOException {
        if (PlatformUtils.FileExistenceAndSize(FilePath.getAuthlibInjector())) {
            String authlib_injector_url = JSONObject.parseObject(PlatformUtils.GetUrlContent(BMCL_AUTHLIB_INJECTOR + "/artifact/latest.json")).getString("download_url");
            DownloadTask.StartWGetDownloadTask(authlib_injector_url, FilePath.getAuthlibInjector());
        }
    }

    public static void UpdateAuthlibInjector() throws IOException {
        DownloadauthlibInjector();
        File FileList = FilePath.getWdtcConfig();
        for (File file : Objects.requireNonNull(FileList.listFiles())) {
            if (Pattern.compile("authlib-injector").matcher(file.getName()).find()) {
                if (!file.getName().equals(FilePath.getAuthlibInjector().getName())) {
                    FileUtils.delete(file);
                }

            }
        }
    }
}
