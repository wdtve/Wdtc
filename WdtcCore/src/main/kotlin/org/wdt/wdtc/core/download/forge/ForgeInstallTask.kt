package org.wdt.wdtc.core.download.forge

import com.google.gson.JsonObject
import org.wdt.utils.dependency.DefaultDependency
import org.wdt.utils.dependency.DependencyDownload
import org.wdt.utils.gson.*
import org.wdt.utils.io.FilenameUtils
import org.wdt.utils.io.newInputStream
import org.wdt.wdtc.core.download.SpeedOfProgress
import org.wdt.wdtc.core.download.game.DownloadGameClass
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.game.config.DefaultGameConfig
import org.wdt.wdtc.core.manger.DownloadSourceManger.isNotOfficialDownloadSource
import org.wdt.wdtc.core.manger.FileManger.wdtcCache
import org.wdt.wdtc.core.manger.URLManger.pistonDataMojang
import org.wdt.wdtc.core.utils.DownloadUtils.Companion.startDownloadTask
import org.wdt.wdtc.core.utils.StringUtils.cleanStrInString
import org.wdt.wdtc.core.utils.URLUtils.getRedirectUrl
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import org.wdt.wdtc.core.utils.ZipUtils.unZipBySpecifyFile
import java.io.*
import java.util.jar.JarInputStream
import java.util.regex.Matcher
import java.util.regex.Pattern

// TODO Repair and optimization Forge install
class ForgeInstallTask(launcher: Launcher, forgeVersion: String?) : ForgeDownloadInfo(launcher, forgeVersion),
	InstallTaskInterface {
	private val config: DefaultGameConfig.Config
	private val logmaker = ForgeInstallTask::class.java.getWdtcLogger()

	init {
		config = launcher.gameConfig.config!!
	}

	constructor(launcher: Launcher, versionJsonObjectInterface: VersionJsonObjectInterface) : this(
		launcher,
		versionJsonObjectInterface.versionNumber
	)

	@Throws(IOException::class)
	fun getCommandLine(index: Int): String {
		val jsonObject = installPrefileJSONObject.getJsonArray("processors").getJsonObject(index)
		val commandLineBuilder = StringBuilder()
		commandLineBuilder.append(config.javaPath).append(" -cp ")
		val jarList = jsonObject.getJsonArray("classpath")
		for (i in 0 until jarList.size()) {
			val Jar = DependencyDownload(jarList.getString(i))
			Jar.downloadPath = launcher.gameLibraryDirectory
			commandLineBuilder.append(FilenameUtils.separatorsToWindows(Jar.libraryFilePath)).append(";")
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
	fun DownloadClientText() {
		var TxtPath: DefaultDependency? = null
		val DataObject = installPrefileJSONObject.getJsonObject("data")
		if (DataObject.has("MOJMAPS")) {
			val matcher = getMiddleBracket(DataObject.getJsonObject("MOJMAPS").getString("client"))
			if (matcher.find()) {
				TxtPath = DefaultDependency(matcher.group(1))
			}
		} else {
			val matcher = getMiddleBracket(DataObject.getJsonObject("MAPPINGS").getString("client"))
			if (matcher.find()) {
				TxtPath = DefaultDependency(matcher.group(1))
			}
		}
		var TxtUrl: String = launcher.versionJson.readFileToJsonObject().getJsonObject("downloads")
			.getJsonObject("client_mappings").getString("url")
		if (isNotOfficialDownloadSource) {
			TxtUrl = getRedirectUrl(TxtUrl.replace(pistonDataMojang, source.dataUrl))
		}
		if (TxtPath != null) {
			startDownloadTask(TxtUrl, File(launcher.gameLibraryDirectory, TxtPath.formJar()))
		}
	}

	@Throws(IOException::class)
	fun InstallForge() {
		val objects = installPrefileJSONObject.getJsonArray("processors")
		for (i in 0 until objects.size()) {
			val TaskJson = objects.getJsonObject(i)
			if (TaskJson.has("sides")) {
				if (TaskJson.getJsonArray("sides").getString(0) == "client") {
					startRunCommand(i)
				}
			} else {
				if (TaskJson.getJsonArray("args").getString(1) != "DOWNLOAD_MOJMAPS") {
					startRunCommand(i)
				}
			}
		}
	}

	val clientLzmaPath: File
		get() = File(wdtcCache, "/data/client.lzma")


	fun cleanString(str: String): String {
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

	fun getMiddleBracket(args: String?): Matcher {
		return Pattern.compile("\\[(.+)]").matcher(args)
	}

	fun getLargeBracket(args: String?): Matcher {
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
		val patches: MutableList<JsonObject> = ArrayList()
		patches.add(JsonUtils.getJsonObject(launcher.versionJson))
		patches.add(JsonUtils.getJsonObject(forgeVersionJsonFile))
		versionJsonObject.patches = patches
		launcher.putToVersionJson(versionJsonObject)
	}

	@Throws(IOException::class)
	override fun afterDownloadTask() {
		startDownloadForgeLibraryFile(installProfileFile)
		DownloadClientText()
		InstallForge()
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
