package org.wdt.WdtcLauncher;

import java.util.UUID;

public class OfflineAccounts {
    private final String username;
    private final String UserUuid = String.valueOf(UUID.randomUUID()).replaceAll("-", "");
    private String accessToken = "${auth_access_token}";

    public OfflineAccounts(String username) {
        this.username = username;
    }

    public String GetUserUuid() {
        return UserUuid;
    }

    public String GetAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String GetUserName() {
        return username;
    }
}
