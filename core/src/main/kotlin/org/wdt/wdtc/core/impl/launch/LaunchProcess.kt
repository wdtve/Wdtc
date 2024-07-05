package org.wdt.wdtc.core.impl.launch

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.wdt.wdtc.core.openapi.utils.launchOnIO
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.core.openapi.utils.newThreadPoolContext
import org.wdt.wdtc.core.openapi.utils.runOnIO
import java.io.InputStream
import kotlin.system.measureTimeMillis

class LaunchProcess(
	private val builder: ProcessBuilder,
	private val action: LaunchAction
) {
	private val infos = StringBuilder()
	private var exit: Int = 0
	
	suspend fun launchGame() {
		val runtime = measureTimeMillis {
			exit = runOnIO {
				builder.start().also { process ->
					withContext(newThreadPoolContext(2, "read info lines")) {
						launch { process.inputStream.use { it.getRunInfo() } }
						launch { process.errorStream.use { it.getRunInfo() } }
					}
				}.waitFor()
			}
		}
		if (exit != 0) {
			launchOnIO { action.invoke(infos.toString()) }
		}
		logmaker.info("Game over. Game run time: $runtime ms,exit value: $exit.")
	}
	
	private fun InputStream.getRunInfo() {
		val thread = Thread.currentThread()
		reader(charset("GBK")).buffered().use {
			var line: String
			while (it.readLine().also { str -> line = str } != null) {
				if (thread.isInterrupted) {
					exit = -1
					return
				} else {
					val info = line.also { str ->
						infos.append(str).appendLine()
					}
					println(info)
				}
			}
		}
	}
}

interface LaunchAction {
	suspend fun invoke(infos: String)
}

object DefaultLaunchAction : LaunchAction {
	override suspend fun invoke(infos: String) {
		println(infos)
	}
}