package org.wdt.wdtc.core.auth;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.auth.accounts.Accounts;
import org.wdt.wdtc.core.manger.FileManger;

import java.io.File;
import java.io.IOException;

@Data
public class User {
  @EqualsAndHashCode.Include
  @SerializedName("userName")
  private String UserName;

  @ToString.Exclude
  @SerializedName("accessToken")
  private String AccessToken;

  @EqualsAndHashCode.Include
  @SerializedName("type")
  private Accounts.AccountsType Type;

  @ToString.Exclude
  @SerializedName("uuid")
  private String Uuid;

  @ToString.Exclude
  @SerializedName("metaData")
  private String API;

  @ToString.Exclude
  @SerializedName("base64Data")
  private String APIBase64;

  @SerializedName("headFile")
  private File HeadFile;

  public User() {
  }

  public User(String userName, String accessToken, Accounts.AccountsType type, String uuid, String API, String APIBase64, File headFile) {
    UserName = userName;
    AccessToken = accessToken;
    Type = type;
    Uuid = uuid;
    this.API = API;
    this.APIBase64 = APIBase64;
    HeadFile = headFile;
  }

  public User(String userName, String accessToken, Accounts.AccountsType type, String uuid) {
    UserName = userName;
    AccessToken = accessToken;
    Type = type;
    Uuid = uuid;
  }

  @SneakyThrows
  public static void setUserToJson(User user) {
    JsonUtils.writeObjectToFile(FileManger.getUsersJson(), user);
  }

  @SneakyThrows(IOException.class)
  public static User getUser() {
    return JsonUtils.readFileToClass(FileManger.getUsersJson(), User.class);
  }

  public static boolean isExistUserJsonFile() {
    return FileUtils.isFileExists(FileManger.getUsersJson());
  }


}
