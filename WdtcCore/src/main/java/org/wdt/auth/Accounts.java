package org.wdt.auth;

import org.wdt.download.FileUrl;
import org.wdt.game.FilePath;
import org.wdt.platform.PlatformUtils;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.Utils;

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
        return Utils.getJSONObject(FilePath.getUsersJson());
    }

    public String getUserType() throws IOException {
        return UserSetting().getString("Type");
    }


    public enum AccountsType {
        OFFLINE,
        YGGDRASIL
    }

}
