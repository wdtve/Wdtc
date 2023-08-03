package org.wdt.wdtc.test;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class test {
    @Test
    public void getVersion() throws IOException {
        URL url = new URL("https://login.live.com/oauth20_token.srf");
        Map<Object, Object> data = Map.of("client_id", "00000000402b5328",
                "code", "M.C107_BAY.2.70d5f680-4f8c-e60b-8e88-4637c254cee7",
                "grant_type", "authorization_code",
                "redirect_uri", "https://login.live.com/oauth20_desktop.srf",
                "scope", "service::user.auth.xboxlive.com::MBI_SSL");

        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("content-type", "x-www-form-urlencoded");
        PrintWriter printWriter = new PrintWriter(urlConnection.getOutputStream());
        printWriter.print(data);
        printWriter.flush();
        System.out.println(IOUtils.toString(urlConnection.getInputStream()));
    }


}
