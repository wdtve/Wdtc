package org.wdt;

import org.junit.jupiter.api.Test;
import org.wdt.download.SelectGameVersion;
import org.wdt.download.fabric.FabricDownloadTask;
import org.wdt.download.forge.ForgeDownloadTask;
import org.wdt.download.forge.ForgeInstallTask;
import org.wdt.download.forge.ForgeLaunchTask;
import org.wdt.game.Launcher;
import org.wdt.launch.LauncherGame;

import java.io.IOException;

public class testjson {
    @Test
    public void test() throws IOException {
        Launcher launcher = new Launcher("1.19.4");
        ForgeDownloadTask download = new ForgeDownloadTask(launcher, "45.0.64");
        ForgeInstallTask install = new ForgeInstallTask(launcher, "45.0.64");
        SelectGameVersion downloadgame = new SelectGameVersion(launcher);
        ForgeLaunchTask forgeLaunchTask = new ForgeLaunchTask(launcher, "45.0.64");
        forgeLaunchTask.DownloadVersionJsonLibarary();

    }

    @Test
    public void testcommand() throws IOException, InterruptedException {
        Launcher launcher = new Launcher("1.19.4");
        ForgeDownloadTask downloadTask = new ForgeDownloadTask(launcher, "45.0.66");
        launcher.setForgeModDownloadTask(downloadTask);
        SelectGameVersion selectGameVersion = new SelectGameVersion(launcher);
        selectGameVersion.DownloadGame();
        LauncherGame.LauncherVersion(launcher);
    }

    @Test
    public void getlist() throws IOException {
        Launcher launcher = new Launcher("1.19.4");
        FabricDownloadTask fabricFileList = new FabricDownloadTask("0.14.21", launcher);
        launcher.setFabricModDownloadTask(fabricFileList);
        LauncherGame.LauncherVersion(launcher);
    }


}
