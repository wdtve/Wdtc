package org.wdt.wdtc.auth;

import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.IOException;

public class Accounts {
    private final AccountsType type;

    public Accounts() throws IOException {
        if (getUserType().equals("offline")) {
            type = AccountsType.OFFLINE;
        } else {
            type = AccountsType.YGGDRASIL;
        }
    }

    public String getUserUUID() throws IOException {
        return UserSetting().getString("Uuid");
    }


    public String getAccessToken() throws IOException {
        return UserSetting().getString("AccessToken");
    }

    public String getJvm() throws IOException {
        if (type == AccountsType.OFFLINE) {
            return "";
        } else {
            return "-javaagent:" + FilePath.getAuthlibInjector() + "=" + FileUrl.getLittleskinApi() + " -Dauthlibinjector.yggdrasil.prefetched=" +
                    PlatformUtils.StringToBase64(PlatformUtils.GetUrlContent(FileUrl.getLittleskinApi()));
        }
    }

    public String getUserName() throws IOException {
        return UserSetting().getString("UserName");
    }

    public JSONObject UserSetting() throws IOException {
        return JSONUtils.getJSONObject(FilePath.getUsersJson());
    }

    public String getUserType() throws IOException {
        return UserSetting().getString("Type");
    }


    public enum AccountsType {
        OFFLINE,
        YGGDRASIL
    }

}
