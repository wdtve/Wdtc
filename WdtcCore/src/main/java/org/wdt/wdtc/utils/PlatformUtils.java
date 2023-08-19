package org.wdt.wdtc.utils;


import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.wdt.platform.gson.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PlatformUtils extends FileUtils {
    public static String GetUrlContent(URL url) throws IOException {
        return IOUtils.toString(url, StandardCharsets.UTF_8);
    }

    public static String GetUrlContent(String StriingUrl) throws IOException {
        return GetUrlContent(new URL(StriingUrl));
    }


    public static void PutJSONObject(File file, JsonObject object) {
        JSONUtils.ObjectToJsonFile(file, object);
    }

    public static void PutJSONObject(String FilePath, JsonObject object) {
        PutJSONObject(new File(FilePath), object);
    }

    public static String StringToBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean FileExistenceAndSize(File file) throws IOException {
        return !file.exists();
    }

    public static boolean FileExistenceAndSize(File file, long size) throws IOException {
        if (file.exists()) {
            if (size != 0) {
                return sizeOf(file) != size;
            } else {
                return false;
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
            URLConnection uc = url.openConnection();
            uc.setConnectTimeout(12000);
            uc.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String getRedirectUrl(String path) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(5000);
        return conn.getHeaderField("Location");
    }

    public static String getFileSha1(InputStream in) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024 * 1024 * 10];
            int len;
            while ((len = in.read(buffer)) > 0) {
                digest.update(buffer, 0, len);
            }
            String sha1 = new BigInteger(1, digest.digest()).toString(16);
            int length = 40 - sha1.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    sha1 = "0" + sha1;
                }
            }
            return sha1;
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
