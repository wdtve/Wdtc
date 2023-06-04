package org.wdt.launch;

import org.wdt.Launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetJvm {
    private static final String JAVA_HOME = GetJavaPath.GetRunJavaHome();
    private static StringBuilder JvmSet;
    private static final String xmx = "1024";

    public static void GetJvmList(Launcher version) throws IOException {
        GetJvm.JvmSet = new StringBuilder();
        JvmSet.append("@echo off\n").append("cd ").append(version.getVersionPath()).append("\n");
        Add(JAVA_HOME);
        Add("-Dlog4j.configurationFile=", version.getVersionLog4j2());
        Add("-Xmx" + xmx, "M");
        Add("-Dminecraft.client.jar=", version.getVersionJar());
        Add("-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32m");
        Add("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
        Files.createDirectories(Paths.get(version.getVersionNativesPath()));
        Add("-Djava.library.path=", version.getVersionNativesPath());
        Add("-Dminecraft.launcher.brand=Wdtc -Dminecraft.launcher.version=1.0.0-demo");
        Add("-cp");
        version.setJvmattribute(JvmSet);
    }

    private static void Add(String str) {
        JvmSet.append(str).append(" ");
    }

    private static void Add(String str, String string) {
        Add(str + string);
    }
}
