package org.wdt.wdtc.core.game.config

import com.google.gson.annotations.SerializedName
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.utils.JavaUtils.runJavaHome


class DefaultGameConfig(launcher: Launcher) {
	@SerializedName("info")
	var info: VersionInfo? = null

	@SerializedName("config")
	var config: Config? = null

	init {
		info = launcher.versionInfo
		config = Config()
	}


	override fun toString(): String {
		return "DefaultGameConfig(info=$info, config=$config)"
	}

	class Config {
		@SerializedName("RunningMemory")
		var memory: Int

		@SerializedName("JavaPath")
		var javaPath: String

		@SerializedName("WindowWidth")
		var width: Int

		@SerializedName("WindowHeight")
		var hight: Int

		constructor() {
			memory = 1024
			javaPath = runJavaHome
			width = 1000
			hight = 618
		}

		constructor(memory: Int, javaPath: String, width: Int, hight: Int) {
			this.memory = memory
			this.javaPath = javaPath
			this.width = width
			this.hight = hight
		}

		override fun toString(): String {
			return "Config(Memory=$memory, JavaPath='$javaPath', width=$width, hight=$hight)"
		}

	}

}
