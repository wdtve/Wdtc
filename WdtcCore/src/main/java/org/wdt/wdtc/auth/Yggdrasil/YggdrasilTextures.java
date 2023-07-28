package org.wdt.wdtc.auth.Yggdrasil;

import org.wdt.platform.gson.JSONObject;
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

    public YggdrasilTextures(YggdrasilAccounts yggdrasilAccounts) throws IOException {
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

    public URL getUserSkinUrl() throws IOException {
        return new URL(url + "/textures/" + GetUserSkinHash());
    }

    public String GetUserSkinHash() throws IOException {
        JSONObject object = JSONObject.parseObject(PlatformUtils.GetUrlContent(GetUserJson()));
        if (Objects.nonNull(object.getJSONObject("skins").getString("default"))) {
            return object.getJSONObject("skins").getString("default");
        } else {
            return object.getJSONObject("skins").getString("slim");
        }
    }

    public void DownloadUserSkin() throws IOException {
        File SkinPath = new File(FilePath.getMinecraftComSkin()
                + "/" + GetUserSkinHash().substring(0, 2) + "/" + GetUserSkinHash());
        DownloadTask.StartDownloadTask(getUserSkinUrl(), SkinPath);
    }
}
