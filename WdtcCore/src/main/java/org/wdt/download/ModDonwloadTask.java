package org.wdt.download;

import org.wdt.download.fabric.FabricDownloadTask;
import org.wdt.download.forge.ForgeDownloadTask;
import org.wdt.download.forge.ForgeInstallTask;
import org.wdt.download.forge.ForgeLaunchTask;
import org.wdt.game.Launcher;
import org.wdt.game.ModList;

import java.io.IOException;

public class ModDonwloadTask extends ModList {
    private static void DownloadForge(Launcher launcher) throws IOException {
        ForgeDownloadTask download = launcher.getForgeDownloadTask();
        ForgeInstallTask install = download.getForgeInstallTask();
        ForgeLaunchTask forgeLaunchTask = download.getForgeLaunchTask();
        download.DownloadInstallJar();
        download.getInstallProfile();
        download.DownloadInstallPrefileLibarary();
        install.DownloadClientText();
        install.InstallForge();
        forgeLaunchTask.getForgeVersionJson();
        forgeLaunchTask.DownloadVersionJsonLibarary();
    }

    private static void DownloadFabric(Launcher launcher) throws IOException {
        FabricDownloadTask fabricDownloadTask = launcher.getFabricModDownloadTask();
        fabricDownloadTask.DownloadFile();
        if (fabricDownloadTask.getAPIDownloadTaskNoNull()) {
            fabricDownloadTask.getAPIDownloadTask().DownloadFabricAPI();
        }
    }

    public static void DownloadMod(Launcher launcher) throws IOException {
        if (GameModIsForge(launcher)) {
            DownloadForge(launcher);
        } else if (GameModIsFabric(launcher)) {
            DownloadFabric(launcher);
        }
    }
}
