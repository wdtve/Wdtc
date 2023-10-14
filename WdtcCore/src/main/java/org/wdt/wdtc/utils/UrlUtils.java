package org.wdt.wdtc.utils;

import lombok.SneakyThrows;
import org.wdt.utils.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UrlUtils {
    public static boolean isNetworkHasThisFile(String url) throws MalformedURLException {
        return isNetworkHasThisFile(new URL(url));
    }

    public static boolean isNetworkHasThisFile(URL url) {
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


    @SneakyThrows(MalformedURLException.class)
    public static boolean isOnline() {
        return isNetworkHasThisFile("https://www.bilibili.com");
    }

    public static String getUrlToString(String UrlPath) throws IOException {
        return IOUtils.toString(new URL(UrlPath));
    }

    @SneakyThrows(IOException.class)
    public static void openSomething(Object o) {
        Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start", o.toString()});
    }
}
