package org.wdt.wdtc.core.auth.yggdrasil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.core.auth.BaseUser;
import org.wdt.wdtc.core.auth.User;
import org.wdt.wdtc.core.auth.accounts.Accounts;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.manger.URLManger;
import org.wdt.wdtc.core.utils.SkinUtils;
import org.wdt.wdtc.core.utils.StringUtils;
import org.wdt.wdtc.core.utils.URLUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.core.utils.gson.JSONObject;
import org.wdt.wdtc.core.utils.gson.JSONUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

@Getter
public class YggdrasilAccounts extends BaseUser {
  private static final Logger logmaker = WdtcLogger.getLogger(YggdrasilAccounts.class);
  private final String url;
  private final String username;
  private final String password;

  public YggdrasilAccounts(String url, String username, String password) {
    this.password = password;
    this.username = username;
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
    PostJsonObject jsonObject = new PostJsonObject(username, password);
    String jsonStr = JSONObject.toJSONString(jsonObject);
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
    return JSONObject.parseObject(sendPostWithJson(), UserInformation.class);
  }

  public YggdrasilTextures getYggdrasilTextures() {
    return new YggdrasilTextures(this);
  }

  @Override
  public User getUser() throws IOException {
    UserInformation UserInfo = getUserInformation();
    YggdrasilTextures textures = getYggdrasilTextures();
    User user = new User();
    user.setType(Accounts.AccountsType.Yggdrasil);
    JSONObject selectedProfile = new JSONObject(UserInfo.getSelectedProfile());
    user.setUserName(selectedProfile.getString("name"));
    user.setUuid(selectedProfile.getString("id"));
    user.setAccessToken(UserInfo.getAccessToken());
    user.setAPI(URLUtils.getURLToString(URLManger.getLittleskinApi()));
    user.setAPIBase64(StringUtils.StringToBase64(user.getAPI()));
    SkinUtils utils = textures.getUtils();
    user.setHeadFile(utils.writeSkinHead());
    JSONUtils.writeObjectToJsonFile(FileManger.getUsersJson(), user);
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
