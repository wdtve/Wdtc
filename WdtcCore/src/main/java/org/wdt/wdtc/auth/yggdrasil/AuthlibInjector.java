package org.wdt.wdtc.auth.yggdrasil;

import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.utils.UrlUtils;
import org.wdt.wdtc.utils.gson.JSONObject;

import java.io.IOException;
import java.util.jar.JarInputStream;

public class AuthlibInjector {
    private static final String BMCL_AUTHLIB_INJECTOR_LATEST_JSON = "https://bmclapi2.bangbang93.com/mirrors/authlib-injector/artifact/latest.json";
    private static final String AUTHLIB_INJECTOR = "https://authlib-injector.yushi.moe/";

    public static void downloadauthlibInjector() throws IOException {
        DownloadTask.StartDownloadTask(getBmclAuthlibInjectorLatestJsonObject().getString("download_url"), FileManger.getAuthlibInjector());
    }

    public static void updateAuthlibInjector() throws IOException {
        if (FileUtils.isFileExists(FileManger.getAuthlibInjector())) {
            String LatestVersionNumber = getBmclAuthlibInjectorLatestJsonObject().getString("version");
            String PresentVersionNumber = new JarInputStream(FileUtils.newInputStream(FileManger.getAuthlibInjector()))
                    .getManifest().getMainAttributes().getValue("Implementation-Version");
            if (!PresentVersionNumber.equals(LatestVersionNumber)) {
                downloadauthlibInjector();
            }
        } else {
            downloadauthlibInjector();
        }
    }

    private static JSONObject getBmclAuthlibInjectorLatestJsonObject() throws IOException {
        return JSONObject.parseObject(UrlUtils.getUrlToString(BMCL_AUTHLIB_INJECTOR_LATEST_JSON));
    }
}
