package org.wdt.wdtc.core.launch

import org.wdt.utils.io.FileUtils
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.VMManger.launcherVersion
import java.io.IOException

class GameJvmCommand(private val launcher: Launcher) : AbstractGameCommand() {
  private val libraryList: String = GameClassPath(launcher).getCommand().toString()

  @Throws(IOException::class)
  override fun getCommand(): StringBuilder {
    val gameConfig = launcher.gameConfig.config!!
    val versionJsonObject = launcher.gameVersionJsonObject
    commandBuilder.append("@echo off\n").append("cd ").append(launcher.versionDirectory).append("\n")
    NonBreakingSpace("\"" + gameConfig.javaPath + "\"")
    NonBreakingSpace("-Dlog4j.configurationFile=", launcher.versionLog4j2)
    NonBreakingSpace("-Xmx" + gameConfig.memory, "M")
    NonBreakingSpace("-Dminecraft.client.jar=", launcher.versionJar)
    NonBreakingSpace("-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32m")
    NonBreakingSpace("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump")
    FileUtils.createDirectories(launcher.versionNativesPath)
    for (element in versionJsonObject.arguments?.jvmList!!) {
      if (!element.isJsonObject) {
        NonBreakingSpace(replaceData(element.asString))
      }
    }
    return commandBuilder
  }

  private val dataMap: Map<String, String>
    get() = java.util.Map.of(
      "\${natives_directory}",
      launcher.versionNativesPath.canonicalPath,
      "\${launcher_name}",
      "Wdtc",
      "\${launcher_version}",
      launcherVersion,
      "\${library_directory}",
      launcher.gameLibraryDirectory.canonicalPath,
      "\${classpath_separator}",
      ";",
      "\${version_name}",
      launcher.versionNumber,
      "\${classpath}",
      libraryList
    )

  private fun replaceData(strs: String): String {
    var str = strs
    val replaceMap = dataMap
    for (s in replaceMap.keys) {
      str = str.replace(s, replaceMap[s]!!)
    }
    return str
  }
}
