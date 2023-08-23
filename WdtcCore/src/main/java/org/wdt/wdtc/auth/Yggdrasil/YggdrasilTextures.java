package org.wdt.wdtc.auth.Yggdrasil;

import com.google.gson.annotations.SerializedName;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.auth.skin.SkinUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.utils.PlatformUtils;

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
        File SkinPath = new File(FilePath.getMinecraftComSkin(), UserSkinHash.substring(0, 2) + "/" + UserSkinHash);
        DownloadTask.StartDownloadTask(getSkinUrl(), SkinPath);
    }

    public URL getSkinUrl() throws IOException {
        return new URL(url + "/textures/" + GetUserSkinHash());
    }

    public Csl getCsl() throws IOException {
        return JSONObject.parseObject(PlatformUtils.GetUrlContent(GetUserJson()), Csl.class);
    }

    public SkinUtils getUtils() throws IOException {
        SkinUtils utils = new SkinUtils(getUserSkinFile());
        utils.setUserSkinInput(getSkinUrl().openStream());
        return utils;
    }


    public static class Csl {
        @SerializedName("username")
        private String UserName;
        @SerializedName("skins")
        private Skins Skins;
        @SerializedName("cape")
        private String Cape;


        public Skins getSkins() {
            return Skins;
        }

    }

    public static class Skins {
        @SerializedName(value = "default", alternate = {"slim"})
        private String SkinKind;

        public String getSkinKind() {
            return SkinKind;
        }
    }

}
