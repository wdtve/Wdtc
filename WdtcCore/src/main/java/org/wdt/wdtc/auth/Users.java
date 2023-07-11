package org.wdt.wdtc.auth;

import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("UserName")
    public String UserName;
    @SerializedName("AccessToken")
    public String AccessToken;
    @SerializedName("ClientToken")
    public String ClientToken;
    @SerializedName("Type")
    public String Type;
    @SerializedName("Uuid")
    public String Uuid;

    public void setClientToken(String clientToken) {
        ClientToken = clientToken;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }

    @Override
    public String toString() {
        return "Users{" +
                "UserName='" + UserName + '\'' +
                ", AccessToken='" + AccessToken + '\'' +
                ", Type='" + Type + '\'' +
                ", Uuid='" + Uuid + '\'' +
                '}';
    }
}
