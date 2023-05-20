package org.wdt.WdtcLauncher;

import org.wdt.Launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetJvm {
    private static final String java_home = GetJavaPath.GetRunJavaHome();
    private static String xmx = "1024";

    public static void readJvm(Launcher version) throws IOException {
        StringBuilder JvmSet = new StringBuilder();
        JvmSet.append("@echo off\n").append("cd ").append(version.getVersionPath()).append("\n");
        JvmSet.append(java_home).append(" ");
        JvmSet.append("-Dlog4j.configurationFile=").append(version.getVersionLog4j2()).append(" ");
        JvmSet.append("-Xmx").append(xmx).append("M").append(" ");
        JvmSet.append("-Dminecraft.client.jar=").append(version.getVersionJar()).append(" ");
        JvmSet.append("-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32m").append(" ");
        JvmSet.append("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump").append(" ");
        Files.createDirectories(Paths.get(version.getVersionNativesPath()));
        JvmSet.append("-Djava.library.path=").append(version.getVersionNativesPath()).append(" ");
        JvmSet.append("-Dminecraft.launcher.brand=Wdtc -Dminecraft.launcher.version=1.0.0-demo").append(" ").append("-cp").append(" ");
        version.setJvmattribute(JvmSet);
    }
}
