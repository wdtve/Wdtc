package org.wdt.wdtc.core.utils;

import lombok.SneakyThrows;
import org.wdt.utils.io.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URLUtils {

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
        HttpURLConnection conn = (HttpURLConnection) toURL(path).openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(5000);
        return conn.getHeaderField("Location");
    }


    public static boolean isOnline() {
        return isNetworkHasThisFile(toURL("https://www.bilibili.com"));
    }

    public static String getURLToString(String UrlPath) throws IOException {
        return IOUtils.toString(toURL(UrlPath));
    }

    @SneakyThrows(IOException.class)
    public static void openSomething(Object o) {
        Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start", o.toString()});
    }

    public static InputStream newInputStream(URL url) throws IOException {
        HttpsURLConnection connection = toHttpsURLConnection(url);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        return connection.getInputStream();
    }

    public static HttpsURLConnection toHttpsURLConnection(URL url) throws IOException {
        return (HttpsURLConnection) url.openConnection();
    }

    @SneakyThrows(IOException.class)
    public static URL toURL(String url) {
        return new URL(url);
    }

    public static URL toURL(String url, String url1) {
        return toURL(StringUtils.appendForString(url, url1));
    }

    public static URL toURL(URL url, String url1) {
        return toURL(StringUtils.appendForString(url, url1));
    }
}
