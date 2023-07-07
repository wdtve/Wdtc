package org.wdt.auth.Yggdrasil;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.game.FilePath;
import org.wdt.platform.PlatformUtils;

import java.io.IOException;

public class YggdrasilAccounts {
    private final Logger logmaker = Logger.getLogger(YggdrasilAccounts.class);
    private String url;
    private String username;
    private String password;

    public YggdrasilAccounts(String url, String username, String password) {
        this.password = password;
        this.username = username;
        this.url = url;
    }

    public YggdrasilAccounts() {
    }

    public void sendPostWithJson() throws IOException {
        String requestUrl = url + "/api/yggdrasil/authserver/authenticate";
        String jsonStr = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"requestUser\":true," +
                "\"agent\":{" +
                "\"name\":\"Minecraft\"," +
                "\"version\":1" +
                "}" +
                "}";
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(3 * 1000);
        client.getHttpConnectionManager().getParams().setSoTimeout(3 * 60 * 1000);
        client.getParams().setContentCharset("UTF-8");
        PostMethod postMethod = new PostMethod(requestUrl);
        postMethod.setRequestHeader("content-type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity(jsonStr, "application/json", "UTF-8");
        postMethod.setRequestEntity(requestEntity);
        int status = client.executeMethod(postMethod);
        if (status == HttpStatus.SC_OK) {
            FileUtils.writeStringToFile(FilePath.getYggdrasilFile(), postMethod.getResponseBodyAsString(), "UTF-8");
        } else {
            throw new RuntimeException("接口连接失败！");
        }

    }

    public String getUrl() {
        return url;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String GetResponse() throws IOException {
        return FileUtils.readFileToString(FilePath.getYggdrasilFile(), "UTF-8");
    }

    public String GetUserUuid() throws IOException {
        return YggdrasilFileObject().getJSONObject("selectedProfile").getString("id");
    }

    public String GetUserName() throws IOException {
        return YggdrasilFileObject().getJSONObject("selectedProfile").getString("name");
    }

    public String GetClientToken() throws IOException {
        return YggdrasilFileObject().getString("clientToken");
    }

    public String GetAccessToken() throws IOException {
        return YggdrasilFileObject().getString("accessToken");
    }


    public JSONObject YggdrasilFileObject() throws IOException {
        return PlatformUtils.FileToJSONObject(FilePath.getYggdrasilFile());
    }
}
