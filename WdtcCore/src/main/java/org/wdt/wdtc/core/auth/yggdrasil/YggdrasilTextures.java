package org.wdt.wdtc.core.auth.yggdrasil;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.wdt.utils.gson.JsonObjectUtils;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.SkinUtils;
import org.wdt.wdtc.core.utils.URLUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class YggdrasilTextures {
  private final String username;
  private String url;

  public YggdrasilTextures(YggdrasilAccounts yggdrasilAccounts) {
    username = yggdrasilAccounts.getUserName();
    url = yggdrasilAccounts.getUrl();
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUserJsonUrl() {
    return url + "/csl/" + username + ".json";
  }

  public String getUserSkinHash() throws IOException {
    Csl csl = getCsl();
    Skins skins = csl.getSkins();
    return skins.getSkinKind();
  }

  public File getUserSkinFile() {
    return new SkinUtils(username).getSkinFile();
  }

  public void DownloadUserSkin() throws IOException {
    String UserSkinHash = getUserSkinHash();
    File SkinPath = new File(FileManger.getMinecraftComSkin(), UserSkinHash.substring(0, 2) + "/" + UserSkinHash);
    DownloadUtils.StartDownloadTask(getSkinUrl(), SkinPath);
  }

  public URL getSkinUrl() throws IOException {
    return new URL(url + "/textures/" + getUserSkinHash());
  }

  public Csl getCsl() throws IOException {
    return JsonObjectUtils.parseObject(URLUtils.getURLToString(getUserJsonUrl()), Csl.class);
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
