package org.wdt.wdtc.core.download.forge

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.wdt.utils.gson.*
import org.wdt.wdtc.core.download.game.DownloadGameRuntime
import org.wdt.wdtc.core.download.infterface.ModInstallTaskInterface
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.launch.GameRuntimeFile
import org.wdt.wdtc.core.manger.*
import org.wdt.wdtc.core.utils.*
import java.io.*

// TODO Repair and optimization Forge install
class ForgeInstallTask(version: Version, forgeVersion: String) :
	ForgeDownloadInfo(version, forgeVersion), ModInstallTaskInterface {
	
	private val config: GameConfig.Config = version.gameConfig.config
	
	
	constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) : this(
		version,
		versionsJsonObjectInterface.versionNumber
	)
	
	@Throws(IOException::class)
	fun getCommandLine(index: Int): String {
		val installProfile = newForgeInstallProfileJsonObject.processors[index]
		return buildString {
			append(config.javaPath).append(" -cp ")
			installProfile.classpath.forEach {
				it.toGameRuntimeDependency.run {
					append(libraryFilePath).append(";")
				}
			}
			installProfile.libraryName.toGameRuntimeDependency.run {
				libraryFile.getJarManifestInfo("Main-Class").noNull().let {
					append(libraryFilePath).append(STRING_SPACE).append(it).append(" ")
				}
			}
			installProfile.args.forEach { arg ->
				if (arg.startsWith("--")) {
					append(arg).append(STRING_SPACE)
				} else if (arg.startendWith("{", "}")) {
					if (arg == "{BINPATCH}") {
						append(clientLzmaFile.canonicalPath)
					} else {
						dataMap[arg].noNull().let {
							if (it.isRuntime) append(it.client.toGameRuntimeDependency.libraryFilePath)
							else append(it.client)
						}
					}
				}
			}
			
		}
	}
	
	private val dataMap: Map<String, ForgeInstallProfileJsonObject.ForgeDataObject>
		get() {
			return hashMapOf<String, ForgeInstallProfileJsonObject.ForgeDataObject>().apply {
				forgeInstallProfileJsonObject.getJsonObject("data").asMap().forEach { (key, value) ->
					put("{$key}", value.asJsonObject.parseObject())
				}
				put(
					"{MINECRAFT_JAR}",
					ForgeInstallProfileJsonObject.ForgeDataObject(
						version.versionJar.canonicalPath,
						version.versionJar.canonicalPath
					)
				)
				put("{SIDE}", ForgeInstallProfileJsonObject.ForgeDataObject("client", "server"))
			}
		}
	
	
	@Throws(IOException::class)
	fun downloadClientText() {
		val txtPath = newForgeInstallProfileJsonObject.data.let {
			if (it.mojmaps != null) it.mojmaps.client.toGameRuntimeDependency
			else if (it.mappings != null) it.mappings.client.toGameRuntimeDependency
			else null
		}.noNull()
		val txtUrl = version.gameVersionJsonObject.downloads.clientMappings.url.run {
			if (isNotOfficialDownloadSource)
				this.toString().replace(pistonDataMojang, currentDownloadSource.dataUrl).toURL().getRedirectUrl().toURL()
			else this
		}
		startDownloadTask(txtUrl, File(version.gameLibraryDirectory, txtPath.formJar()))
	}
	
	@Throws(IOException::class)
	fun startInstallForge() {
		newForgeInstallProfileJsonObject.processors.let {
			repeat(it.size) { index ->
				it[index].run {
					if (sides != null && sides.first() == "client") {
						startRunCommand(index)
					} else if (args[1] != "DOWNLOAD_MOJMAPS") {
						startRunCommand(index)
					}
				}
			}
		}
	}
	
	private val clientLzmaFile: File = File(wdtcCache, "data/client.lzma")
	
	
	@Throws(IOException::class)
	private fun startRunCommand(i: Int) {
		getCommandLine(i).let {
			logmaker.info("Command Line:$it")
			Runtime.getRuntime().exec(arrayOf("cmd", "/c", it)).run {
				val bufferedReader = BufferedReader(InputStreamReader(inputStream, "GBK"))
				var line: String?
				while (bufferedReader.readLine().also { info -> line = info } != null) {
					logmaker.info(line)
				}
				destroy()
			}
		}
	}
	
	@Throws(IOException::class)
	override fun overwriteVersionJson() {
		version.run {
			gameVersionJsonObject.apply {
				newForgeVersionJsonObject.let {
					this.arguments.run {
						jvmList?.add(it.arguments.jvmList)
						gameList?.add(it.arguments.gameList)
					}
					this.libraries.addAll(it.libraries)
				}
			}.putToVersionJson()
		}
	}
	
	@Throws(IOException::class)
	override fun writeVersionJsonPatches() {
		version.run {
			gameVersionJsonObject.apply {
				patches = mutableListOf(
					versionJson.readFileToJsonObject(),
					forgeVersionJsonFile.readFileToJsonObject()
				)
			}.putToVersionJson()
		}
	}
	
	@Throws(IOException::class)
	override fun afterDownloadTask() = runBlocking(Dispatchers.IO) {
		launch { startDownloadForgeLibraryFile() }
		launch { downloadClientText() }
		startInstallForge()
	}
	
	override fun beforInstallTask(): Unit = runBlocking(Dispatchers.IO) {
		startDownloadInstallJar()
		launch { unZipToInstallProfile() }
		launch { unZipToForgeVersionJson() }
		launch { unZipBySpecifyFile(forgeInstallJarFile, clientLzmaFile) }
	}
	
	private val String.toGameRuntimeDependency: GameRuntimeDependency
		get() {
			return GameRuntimeDependency(
				this.formatString.run {
					if (isHasFileExtension) cleanStrInString("@$getFileExtension") else this
				}
			).apply {
				if (isHasFileExtension) fileExtension = this@toGameRuntimeDependency.getFileExtension
				libraryDirectory = this@ForgeInstallTask.version.gameLibraryDirectory
			}
		}
	
	private val String.isHasFileExtension: Boolean
		get() = this.indexOf("@") == -1
	
	private val String.getFileExtension: String
		get() = this.substringAfter("@")
	
	private val String.formatString
		get() = this.cleanStrInString("[").cleanStrInString("]").cleanStrInString("{").cleanStrInString("}")
	
	
	@Throws(IOException::class)
	fun startDownloadForgeLibraryFile() {
		newForgeVersionJsonObject.libraries.run {
			val speed = ProgressManger(size).apply {
				coroutineScope = executorCoroutineScope(name = "Download forge library file")
			}
			forEach {
				DownloadGameRuntime(version, GameRuntimeFile(it), speed).run { start() }
			}
			speed.await()
		}
	}
}
