package org.wdt.wdtc.auth;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.auth.accounts.Accounts;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.utils.gson.JSONUtils;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
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
    private File HeadFile;

    public User() {
    }

    public static void setUserToJson(User user) {
        JSONUtils.ObjectToJsonFile(FileManger.getUsersJson(), user);
    }

    @SneakyThrows(IOException.class)
    public static User getUsers() {
        return JSONUtils.JsonFileToClass(FileManger.getUsersJson(), User.class);
    }

    public static boolean isExistUserJsonFile() {
        return FileUtils.isFileExists(FileManger.getUsersJson());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return UserName.equals(user.getUserName());
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
