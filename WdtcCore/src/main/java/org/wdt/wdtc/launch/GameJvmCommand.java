package org.wdt.wdtc.launch;

import org.apache.commons.io.FilenameUtils;
import org.wdt.wdtc.game.DetermineVersionSize;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.GameConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameJvmCommand {
    private static final String xmx = "1024";
    private static StringBuilder JvmSet;

    public static void GetJvmList(Launcher launcher) throws IOException {
        GameJvmCommand.JvmSet = new StringBuilder();
        GameConfig gameConfig = launcher.getGameConfig();
        JvmSet.append("@echo off\n").append("cd ").append(launcher.getVersionPath()).append("\n");
        Add("\"" + gameConfig.getJavaPath() + "\"");
        Add("-Dlog4j.configurationFile=", FilenameUtils.separatorsToWindows(launcher.getVersionLog4j2()));
        Add("-Xmx" + gameConfig.getRunningMemory(), "M");
        Add("-Dminecraft.client.jar=", launcher.getVersionJar());
        Add("-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32m");
        Add("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
        Files.createDirectories(Paths.get(launcher.getVersionNativesPath()));
        Add("-Djava.library.path=", launcher.getVersionNativesPath());
        if (DetermineVersionSize.DetermineSize("1.20", launcher)) {
            Add("-Djna.tmpdir=", launcher.getVersionNativesPath());
            Add("-Dorg.lwjgl.system.SharedLibraryExtractPath=", launcher.getVersionNativesPath());
            Add("-Dio.netty.native.workdir=", launcher.getVersionNativesPath());
        }
        Add("-Dminecraft.launcher.brand=Wdtc -Dminecraft.launcher.version=1.0.0-demo");
        Add("-cp");
        launcher.setJvmattribute(JvmSet);
    }

    private static void Add(String str) {
        JvmSet.append(str).append(" ");
    }

    private static void Add(String str, String string) {
        Add(str + string);
    }
}
