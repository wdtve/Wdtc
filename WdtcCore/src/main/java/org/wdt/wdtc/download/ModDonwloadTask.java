package org.wdt.wdtc.download;

import org.wdt.wdtc.download.fabric.FabricDownloadTask;
import org.wdt.wdtc.download.forge.ForgeDownloadTask;
import org.wdt.wdtc.download.forge.ForgeInstallTask;
import org.wdt.wdtc.download.forge.ForgeLaunchTask;
import org.wdt.wdtc.download.quilt.QuiltDownloadTask;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModList;

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

    private static void DownloadQuilt(Launcher launcher) throws IOException {
        QuiltDownloadTask downloadTask = launcher.getQuiltModDownloadTask();
        downloadTask.DownloadQuilt();
    }

    public static void DownloadMod(Launcher launcher) throws IOException {
        if (GameModIsForge(launcher)) {
            DownloadForge(launcher);
        } else if (GameModIsFabric(launcher)) {
            DownloadFabric(launcher);
        } else if (GameModIsQuilt(launcher)) {
            DownloadQuilt(launcher);
        }
    }
}
