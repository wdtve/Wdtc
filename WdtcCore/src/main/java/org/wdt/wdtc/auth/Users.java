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
    @SerializedName("Type")
    public String Type;
    @SerializedName("Uuid")
    public String Uuid;


    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getUserName() {
        return UserName;
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

    public String getAccessToken() {
        return AccessToken;
    }

    public Accounts.AccountsType getType() {
        return Accounts.AccountsType.valueOf(Type);
    }

    public void setType(Accounts.AccountsType type) {
        Type = type.toString();
    }

    public String getUuid() {
        return Uuid;
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
