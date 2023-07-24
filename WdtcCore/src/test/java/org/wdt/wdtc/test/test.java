package org.wdt.wdtc.test;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.game.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class test {
    private static final Logger logmaker = Logger.getLogger(test.class);


    public static void executeCmdCommand(String cmdCommand) {
        try {
            Process process = Runtime.getRuntime().exec(cmdCommand);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
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

    public static void main(String[] args) throws InterruptedException {
        SpeedOfProgress speedOfProgress = new SpeedOfProgress(100.00);
        Timer timer = new Timer();
        for (int i = 0; i < 100; i++) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    DecimalFormat s = new DecimalFormat("0");
                    System.out.println(s.format((speedOfProgress.getSpend() / 100) * 100));
                    try {
                        TimeUnit.SECONDS.sleep(20);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    speedOfProgress.countDown();

                }
            };
            timer.schedule(timerTask, 50, 999);
        }
        try {
            speedOfProgress.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        timer.cancel();


    }

}
