package org.wdt.wdtc;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.wdt.wdtc.download.quilt.QuiltInstallTask;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.LauncherGame;

import java.io.FileReader;
import java.io.IOException;

public class testjson {
    @Test
    public void test() throws IOException, InterruptedException {
        System.out.println(JsonParser.parseReader(new FileReader("WdtcCore/src/test/java/org/wdt/wdtc/list.json")));

    }

    @Test
    public void launch() {
        Launcher launcher = new Launcher("1.20.1");
        QuiltInstallTask downloadTask = new QuiltInstallTask(launcher, "0.20.0-beta.3");
        launcher.setQuiltModDownloadInfo(downloadTask);
        LauncherGame launcherGame = new LauncherGame(launcher);

    }


}
