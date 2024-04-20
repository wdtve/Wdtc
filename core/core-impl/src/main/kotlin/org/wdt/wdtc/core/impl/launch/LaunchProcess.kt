package org.wdt.wdtc.core.impl.launch

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.core.openapi.utils.runOnIO
import java.io.InputStream
import java.nio.charset.Charset
import java.util.regex.Pattern
import kotlin.system.measureTimeMillis

class LaunchProcess(
	private val process: ProcessBuilder,
	var printInfo: (String) -> Unit
) {
	private val builder = StringBuilder()
	suspend fun startLaunchGame() {
		val runtime = measureTimeMillis {
			runOnIO {
				process.start().run {
					launch(CoroutineName("Read info inputStream")) { inputStream.use { it.getRunInfo() } }
					launch(CoroutineName("Read error inputStream")) { errorStream.use { it.getRunInfo() } }
				}
			}
		}
		logmaker.info("Game over. Game run time: $runtime ms")
	}
	
	private fun InputStream.getRunInfo() {
		val thread = Thread.currentThread()
		val reader = reader(Charset.forName("GBK")).buffered()
		var line: String
		while (reader.readLine().also { line = it } != null) {
			if (thread.isInterrupted) {
				launchErrorTask()
				return
			} else {
				line.let {
					if (Pattern.compile("FATAL").matcher(it).find()) {
						println(it)
						builder.append(it)
						thread.interrupt()
					} else {
						println(it)
						builder.append(it)
					}
				}
			}
		}
	}
	
	private fun launchErrorTask() {
		printInfo("启动失败:\n${builder}")
	}
}
