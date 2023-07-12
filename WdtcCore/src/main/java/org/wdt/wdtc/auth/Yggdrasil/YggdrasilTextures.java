package org.wdt.wdtc.auth.Yggdrasil;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class YggdrasilTextures {
    private static final Logger logmaker = getWdtcLogger.getLogger(YggdrasilTextures.class);
    private final String username;
    private String url;

    public YggdrasilTextures(YggdrasilAccounts yggdrasilAccounts) throws IOException {
        username = yggdrasilAccounts.GetUserName();
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
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(GetUserJson());
        client.executeMethod(method);
        JSONObject object = JSONObject.parseObject(method.getResponseBodyAsString());
        method.releaseConnection();
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
