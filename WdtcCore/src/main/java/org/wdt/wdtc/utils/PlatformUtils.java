package org.wdt.wdtc.utils;


import com.google.gson.JsonObject;
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


    public static void PutJSONObject(File file, JsonObject object) {
        try {
            writeStringToFile(file, org.wdt.platform.gson.JSONObject.toJSONString(object), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void PutJSONObject(String FilePath, JsonObject object) {
        PutJSONObject(new File(FilePath), object);
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
