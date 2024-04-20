package org.wdt.wdtc.core.impl.launch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import org.wdt.utils.io.isFileNotExists
import org.wdt.utils.io.writeStringToFile
import org.wdt.wdtc.core.impl.download.InstallGameVersion
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manger.*
import org.wdt.wdtc.core.openapi.utils.*
import java.util.*

class LaunchGame(
	private val version: Version,
	private val coroutineScope: CoroutineScope = ioCoroutineScope
) {
	private val launchTaskProcess: ProcessBuilder
		get() = if (currentSetting.console)
			ProcessBuilder("cmd.exe", "/C", "start", starterBat.canonicalPath).directory(version.versionDirectory)
		else
			ProcessBuilder(starterBat.canonicalPath).directory(version.versionDirectory)
	
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
		block: (String) -> Unit = { println(it) }
	): MutableList<TaskManger> = LinkedList<TaskManger>().apply {
		
		add(object : TaskManger("启动前配置", TaskKind.COROUTINES) {
			init {
				coroutinesAction =
					coroutineScope.launch("before launch task".toCoroutineName(), start = CoroutineStart.LAZY) {
						beforLaunchTask()
					}
			}
		})
		
		add(object : TaskManger("补全文件", TaskKind.COROUTINES) {
			init {
				coroutinesAction = coroutineScope.launch("Complete the file".toCoroutineName(), start = CoroutineStart.LAZY) {
					logmaker.info("Start Download")
					InstallGameVersion(version, false).run {
						startInstallGame()
					}
					logmaker.info("Downloaded Finish")
					
				}
			}
		}
		)
		
		add(object : TaskManger("写入启动脚本", TaskKind.COROUTINES) {
			init {
				coroutinesAction =
					coroutineScope.launch("Write start script".toCoroutineName(), start = CoroutineStart.LAZY) {
						logmaker.info("Write Start Script")
						val command = buildString {
							append(GameJvmCommand(version).getCommand())
							append(GameCLICommand(version).getCommand())
						}.also {
							starterBat.writeStringToFile(it)
						}
						logmaker.info(command)
						logmaker.info("Write Start Script Finish")
						
					}
			}
		})
		add(object : TaskManger("运行脚本", TaskKind.COROUTINES) {
			init {
				coroutinesAction = coroutineScope.launch("Run script".toCoroutineName(), start = CoroutineStart.LAZY) {
					logmaker.info("Start Run Start Script,Console:${currentSetting.console}")
					version.run {
						logmaker.info("Launch Version: $versionNumber - $kind")
						logmaker.info(gameConfig.config)
					}
					LaunchProcess(launchTaskProcess, block).run {
						startLaunchGame()
					}
				}
			}
		})
	}
	
	private fun getSimpleTaskList(block: (String) -> Unit): MutableList<TaskManger> {
		return mutableListOf(TaskManger("启动游戏", TaskKind.COROUTINES).apply {
			coroutinesAction = defaultCoroutineScope.launch("Run all game".toCoroutineName(), CoroutineStart.LAZY) {
				getFullTaskList(block).forEach {
					it.coroutinesAction.noNull().run {
						start()
						join()
					}
				}
			}
		})
	}
	
	fun getTaskList(block: (String) -> Unit): MutableList<TaskManger> {
		return if (isLowMode) getSimpleTaskList(block) else getFullTaskList(block)
	}
	
	companion object {
		val Version.launchGameTask
			get() = LaunchGame(this)
	}
}
