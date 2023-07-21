package org.wdt.test;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.wdt.wdtc.game.DetermineVersionSize;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class test {
    private static final Logger logmaker = Logger.getLogger(test.class);


    public static void executeCmdCommand(String cmdCommand) {
        try {
            Process process = Runtime.getRuntime().exec(cmdCommand);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.err.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void urlio() throws IOException {
    }

    @Test
    public void here() throws IOException {
        Version version = new Version("1.19.4");
        JSONObject jsonObject = JSONObject.parseObject(FileUtils.readFileToString(new File(version.getVersionJson())));
        JSONArray jsonArray = jsonObject.getJSONArray("libraries");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            System.out.println(jsonObject1);

        }
    }

    @Test
    public void maker() throws IOException {
        Files.createDirectories(Paths.get("hello"));
    }

    @Test
    public void commonio() throws IOException, InterruptedException {
        String[] s = {"cmd", "/K cmd start", "C:\\Users\\yuwen\\.wdtc\\WdtcGameLauncherScript.bat"};
        Runtime.getRuntime().exec(s);

    }

    @Test
    public void log() {
        Launcher launcher = new Launcher("1.20");
        System.out.println(DetermineVersionSize.DetermineSize("1.20", launcher));
    }

}
