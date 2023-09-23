package org.wdt.wdtc.auth.yggdrasil;

import org.wdt.utils.gson.JSONObject;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.IOException;
import java.util.jar.JarInputStream;

public class AuthlibInjector {
    private static final String BMCL_AUTHLIB_INJECTOR_LATEST_JSON = "https://bmclapi2.bangbang93.com/mirrors/authlib-injector/artifact/latest.json";
    private static final String AUTHLIB_INJECTOR = "https://authlib-injector.yushi.moe/";

    public static void DownloadauthlibInjector() throws IOException {
        String authlib_injector_url = GetBmclAuthlibInjectorLatestJsonObject().getString("download_url");
        DownloadTask.StartDownloadTask(authlib_injector_url, FileManger.getAuthlibInjector());
    }

    public static void UpdateAuthlibInjector() throws IOException {
        if (!PlatformUtils.FileExistenceAndSize(FileManger.getAuthlibInjector())) {
            String LatestVersionNumber = GetBmclAuthlibInjectorLatestJsonObject().getString("version");
            String PresentVersionNumber = new JarInputStream(FileUtils.newInputStream(FileManger.getAuthlibInjector()))
                    .getManifest().getMainAttributes().getValue("Implementation-Version");
            if (!PresentVersionNumber.equals(LatestVersionNumber)) {
                DownloadauthlibInjector();
            }
        } else {
            DownloadauthlibInjector();
        }
    }

    private static JSONObject GetBmclAuthlibInjectorLatestJsonObject() throws IOException {
        return JSONObject.parseObject(PlatformUtils.UrltoString(BMCL_AUTHLIB_INJECTOR_LATEST_JSON));
    }
}
