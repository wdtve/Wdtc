package org.wdt.wdtc.platform;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PlatformUtils extends FileUtils {
    public static String GetUrlContent(URL url) throws IOException {
        return IOUtils.toString(url, StandardCharsets.UTF_8);
    }

    public static String GetUrlContent(String StriingUrl) throws IOException {
        return GetUrlContent(new URL(StriingUrl));
    }

    public static JSONObject FileToJSONObject(File file) throws IOException {
        if (FileExistenceAndSize(file)) {
            throw new IOException(file + " no exists!");
        } else {
            return JSONObject.parseObject(readFileToString(file, "UTF-8"));
        }
    }

    public static JSONObject FileToJSONObject(String filepath) throws IOException {
        return FileToJSONObject(new File(filepath));
    }

    public static void PutKeyToFile(File file, JSONObject jsonObject, String key, Object object) {
        jsonObject.put(key, object);
        PutJSONObject(file, jsonObject);
    }

    public static void PutKeyToFile(String filePath, JSONObject jsonObject, String key, Object object) {
        PutKeyToFile(new File(filePath), jsonObject, key, object);
    }

    public static void PutJSONObject(File file, JSONObject jsonObject) {
        try {
            writeStringToFile(file, jsonObject.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void PutJSONObject(String filepath, JSONObject jsonObject) {
        PutJSONObject(new File(filepath), jsonObject);
    }

    public static String StringToBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean FileExistenceAndSize(File file) throws IOException {
        return FileExistenceAndSize(file, 0);
    }

    public static boolean FileExistenceAndSize(File file, long size) throws IOException {
        if (file.exists()) {
            if (file.isFile()) {
                return sizeOf(file) == size;
            } else {
                throw new IOException(file + "is not file!");
            }
        } else {
            return true;
        }
    }

    public static boolean FileExistenceAndSize(String path, long size) throws IOException {
        return FileExistenceAndSize(new File(path), size);
    }


    public static boolean FileExistenceAndSize(String filePath) throws IOException {
        return FileExistenceAndSize(new File(filePath));
    }

    public static boolean NetworkHasThisFile(String url) throws MalformedURLException {
        return NetworkHasThisFile(new URL(url));
    }

    public static boolean NetworkHasThisFile(URL url) {
        try {
            IOUtils.toString(url.openStream());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
