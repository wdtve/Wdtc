package org.wdt.wdtc.core.manger

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.createDirectories
import org.wdt.utils.io.deleteDirectory
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.auth.UsersList
import org.wdt.wdtc.core.auth.serializeUsersListGsonBuilder
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile
import org.wdt.wdtc.core.game.VersionsList
import org.wdt.wdtc.core.game.ckeckVersionsList
import org.wdt.wdtc.core.utils.*
import org.wdt.wdtc.core.utils.gson.serializeVersionGson
import org.wdt.wdtc.core.utils.gson.serializeVersionsListGson
import java.io.File

fun ckeckRunEnvironment() {
	if (isMacos) {
		error("Wdtc cannot run on macos!")
	}
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
	wdtcDependenciesDirectory.createDirectories()
	userAsste.createDirectories()
}

fun ckeckVMConfig() {
	if (isDebug) System.setProperty(CONFIG_PATH, "./")
	if (System.getProperty(CONFIG_PATH) == null) {
		System.setProperty(CONFIG_PATH, System.getProperty("user.home"))
	}
}


fun runStartUpTask() = runBlocking(Dispatchers.IO) {
	IOUtils.copy(
		getResourceAsStream("/assets/readme.txt"), File(wdtcConfig, "readme.txt").outputStream()
	)
	userListFile.run {
		if (isFileNotExists()) {
			writeObjectToFile(UsersList(), serializeUsersListGsonBuilder)
		}
	}
	settingFile.run {
		if (isFileNotExists()) {
			writeObjectToFile(Setting(), serializeVersionGson)
		}
	}
	versionsJson.run {
		if (isFileNotExists()) {
			writeObjectToFile(VersionsList(), serializeVersionsListGson)
		}
	}
	launch {
		writeConfigJsonToAllVersion()
		ckeckVersionsList()
	}
	
	launch {
		llbmpipeLoader.let { file ->
			"https://maven.aliyun.com/repository/public/org/glavo/llvmpipe-loader/1.0/llvmpipe-loader-1.0.jar".toURL().let {
				if (file.compareFile(it.toFileData())) {
					startDownloadTask(it, file)
				}
			}
		}
	}
	if (versionManifestFile.isFileNotExists()) {
		launch { DownloadVersionGameFile.startDownloadVersionManifestJsonFile() }
	}
	
}

fun removeConfigDirectory(boolean: Boolean) {
	if (boolean) {
		wdtcConfig.deleteDirectory()
		GameDirectoryManger(currentSetting.defaultGamePath).gameVersionList.run {
			forEach {
				it.versionConfigFile.delete()
			}
		}
	}
}

open class TaskManger(
	val actionName: String,
	val actionKind: TaskKind = TaskKind.FUNCTION,
	var coroutinesAction: Job? = null,
	var action: (() -> Unit)? = null
) {
	open fun start() {
		if (actionKind == TaskKind.FUNCTION) {
			action.noNull().invoke()
		} else {
			coroutinesAction.noNull().start()
		}
	}
	
	override fun toString(): String {
		return "TaskManger(actionName='$actionName', actionKind=$actionKind, coroutinesAction=$coroutinesAction, action=$action)"
	}
	
	
}

enum class TaskKind {
	FUNCTION, COROUTINES
}