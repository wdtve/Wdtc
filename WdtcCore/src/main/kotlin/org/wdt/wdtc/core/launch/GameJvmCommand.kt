package org.wdt.wdtc.core.launch

import org.wdt.utils.io.FileUtils
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.game.config.GameConfig.Companion.gameConfig
import org.wdt.wdtc.core.manger.VMManger.launcherVersion
import java.io.IOException

class GameJvmCommand(private val launcher: Launcher) : AbstractGameCommand() {
  private val libraryList: String = GameClassPath(launcher).getCommand().toString()

  @Throws(IOException::class)
  override fun getCommand(): StringBuilder {
    val gameConfig = launcher.gameConfig.config!!
    val versionJsonObject = launcher.gameVersionJsonObject
    commandBuilder.append("@echo off\n").append("cd ").append(launcher.versionDirectory).append("\n")
    nonBreakingSpace("\"" + gameConfig.javaPath + "\"")
    nonBreakingSpace("-Dlog4j.configurationFile=", launcher.versionLog4j2)
    nonBreakingSpace("-Xmx" + gameConfig.memory, "M")
    nonBreakingSpace("-Dminecraft.client.jar=", launcher.versionJar)
    nonBreakingSpace("-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32m")
    nonBreakingSpace("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump")
    FileUtils.createDirectories(launcher.versionNativesPath)
    for (element in versionJsonObject.arguments?.jvmList!!) {
      if (!element.isJsonObject) {
        nonBreakingSpace(replaceData(element.asString))
      }
    }
    return commandBuilder
  }

  private val dataMap
    get() = mutableMapOf(
      "\${natives_directory}" to
          launcher.versionNativesPath.canonicalPath,
      "\${launcher_name}" to
          "Wdtc",
      "\${launcher_version}" to
          launcherVersion,
      "\${library_directory}" to
          launcher.gameLibraryDirectory.canonicalPath,
      "\${classpath_separator}" to
          ";",
      "\${version_name}" to
          launcher.versionNumber,
      "\${classpath}" to
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
