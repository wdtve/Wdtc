package org.wdt.wdtc.core.auth.yggdrasil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.wdt.utils.gson.Json;
import org.wdt.utils.gson.JsonObjectUtils;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.core.auth.BaseUser;
import org.wdt.wdtc.core.auth.User;
import org.wdt.wdtc.core.auth.accounts.Accounts;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.manger.URLManger;
import org.wdt.wdtc.core.utils.StringUtils;
import org.wdt.wdtc.core.utils.URLUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

@Getter
public class YggdrasilAccounts extends BaseUser {
  private static final Logger logmaker = WdtcLogger.getLogger(YggdrasilAccounts.class);
  @NotNull
  private final String url;
  @NotNull
  private final String userName;
  @NotNull
  private final String pwd;

  public YggdrasilAccounts(@NotNull String url, @NotNull String userName, @NotNull String password) {
    this.pwd = password;
    this.userName = userName;
    this.url = url;
  }

  public String sendPostWithJson() throws IOException {
    URL requestUrl = new URL(url + "/api/yggdrasil/authserver/authenticate");
//        String jsonStr = "{" +
//                "\"username\":\"" + username + "\"," +
//                "\"password\":\"" + password + "\"," +
//                "\"requestUser\":true," +
//                "\"agent\":{" +
//                "\"name\":\"Minecraft\"," +
//                "\"version\":1" +
//                "}" +
//                "}";
    String jsonStr = Json.toJsonString(new PostJsonObject(userName, pwd));
    URLConnection conn = requestUrl.openConnection();
    conn.setRequestProperty("content-type", "application/json");
    conn.setDoOutput(true);
    conn.setDoInput(true);
    PrintWriter out = new PrintWriter(conn.getOutputStream());
    out.print(jsonStr);
    out.flush();
    return IOUtils.toString(conn.getInputStream());
  }

  public UserInformation getUserInformation() throws IOException {
    return JsonObjectUtils.parseObject(sendPostWithJson(), UserInformation.class);
  }

  public YggdrasilTextures getYggdrasilTextures() {
    return new YggdrasilTextures(this);
  }

  @Override
  public User getUser() throws IOException {
    UserInformation userInfo = getUserInformation();
    YggdrasilTextures textures = getYggdrasilTextures();
    JsonObject selectedProfile = userInfo.getSelectedProfile();
    String api = URLUtils.getURLToString(URLManger.getLittleskinApi());
    User user = new User(
        selectedProfile.get("name").getAsString(),
        userInfo.accessToken,
        Accounts.AccountsType.Yggdrasil,
        selectedProfile.get("id").getAsString(),
        api,
        StringUtils.StringToBase64(api),
        textures.getUtils().getSkinFile());
    JsonUtils.writeObjectToFile(FileManger.getUsersJson(), user, Json.getBuilder().setPrettyPrinting());
    logmaker.info(user);
    return user;
  }

  public static class PostJsonObject {
    @SerializedName("username")
    private final String username;
    @SerializedName("password")
    private final String password;
    @SerializedName("requestUser")
    private final boolean requestUser;
    @SerializedName("agent")
    private final Agent agent;

    public PostJsonObject(String username, String password) {
      this.username = username;
      this.password = password;
      this.requestUser = true;
      this.agent = new Agent();
    }

    public static class Agent {
      @SerializedName("name")
      private final String name;


      @SerializedName("version")
      private final int version;

      public Agent() {
        this.name = "Minecraft";
        this.version = 1;
      }
    }
  }

  @Getter
  public static class UserInformation {
    public String accessToken;
    public String clientToken;
    public JsonArray availableProfiles;
    public JsonObject user;
    public JsonObject selectedProfile;
  }

}
