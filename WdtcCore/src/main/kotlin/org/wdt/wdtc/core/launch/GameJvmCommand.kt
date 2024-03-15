package org.wdt.wdtc.core.launch

import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.gameConfig
import org.wdt.wdtc.core.manger.launcherVersion
import org.wdt.wdtc.core.utils.noNull
import java.io.IOException

class GameJvmCommand(private val version: Version) : AbstractGameCommand() {
  private val libraryList: String = GameRuntimeCommnad(version).getCommand().toString()

  @Throws(IOException::class)
  override fun getCommand(): StringBuilder {
    val gameConfig = version.gameConfig.config
	  commandBuilder.append("@echo off").appendLine().append("cd ").append(version.versionDirectory).appendLine()
	  nonBreakingSpace("\"${gameConfig.javaPath}\"")
    nonBreakingSpace("-Dlog4j.configurationFile=", version.versionLog4j2)
    nonBreakingSpace("-Xmx${gameConfig.memory}", "M")
    nonBreakingSpace("-Dminecraft.client.jar=", version.versionJar)
    nonBreakingSpace("-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32m")
    nonBreakingSpace("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump")
    version.gameVersionJsonObject.arguments.jvmList.noNull().forEach {
      if (it.isJsonPrimitive) {
        nonBreakingSpace(it.asString.replaceData(dataMap))
      }
    }
    return commandBuilder
  }

  override val dataMap
    get() = mapOf(
      "natives_directory" to version.versionNativesPath.canonicalPath,
      "launcher_name" to "Wdtc",
      "launcher_version" to launcherVersion,
      "library_directory" to version.gameLibraryDirectory.canonicalPath,
      "classpath_separator" to ";",
      "version_name" to version.versionNumber,
      "classpath" to libraryList
    )

}
