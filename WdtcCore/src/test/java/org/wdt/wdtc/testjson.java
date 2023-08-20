package org.wdt.wdtc;

import org.junit.jupiter.api.Test;
import org.wdt.utils.FileUtils;
import org.wdt.wdtc.download.quilt.QuiltInstallTask;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.LauncherGame;

import java.io.File;
import java.io.IOException;

public class testjson {
    @Test
    public void test() throws IOException {
        File file = new File("WdtcCore/src/test/java/org/wdt/wdtc/");
        for (File s : FileUtils.FileList(file)) {
            System.out.println(s);
        }

    }

    @Test
    public void launch() {
        Launcher launcher = new Launcher("1.20.1");
        QuiltInstallTask downloadTask = new QuiltInstallTask(launcher, "0.20.0-beta.3");
        launcher.setQuiltModDownloadInfo(downloadTask);
        LauncherGame launcherGame = new LauncherGame(launcher);

    }


}
