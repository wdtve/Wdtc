package org.wdt.wdtc.core.auth.yggdrasil;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.SkinUtils;
import org.wdt.wdtc.core.utils.URLUtils;
import org.wdt.wdtc.core.utils.gson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class YggdrasilTextures {
  private final String username;
  private String url;

  public YggdrasilTextures(YggdrasilAccounts yggdrasilAccounts) {
    username = yggdrasilAccounts.getUsername();
    if (Objects.nonNull(yggdrasilAccounts.getUrl())) {
      url = yggdrasilAccounts.getUrl();
    } else {
      throw new NullPointerException("URL为空");
    }
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String GetUserJson() {
    return url + "/csl/" + username + ".json";
  }

  public String GetUserSkinHash() throws IOException {
    Csl csl = getCsl();
    Skins skins = csl.getSkins();
    return skins.getSkinKind();
  }

  public File getUserSkinFile() {
    return new SkinUtils(username).getSkinFile();
  }

  public void DownloadUserSkin() throws IOException {
    String UserSkinHash = GetUserSkinHash();
    File SkinPath = new File(FileManger.getMinecraftComSkin(), UserSkinHash.substring(0, 2) + "/" + UserSkinHash);
    DownloadUtils.StartDownloadTask(getSkinUrl(), SkinPath);
  }

  public URL getSkinUrl() throws IOException {
    return new URL(url + "/textures/" + GetUserSkinHash());
  }

  public Csl getCsl() throws IOException {
    return JSONObject.parseObject(URLUtils.getURLToString(GetUserJson()), Csl.class);
  }

  public SkinUtils getUtils() throws IOException {
    SkinUtils utils = new SkinUtils(getUserSkinFile());
    utils.setUserSkinInput(getSkinUrl().openStream());
    return utils;
  }


  public static class Csl {
    @SerializedName("username")
    private String UserName;
    @Getter
    @SerializedName("skins")
    private Skins Skins;
    @SerializedName("cape")
    private String Cape;

  }

  @Getter
  public static class Skins {
    @SerializedName(value = "default", alternate = {"slim"})
    private String SkinKind;
  }

}
