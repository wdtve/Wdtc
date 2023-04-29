package org.wdt;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class StringUtil {
    public static String GetUrlContent(URL url) throws IOException {
        URLConnection uc = url.openConnection();
        return IOUtils.toString(uc.getInputStream(), StandardCharsets.UTF_8);
    }

    public static String GetUrlContent(String url_string) throws IOException {
        return GetUrlContent(new URL(url_string));
    }

    public static JSONObject FileToJSONObject(File file) throws IOException {
        return JSONObject.parseObject(FileUtils.readFileToString(file, "UTF-8"));
    }

    public static JSONObject FileToJSONObject(String filepath) throws IOException {
        return FileToJSONObject(new File(filepath));
    }
}
