package org.wdt.auth;

public class Users {
    public String UserName;
    public String AccessToken;
    public String ClientToken;
    public String Uuid;

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public void setClientToken(String clientToken) {
        ClientToken = clientToken;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }
}
