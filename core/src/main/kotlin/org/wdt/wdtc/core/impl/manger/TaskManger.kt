package org.wdt.wdtc.core.impl.manger

import kotlinx.coroutines.launch
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.createDirectories
import org.wdt.utils.io.deleteDirectory
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.impl.download.game.DownloadVersionGameFile
import org.wdt.wdtc.core.impl.plugins.impl.runStartupActions
import org.wdt.wdtc.core.openapi.auth.UsersList
import org.wdt.wdtc.core.openapi.auth.serializeUsersListGsonBuilder
import org.wdt.wdtc.core.openapi.game.VersionsList
import org.wdt.wdtc.core.openapi.manager.*
import org.wdt.wdtc.core.openapi.utils.*
import org.wdt.wdtc.core.openapi.utils.gson.serializeVersionGson
import org.wdt.wdtc.core.openapi.utils.gson.serializeVersionsListGson
import java.net.URL

fun ckeckRunEnvironment() {
	require(!isMacos) { "Wdtc cannot run on macos!" }
	if (isLinux) {
		logmaker.warning("Wdtc not recommended to run on Linux")
	}
	if (wdtcConfig.isFileNotExists()) {
		logmaker.info("First run, init config")
	}
}

fun createNeedDirectories() {
	wdtcConfig.createDirectories()
	wdtcCache.createDirectories()
	wdtcUser.createDirectories()
	wdtcDependencies.createDirectories()
	userAsste.createDirectories()
	pluginsDirectory.createDirectories()
}

suspend fun runStartUpTask() = runOnIO {
	launch {
		IOUtils.copy(
			getResourceAsStream("/assets/readme.txt"),
			wdtcConfig.resolve("readme.txt").outputStream()
		)
	}
	userListFile.runWhen({ isFileNotExists() }) {
		writeObjectToFile(serializeUsersListGsonBuilder) {
			UsersList()
		}
	}
	settingFile.runWhen({ isFileNotExists() }) {
		writeObjectToFile(serializeVersionGson) { Setting() }
	}
	versionsJson.runWhen({ isFileNotExists() }) {
		writeObjectToFile(serializeVersionsListGson) { VersionsList() }
	}
	launch {
		writeConfigJsonToAllVersion()
		ckeckVersionsList()
	}
	
	launch {
		val url = URL("https://maven.aliyun.com/repository/public/org/glavo/llvmpipe-loader/1.0/llvmpipe-loader-1.0.jar")
		llbmpipeLoader.let { file ->
			createDownloadTask(url.toFileData()) { url to file }.doExecutor()
		}
	}
	if (versionManifestFile.isFileNotExists()) {
		launch { DownloadVersionGameFile.startDownloadVersionManifestJsonFile() }
	}
	try {
		runStartupActions()
	} catch (e: Throwable) {
		logmaker.error(e)
	}
}

fun removeConfigDirectory(boolean: Boolean) {
	if (boolean) {
		wdtcConfig.deleteDirectory()
		GameDirectoryManager.DEFAULT_GAME_DIRECTORY.currentDownlaodedVersionList.forEach {
			it.versionConfigFile.delete()
		}
		
	}
}

