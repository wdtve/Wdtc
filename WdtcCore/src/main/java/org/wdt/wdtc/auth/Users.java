package org.wdt.wdtc.auth;

import com.google.gson.annotations.SerializedName;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.File;
import java.io.IOException;

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

    public static boolean SetUserJson() {
        try {
            File UserJson = FilePath.getUsersJson();
            return !PlatformUtils.FileExistenceAndSize(UserJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
