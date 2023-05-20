package org.wdt;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StringUtil extends FileUtils {
    public static String GetUrlContent(URL url) throws IOException, SocketException {
        URLConnection uc = url.openConnection();
        return IOUtils.toString(uc.getInputStream(), StandardCharsets.UTF_8);
    }

    public static String GetUrlContent(String url_string) throws IOException, SocketException {
        return GetUrlContent(new URL(url_string));
    }

    public static JSONObject FileToJSONObject(File file) throws IOException {
        return JSONObject.parseObject(readFileToString(file, "UTF-8"));
    }

    public static JSONObject FileToJSONObject(String filepath) throws IOException {
        return FileToJSONObject(new File(filepath));
    }

    public static void PutJSONObject(File file, JSONObject jsonObject) {
        try {
            writeStringToFile(file, jsonObject.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void PutJSONObject(String filePath, JSONObject jsonObject) {
        PutJSONObject(new File(filePath), jsonObject);
    }

    public static String StringToBase64(String str) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean FileExistenceAndSize(File file) {
        if (file.exists()) {
            return sizeOf(file) == 0;
        } else {
            return true;
        }
    }

    public static boolean FileExistenceAndSize(String filePath) {
        return FileExistenceAndSize(new File(filePath));
    }
}
