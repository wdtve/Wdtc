package org.wdt.wdtc.auth;

import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.UUID;

public class OfflineAccounts {
    private static final Logger logmaker = WdtcLogger.getLogger(OfflineAccounts.class);
    private final String username;
    private final String UserUuid = String.valueOf(UUID.randomUUID()).replaceAll("-", "");

    public OfflineAccounts(String username) {
        this.username = username;
    }


    public void WriteUserJson() throws IOException {
        Users users = new Users();
        users.setUserName(username);
        users.setType(Accounts.AccountsType.Offline);
        users.setAccessToken("${auth_access_token}");
        users.setUuid(UserUuid);
        JSONUtils.ObjectToJsonFile(FilePath.getUsersJson(), users);
        logmaker.info(users);
    }
}
