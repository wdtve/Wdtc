package org.wdt.wdtc.launch;

import com.google.gson.JsonElement;
import org.wdt.utils.FileUtils;
import org.wdt.wdtc.game.GameVersionJsonObject;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.DefaultGameConfig;
import org.wdt.wdtc.platform.VMManger;

import java.io.IOException;
import java.util.Map;

public class GameJvmCommand {
    private final StringBuilder JvmSet;
    private final Launcher launcher;
    private final String LibraryList;

    public GameJvmCommand(Launcher launcher) {
        this.launcher = launcher;
        this.JvmSet = new StringBuilder();
        this.LibraryList = new GetStartLibraryPath(launcher).getLibraryPath().toString();
    }

    public StringBuilder GetJvmList() throws IOException {
        DefaultGameConfig.Config gameConfig = launcher.getGameConfig().getConfig();
        GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
        JvmSet.append("@echo off\n").append("cd ").append(launcher.getVersionPath()).append("\n");
        NonBreakingSpace("\"" + gameConfig.getJavaPath() + "\"");
        NonBreakingSpace("-Dlog4j.configurationFile=", launcher.getVersionLog4j2());
        NonBreakingSpace("-Xmx" + gameConfig.getRunningMemory(), "M");
        NonBreakingSpace("-Dminecraft.client.jar=", launcher.getVersionJar());
        NonBreakingSpace("-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32m");
        NonBreakingSpace("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
        FileUtils.createDirectories(launcher.getVersionNativesPath());
        for (JsonElement Element : VersionJsonObject.getArguments().getJvmList()) {
            if (!Element.isJsonObject()) {
                NonBreakingSpace(ReplaceData(Element.getAsString()));
            }
        }
        return JvmSet;
    }

    private Map<String, String> getDataMap() {
        return Map.of("${natives_directory}", FileUtils.getCanonicalPath(launcher.getVersionNativesPath()), "${launcher_name}", "Wdtc",
                "${launcher_version}", VMManger.getLauncherVersion(), "${library_directory}", FileUtils.getCanonicalPath(launcher.getGameLibraryPath()),
                "${classpath_separator}", ";", "${version_name}", launcher.getVersionNumber(),
                "${classpath}", LibraryList);
    }

    private void NonBreakingSpace(Object str) {
        JvmSet.append(str).append(" ");
    }

    private void NonBreakingSpace(String str, Object string) {
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
