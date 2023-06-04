package org.wdt.auth.Yggdrasil;

import com.alibaba.fastjson2.JSONObject;
import org.wdt.FilePath;
import org.wdt.download.DownloadTask;
import org.wdt.platform.PlatformUtils;

import java.io.IOException;

public class AuthlibInjector {
    private static final String BMCL_AUTHLIB_INJECTOR = "https://bmclapi2.bangbang93.com/mirrors/authlib-injector/";
    private static final String AUTHLIB_INJECTOR = "https://authlib-injector.yushi.moe/";

    public static void DownloadauthlibInjector() throws IOException {
        if (PlatformUtils.FileExistenceAndSize(FilePath.getAuthlibInjector())) {
            String BMCL_AUTHLIB_INJECTOR = "https://bmclapi2.bangbang93.com/mirrors/authlib-injector/";
            String authlib_injector_url = JSONObject.parseObject(PlatformUtils.GetUrlContent(BMCL_AUTHLIB_INJECTOR + "/artifact/latest.json")).getString("download_url");
            DownloadTask.StartWGetDownloadTask(authlib_injector_url, FilePath.getAuthlibInjector());
        }
    }
}
