package org.wdt.wdtc.utils;


import lombok.SneakyThrows;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;

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


public class PlatformUtils {


    public static String StringToBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean FileExistenceAndSize(File file) throws IOException {
        return !file.exists();
    }

    public static boolean FileExistenceAndSize(File file, long size) throws IOException {
        return !file.exists() || FileUtils.sizeOf(file.toPath()) != size;
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
            StringBuilder sha1 = new StringBuilder(new BigInteger(1, digest.digest()).toString(16));
            int length = 40 - sha1.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    sha1.insert(0, "0");
                }
            }
            return sha1.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows(IOException.class)
    public static void StartSomething(Object o) {
        Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start", o.toString()});
    }

    @SneakyThrows(MalformedURLException.class)
    public static boolean isOnline() {
        return NetworkHasThisFile("https://www.bilibili.com");
    }

    public static String UrltoString(String UrlPath) throws IOException {
        return IOUtils.toString(new URL(UrlPath));
    }
}
