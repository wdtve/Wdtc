package org.wdt.WdtcLauncher;

import org.wdt.Launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetJvm {
    private static final String java_home = GetJavaPath.GetRunJavaHome();
    private static String xmx = "1024";

    public static void readJvm(Launcher version) throws IOException {
        StringBuilder jvm_set = new StringBuilder();
        jvm_set.append("@echo off\n").append("cd ").append(version.getGamePath()).append("\n");
        jvm_set.append(java_home).append(" ");
        jvm_set.append("-Dlog4j.configurationFile=").append(version.getVersionLog4j2()).append(" ");
        jvm_set.append("-Xmx").append(xmx).append("M").append(" ");
        jvm_set.append("-Dminecraft.client.jar=").append(version.getVersionJar()).append(" ");
        jvm_set.append("-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32m").append(" ");
        jvm_set.append("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump").append(" ");
        Files.createDirectories(Paths.get(version.getVersionNativesPath()));
        jvm_set.append("-Djava.library.path=").append(version.getVersionNativesPath()).append(" ");
        jvm_set.append("-Dminecraft.launcher.brand=Wdtc -Dminecraft.launcher.version=1.0.0-demo").append(" ").append("-cp").append(" ");
        version.setJvmattribute(jvm_set);
    }
}
