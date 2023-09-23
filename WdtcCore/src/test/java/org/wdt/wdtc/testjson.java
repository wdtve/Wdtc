package org.wdt.wdtc;

import org.junit.jupiter.api.Test;
import org.wdt.wdtc.download.quilt.QuiltInstallTask;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.LaunchGame;

import java.io.IOException;

public class testjson {
    @Test
    public void test() throws IOException {
        Launcher launcher = new Launcher("1.19.4");
        System.out.println(launcher.equals(new Launcher("1.19.4")));

    }

    @Test
    public void launch() {
        Launcher launcher = new Launcher("1.20.1");
        QuiltInstallTask downloadTask = new QuiltInstallTask(launcher, "0.20.0-beta.3");
        launcher.setQuiltModDownloadInfo(downloadTask);
        LaunchGame launchGame = new LaunchGame(launcher);

    }


}
