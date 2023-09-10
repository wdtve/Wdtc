package org.wdt.wdtc.auth;

import com.google.gson.annotations.SerializedName;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class User {

    @SerializedName("UserName")
    private String UserName;
    @SerializedName("AccessToken")
    private String AccessToken;
    @SerializedName("Type")
    private Accounts.AccountsType Type;
    @SerializedName("Uuid")
    private String Uuid;
    @SerializedName("MetaAPI")
    private String API;
    @SerializedName("MetaAPIBase64")
    private String APIBase64;
    @SerializedName("HeadPhotoPath")
    private String HeadFile;

    public static void setUserToJson(User user) {
        JSONUtils.ObjectToJsonFile(FileManger.getUsersJson(), user);
    }

    public static User getUsers() {
        try {
            return JSONUtils.JsonFileToClass(FileManger.getUsersJson(), User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAPIBase64() {
        return APIBase64;
    }

    public void setAPIBase64(String APIBase64) {
        this.APIBase64 = APIBase64;
    }

    public String getAPI() {
        return API;
    }

    public void setAPI(String API) {
        this.API = API;
    }

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
            File UserJson = FileManger.getUsersJson();
            return !PlatformUtils.FileExistenceAndSize(UserJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getHeadFile() {
        return new File(HeadFile);
    }

    public void setHeadFile(File headFile) throws IOException {
        HeadFile = headFile.getCanonicalPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(UserName, user.UserName);
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public Accounts.AccountsType getType() {
        return Type;
    }

    public void setType(Accounts.AccountsType type) {
        Type = type;
    }

    public String getUuid() {
        return Uuid;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserName='" + UserName + '\'' +
                ", Type=" + Type +
                ", HeadFile='" + HeadFile + '\'' +
                '}';
    }
}
