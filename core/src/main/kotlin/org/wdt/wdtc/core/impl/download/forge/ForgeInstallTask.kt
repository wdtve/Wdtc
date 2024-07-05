package org.wdt.wdtc.core.impl.download.forge

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.openapi.download.interfaces.ModInstallTaskInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.game.GameConfig
import org.wdt.wdtc.core.openapi.game.GameRuntimeDependency
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manager.*
import org.wdt.wdtc.core.openapi.utils.*
import java.io.File

// TODO Repair and optimization Forge install
class ForgeInstallTask(private val version: ForgeVersionImpl) : ModInstallTaskInterface {
	
	private val config: GameConfig.Config = version.gameConfig.config
	
	
	constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) : this(
		ForgeVersionImpl(version, versionsJsonObjectInterface.versionNumber)
	)
	
	
	private fun getCommandLine(index: Int): String {
		val profile = version.forgeInstallProfileJsonObject.processors[index]
		return buildString {
			append(config.javaPath).append(" -cp ")
			profile.classpath.forEach {
				it.toGameRuntimeDependency().run {
					append(libraryFilePath).append(";")
				}
			}
			profile.libraryName.toGameRuntimeDependency().run {
				libraryFile.getJarManifestInfo("Main-Class").noNull().let {
					append(libraryFilePath).append(STRING_SPACE).append(it).append(" ")
				}
			}
			profile.args.forEach { arg ->
				if (arg.startsWith("--")) {
					append(arg).append(STRING_SPACE)
				} else if (arg.startendWith("{", "}")) {
					if (arg == "{BINPATCH}") {
						append(clientLzmaFile.canonicalPath)
					} else {
						dataMap[arg].noNull().let {
							if (it.isRuntime) append(it.client.toGameRuntimeDependency().libraryFilePath)
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
				version.forgeInstallProfileFile.readFileToJsonObject().getJsonObject("data")
					.asMap()
					.forEach { (key, value) ->
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
	
	
	private suspend fun downloadClientText() {
		val txtPath = version.forgeInstallProfileJsonObject.data.let {
			if (it.mojmaps != null) it.mojmaps.client.toGameRuntimeDependency()
			else if (it.mappings != null) it.mappings.client.toGameRuntimeDependency()
			else null
		}.noNull()
		val txtUrl = version.gameVersionJsonObject.downloads.clientMappings.url.let {
			if (isNotOfficialDownloadSource)
				it.toString().replace(pistonDataMojang, currentDownloadSource.dataURL).toURL().getRedirectUrl().toURL()
			else it
		}
		startDownloadTask(txtUrl to version.gameLibraryDirectory.resolve(txtPath.formJar()))
	}
	
	private suspend fun startInstallForge() {
		version.forgeInstallProfileJsonObject.processors.forEachIndexed { i, processor ->
			processor.let {
				if (it.sides != null && it.sides.first() == "client") {
					startRunCommand(i)
				} else if (it.args.first() != "DOWNLOAD_MOJMAPS") {
					startRunCommand(i)
				}
			}
		}
	}
	
	
	private val clientLzmaFile: File = File(wdtcCache, "data/client.lzma")
	
	
	private suspend fun startRunCommand(i: Int) {
		val command = getCommandLine(i).let {
			logmaker.info("Command Line:$it")
			ProcessBuilder("cmd.exe", "/c", it)
		}
		runOnIO {
			command.start().run {
				inputStream.reader(charset("GBK")).run {
					launch {
						while (readlnOrNull().also { logmaker.info(it) } != null);
					}
				}
			}
		}
	}
	
	override suspend fun overwriteVersionJson() {
		version.run {
			gameVersionJsonObject.apply {
				forgeVersionJsonObject.let {
					this.arguments.run {
						it.arguments.jvmList?.forEach { element ->
							jvmList?.add(element)
						}
						it.arguments.gameList?.forEach { element ->
							gameList?.add(element)
						}
					}
					this.libraries.addAll(it.libraries)
				}
			}.putToVersionJson()
		}
	}
	
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
	
	override suspend fun afterDownloadTask() = coroutineScope {
		launch { startDownloadForgeLibraryFile() }
		launch { downloadClientText() }
		startInstallForge()
	}
	
	override suspend fun beforInstallTask() {
		coroutineScope {
			version.forgeInstallJarFile.let {
				startDownloadTask(version.forgeInstallJarUrl, it)
				launch {
					unZipToFile(it, version.forgeInstallProfileFile, "install_profile.json")
				}
				launch { unZipToFile(it, version.forgeVersionJsonFile, "version.json") }
				launch { unZipBySpecifyFile(it, clientLzmaFile) }
			}
		}
	}
	
	private fun String.toGameRuntimeDependency(): GameRuntimeDependency {
		return GameRuntimeDependency(
			this.formatString.let {
				if (isHasFileExtension) cleanStrInString("@$getFileExtension") else it
			}
		).apply {
			if (isHasFileExtension) fileExtension = getFileExtension
			libraryDirectory = this@ForgeInstallTask.version.gameLibraryDirectory
		}
	}
	
	private val String.isHasFileExtension: Boolean
		get() = this.indexOf("@") == -1
	
	private val String.getFileExtension: String
		get() = this.substringAfter("@")
	
	private val String.formatString
		get() = this.cleanStrInString("[").cleanStrInString("]").cleanStrInString("{").cleanStrInString("}")
	
	
	private fun startDownloadForgeLibraryFile() {
		version.forgeVersionJsonObject.libraries.run {
			val speed = MultithreadingManager(size).apply {
				context = newThreadPoolContext(name = "Download forge library file")
			}
			speed.await()
		}
	}
}
