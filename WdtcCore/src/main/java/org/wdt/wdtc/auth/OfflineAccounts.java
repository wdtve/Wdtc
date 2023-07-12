package org.wdt.wdtc.auth;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

import java.io.IOException;
import java.util.UUID;

public class OfflineAccounts {
    private static final Logger logmaker = getWdtcLogger.getLogger(OfflineAccounts.class);
    private final String username;
    private final String UserUuid = String.valueOf(UUID.randomUUID()).replaceAll("-", "");

    public OfflineAccounts(String username) {
        this.username = username;
    }


    public void WriteUserJson() throws IOException {
        Users users = new Users();
        users.setUserName(username);
        users.setType("offline");
        users.setAccessToken("${auth_access_token}");
        users.setUuid(UserUuid);
        FileUtils.writeStringToFile(FilePath.getUsersJson(), JSONObject.toJSONString(users), "UTF-8");
        logmaker.info(users);
    }
}
