package org.wdt.wdtc.core.impl.launch

import org.wdt.utils.io.isFileNotExists
import org.wdt.utils.io.writeStringToFile
import org.wdt.wdtc.core.impl.download.InstallGameVersion
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manager.*
import org.wdt.wdtc.core.openapi.utils.info
import org.wdt.wdtc.core.openapi.utils.logmaker

class LaunchGame(
	private val version: Version
) {
	
	private val launchTaskProcess: ProcessBuilder
		get() = if (currentSetting.console) {
			ProcessBuilder("cmd.exe", "/C", "start", starterBat.canonicalPath)
		} else {
			ProcessBuilder(starterBat.canonicalPath)
		}.directory(version.versionDirectory)
	
	private fun beforLaunchTask() {
		if (!currentSetting.chineseLanguage) return
		version.gameOptionsFile.run {
			if (isFileNotExists()) {
				buildString {
					append("forceUnicodeFont:true").appendLine()
					append("guiScale:2").appendLine()
					append("lang:zh_cn").appendLine()
					append("autoJump:false").appendLine()
				}.also {
					writeStringToFile(it)
				}
			}
		}
	}
	
	// TODO: Optimize lag
	private fun getFullTaskList(
		action: LaunchAction
	): MutableList<CoroutineTask> = mutableListOf<CoroutineTask>().apply {
		
		add(createCoroutineTask("启动前配置") {
			beforLaunchTask()
		})
		
		add(createCoroutineTask("补全文件") {
			logmaker.info("Start Download")
			InstallGameVersion(version, false).run {
				startInstallGame()
			}
			logmaker.info("Downloaded Finish")
		})
		
		add(createCoroutineTask("写入启动脚本") {
			logmaker.info("Write Start Script")
			val command = buildString {
				append(GameJvmCommand(version).getCommand())
				append(GameCLICommand(version).getCommand())
			}.also {
				starterBat.writeStringToFile(it)
			}
			logmaker.info(command)
			logmaker.info("Write Start Script Finish")
		})
		
		add(createCoroutineTask("运行脚本") {
			logmaker.info("Start Run Start Script,Console:${currentSetting.console}")
			version.run {
				logmaker.info("Launch Version: $versionNumber - $kind")
				logmaker.info(gameConfig.config)
			}
			LaunchProcess(launchTaskProcess, action).run {
				launchGame()
			}
		})
	}
	
	private fun getSimpleTaskList(action: LaunchAction): MutableList<CoroutineTask> {
		return mutableListOf(createCoroutineTask("启动游戏") {
			getFullTaskList(action).forEach {
				it.start()
			}
		})
	}
	
	fun getTaskList(action: LaunchAction = DefaultLaunchAction): MutableList<CoroutineTask> {
		return if (isLowMode) getSimpleTaskList(action) else getFullTaskList(action)
	}
}
