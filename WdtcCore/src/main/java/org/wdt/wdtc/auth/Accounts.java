package org.wdt.wdtc.auth;

import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.IOException;

public class Accounts {
    private final AccountsType type;

    public Accounts() {
        if (getUsers().getType() == AccountsType.Offline) {
            type = AccountsType.Offline;
        } else {
            type = AccountsType.Yggdrasil;
        }
    }

    public boolean AccountsIsOffline() {
        return type != AccountsType.Yggdrasil;
    }

    public String getJvm() throws IOException {
        if (AccountsIsOffline()) {
            return "";
        } else {
            return "-javaagent:" + FilePath.getAuthlibInjector() + "=" + FileUrl.getLittleskinApi() + " -Dauthlibinjector.yggdrasil.prefetched=" +
                    PlatformUtils.StringToBase64(PlatformUtils.GetUrlContent(FileUrl.getLittleskinApi()));
        }
    }

    public Users getUsers() {
        return JSONUtils.JsonFileToClass(FilePath.getUsersJson(), Users.class);
    }


    public enum AccountsType {
        Offline,
        Yggdrasil
    }

}
