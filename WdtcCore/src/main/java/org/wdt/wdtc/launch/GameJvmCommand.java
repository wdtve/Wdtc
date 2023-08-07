package org.wdt.wdtc.launch;

import com.google.gson.JsonElement;
import org.apache.commons.io.FilenameUtils;
import org.wdt.wdtc.game.GameVersionJsonObject;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.DefaultGameConfig;
import org.wdt.wdtc.platform.Starter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class GameJvmCommand {
    private final StringBuilder JvmSet;
    private final Launcher launcher;

    public GameJvmCommand(Launcher launcher) {
        this.launcher = launcher;
        this.JvmSet = new StringBuilder();
    }

    public StringBuilder GetJvmList() throws IOException {
        DefaultGameConfig gameConfig = launcher.getGameConfig().getGameConfig();
        GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
        JvmSet.append("@echo off\n").append("cd ").append(launcher.getVersionPath()).append("\n");
        NonBreakingSpace("\"" + gameConfig.getJavaPath() + "\"");
        NonBreakingSpace("-Dlog4j.configurationFile=", FilenameUtils.separatorsToWindows(launcher.getVersionLog4j2()));
        NonBreakingSpace("-Xmx" + gameConfig.getRunningMemory(), "M");
        NonBreakingSpace("-Dminecraft.client.jar=", launcher.getVersionJar());
        NonBreakingSpace("-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32m");
        NonBreakingSpace("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
        Files.createDirectories(Paths.get(launcher.getVersionNativesPath()));
        for (JsonElement Element : VersionJsonObject.getArguments().getJvmList()) {
            if (!Element.isJsonObject()) {
                NonBreakingSpace(ReplaceData(Element.getAsString()));
            }
        }
        return JvmSet;
    }

    private Map<String, String> getDataMap() {
        return Map.of("${natives_directory}", launcher.getVersionNativesPath(), "${launcher_name}", "Wdtc",
                "${launcher_version}", Starter.getLauncherVersion(), "${library_directory}", launcher.GetGameLibraryPath(),
                "${classpath_separator}", ";", "${version_name}", launcher.getVersion(),
                "${classpath}", new GetStartLibraryPath(launcher).getLibraryPath().toString());
    }

    private void NonBreakingSpace(Object str) {
        JvmSet.append(str).append(" ");
    }

    private void NonBreakingSpace(String str, String string) {
        NonBreakingSpace(str + string);
    }

    private String ReplaceData(String str) {
        Map<String, String> ReplaceMap = getDataMap();
        for (String s : ReplaceMap.keySet()) {
            str = str.replace(s, ReplaceMap.get(s));
        }
        return str;
    }
}
