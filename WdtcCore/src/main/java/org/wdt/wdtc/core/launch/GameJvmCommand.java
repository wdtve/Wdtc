package org.wdt.wdtc.core.launch;

import com.google.gson.JsonElement;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.game.GameVersionJsonObject;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.game.config.DefaultGameConfig;
import org.wdt.wdtc.core.manger.VMManger;

import java.io.IOException;
import java.util.Map;

public class GameJvmCommand extends AbstractGameCommand {
  private final Launcher launcher;
  private final String LibraryList;

  public GameJvmCommand(Launcher launcher) {
    this.launcher = launcher;
    this.LibraryList = new GameClassPath(launcher).getCommand().toString();
  }

  public StringBuilder getCommand() throws IOException {
    DefaultGameConfig.Config gameConfig = launcher.getGameConfig().getConfig();
    GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
    command.append("@echo off\n").append("cd ").append(launcher.getVersionPath()).append("\n");
    nonBreakingSpace("\"" + gameConfig.getJavaPath() + "\"");
    nonBreakingSpace("-Dlog4j.configurationFile=", launcher.getVersionLog4j2());
    nonBreakingSpace("-Xmx" + gameConfig.getMemory(), "M");
    nonBreakingSpace("-Dminecraft.client.jar=", launcher.getVersionJar());
    nonBreakingSpace("-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32m");
    nonBreakingSpace("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
    FileUtils.createDirectories(launcher.getVersionNativesPath());
    for (JsonElement Element : VersionJsonObject.getArguments().getJvmList()) {
      if (!Element.isJsonObject()) {
        nonBreakingSpace(ReplaceData(Element.getAsString()));
      }
    }
    return command;
  }

  private Map<String, String> getDataMap() {
    return Map.of("${natives_directory}", FileUtils.getCanonicalPath(launcher.getVersionNativesPath()), "${launcher_name}", "Wdtc",
        "${launcher_version}", VMManger.getLauncherVersion(), "${library_directory}", FileUtils.getCanonicalPath(launcher.getGameLibraryDirectory()),
        "${classpath_separator}", ";", "${version_name}", launcher.getVersionNumber(),
        "${classpath}", LibraryList);
  }

  private String ReplaceData(String str) {
    Map<String, String> ReplaceMap = getDataMap();
    for (String s : ReplaceMap.keySet()) {
      str = str.replace(s, ReplaceMap.get(s));
    }
    return str;
  }
}