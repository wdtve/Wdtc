package org.wdt.wdtc.core.download.forge

import org.wdt.utils.gson.*
import org.wdt.utils.io.FilenameUtils
import org.wdt.utils.io.newInputStream
import org.wdt.wdtc.core.download.SpeedOfProgress
import org.wdt.wdtc.core.download.game.DownloadGameClass
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.game.config.DefaultGameConfig
import org.wdt.wdtc.core.game.config.gameConfig
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.manger.isNotOfficialDownloadSource
import org.wdt.wdtc.core.manger.pistonDataMojang
import org.wdt.wdtc.core.manger.wdtcCache
import org.wdt.wdtc.core.utils.*
import org.wdt.wdtc.core.utils.DefaultDependency
import org.wdt.wdtc.core.utils.DependencyDownload
import java.io.*
import java.util.jar.JarInputStream
import java.util.regex.Matcher
import java.util.regex.Pattern

// TODO Repair and optimization Forge install
class ForgeInstallTask(launcher: Launcher, forgeVersion: String) :
  ForgeDownloadInfo(launcher, forgeVersion), InstallTaskInterface {

  private val config: DefaultGameConfig.Config = launcher.gameConfig.config!!


  constructor(launcher: Launcher, versionJsonObjectInterface: VersionJsonObjectInterface) : this(
    launcher,
    versionJsonObjectInterface.versionNumber!!
  )

  @Throws(IOException::class)
  fun getCommandLine(index: Int): String {
    val jsonObject = installPrefileJSONObject.getJsonArray("processors").getJsonObject(index)
    val commandLineBuilder = StringBuilder()
    commandLineBuilder.append(config.javaPath).append(" -cp ")
    val jarList = jsonObject.getJsonArray("classpath")
    for (i in 0 until jarList.size()) {
      val jar = DependencyDownload(jarList.getString(i))
      jar.downloadPath = launcher.gameLibraryDirectory
      commandLineBuilder.append(FilenameUtils.separatorsToWindows(jar.libraryFilePath)).append(";")
    }
    val mainJar = DependencyDownload(jsonObject.getString("jar"))
    mainJar.downloadPath = launcher.gameLibraryDirectory
    val mainClass =
      JarInputStream(mainJar.libraryFile.newInputStream()).manifest.mainAttributes.getValue("Main-Class")
    commandLineBuilder.append(mainJar.libraryFilePath).append(" ").append(mainClass).append(" ")
    val argsList = jsonObject.getJsonArray("args")
    for (i in 0 until argsList.size()) {
      if (i % 2 == 0) {
        commandLineBuilder.append(argsList.getString(i)).append(" ")
      } else {
        val argeStr = argsList.getString(i)
        val middle = getMiddleBracket(argeStr)
        val large = getLargeBracket(argeStr)
        if (argeStr == "{MINECRAFT_JAR}") {
          commandLineBuilder.append(launcher.versionJar).append(" ")
        } else if (argeStr == "{BINPATCH}") {
          unZipBySpecifyFile(forgeInstallJarFile, clientLzmaPath)
          commandLineBuilder.append(clientLzmaPath).append(" ")
        } else if (argeStr == "{SIDE}") {
          commandLineBuilder.append("client").append(" ")
        } else if (large.find()) {
          val client = DependencyDownload(
            cleanString(
              installPrefileJSONObject.getJsonObject("data")
                .getJsonObject(large.group(1)).getString("client")
            )
          )
          client.downloadPath = launcher.gameLibraryDirectory
          commandLineBuilder.append(client.libraryFilePath).append(" ")
        } else if (middle.find()) {
          val arge = DependencyDownload(middle.group(1))
          arge.downloadPath = launcher.gameLibraryDirectory
          commandLineBuilder.append(arge.libraryFilePath).append(" ")
        } else {
          commandLineBuilder.append(argeStr).append(" ")
        }
      }
    }
    return FilenameUtils.separatorsToWindows(commandLineBuilder.toString())
  }

  @Throws(IOException::class)
  fun downloadClientText() {
    var txtPath: DefaultDependency? = null
    val dataObject = installPrefileJSONObject.getJsonObject("data")
    if (dataObject.has("MOJMAPS")) {
      val matcher = getMiddleBracket(dataObject.getJsonObject("MOJMAPS").getString("client"))
      if (matcher.find()) {
        txtPath = DefaultDependency(matcher.group(1))
      }
    } else {
      val matcher = getMiddleBracket(dataObject.getJsonObject("MAPPINGS").getString("client"))
      if (matcher.find()) {
        txtPath = DefaultDependency(matcher.group(1))
      }
    }
    var txtUrl = launcher.versionJson.readFileToJsonObject().getJsonObject("downloads")
      .getJsonObject("client_mappings").getString("url")
    if (isNotOfficialDownloadSource) {
      txtUrl = txtUrl.replace(pistonDataMojang, downloadSource.dataUrl).toURL().getRedirectUrl()
    }
    if (txtPath != null) {
      startDownloadTask(txtUrl, File(launcher.gameLibraryDirectory, txtPath.formJar()))
    }
  }

  @Throws(IOException::class)
  fun startInstallForge() {
    val objects = installPrefileJSONObject.getJsonArray("processors")
    for (i in 0 until objects.size()) {
      val taskJson = objects.getJsonObject(i)
      if (taskJson.has("sides")) {
        if (taskJson.getJsonArray("sides").getString(0) == "client") {
          startRunCommand(i)
        }
      } else {
        if (taskJson.getJsonArray("args").getString(1) != "DOWNLOAD_MOJMAPS") {
          startRunCommand(i)
        }
      }
    }
  }

  private val clientLzmaPath: File = File(wdtcCache, "/data/client.lzma")


  private fun cleanString(str: String): String {
    return str.cleanStrInString("{").cleanStrInString("}").cleanStrInString("[").cleanStrInString("]")
  }

  @Throws(IOException::class)
  private fun startRunCommand(i: Int) {
    val commmand = getCommandLine(i)
    logmaker.info("Command Line:$commmand")
    val process = Runtime.getRuntime().exec(arrayOf("cmd", "/c", commmand))
    val bufferedReader = BufferedReader(InputStreamReader(process.inputStream, "GBK"))
    var line: String?
    while (bufferedReader.readLine().also { line = it } != null) {
      logmaker.info(line)
    }
    process.destroy()
  }

  private fun getMiddleBracket(args: String): Matcher {
    return Pattern.compile("\\[(.+)]").matcher(args)
  }

  private fun getLargeBracket(args: String): Matcher {
    return Pattern.compile("\\{(.+)}").matcher(args)
  }

  @Throws(IOException::class)
  override fun overwriteVersionJson() {
    val forgeVersionJsonObject = forgeVersionJsonObject
    val forgeLibraryArray = forgeVersionJsonObject.getJsonArray("libraries")
    val gameVersionJsonObject = launcher.gameVersionJsonObject
    val gameVersionArguments = gameVersionJsonObject.arguments
    val gameVersionGameList = gameVersionArguments?.gameList!!
    val forgeVersionArguments = forgeVersionJsonObject.getJsonObject("arguments")
    gameVersionGameList.addAll(forgeVersionArguments.getJsonArray("game"))
    gameVersionArguments.gameList = gameVersionGameList
    val gameVersionJvmList = gameVersionArguments.jvmList!!
    if (forgeVersionArguments.has("jvm")) {
      gameVersionJvmList.addAll(forgeVersionArguments.getJsonArray("jvm"))
    }
    gameVersionArguments.jvmList = gameVersionJvmList
    gameVersionJsonObject.arguments = gameVersionArguments
    for (i in 0 until forgeLibraryArray.size()) {
      gameVersionJsonObject.libraries?.add(forgeLibraryArray.getJsonObject(i).parseObject())
    }
    gameVersionJsonObject.mainClass = forgeVersionJsonObject.getString("mainClass")
    gameVersionJsonObject.id = "${launcher.versionNumber}-forge-$modVersion"
    launcher.putToVersionJson(gameVersionJsonObject)
  }

  @Throws(IOException::class)
  override fun writeVersionJsonPatches() {
    val versionJsonObject = launcher.gameVersionJsonObject
    versionJsonObject.patches = mutableListOf(
      launcher.versionJson.readFileToJsonObject(),
      forgeVersionJsonFile.readFileToJsonObject()
    )
    launcher.putToVersionJson(versionJsonObject)
  }

  @Throws(IOException::class)
  override fun afterDownloadTask() {
    startDownloadForgeLibraryFile(installProfileFile)
    downloadClientText()
    startInstallForge()
  }

  override fun beforInstallTask() {
    startDownloadInstallJar()
    unZipToInstallProfile()
    unZipToForgeVersionJson()
  }

  @Throws(IOException::class)
  fun startDownloadForgeLibraryFile(file: File) {
    val libraryList = file.readFileToJsonObject().getJsonArray("libraries")
    val speed = SpeedOfProgress(libraryList.size())
    for (i in 0 until libraryList.size()) {
      val libraryObject: LibraryObject = LibraryObject.getLibraryObject(libraryList.getJsonObject(i))
      val task = DownloadGameClass(launcher)
      task.startDownloadLibraryTask(libraryObject, speed)
    }
    speed.await()
  }
}
