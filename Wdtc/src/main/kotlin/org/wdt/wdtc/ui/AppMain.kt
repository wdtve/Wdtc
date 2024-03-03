package org.wdt.wdtc.ui

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.image.Image
import javafx.stage.Stage
import org.wdt.wdtc.core.manger.changeSettingToFile
import org.wdt.wdtc.core.manger.currentSetting
import org.wdt.wdtc.core.manger.isDebug
import org.wdt.wdtc.core.manger.putSettingToFile
import org.wdt.wdtc.core.utils.*
import org.wdt.wdtc.ui.window.*
import java.io.IOException

class AppMain : Application() {
	override fun start(mainStage: Stage) {
		try {
			if (!isOnline) logmaker.warning("当前无网络连接,下载功能无法正常使用!")
			mainStage.apply {
				title = if (isOnline) windowsTitle else getWindowsTitle("无网络")
				minWidth = windowsWidht
				minHeight = windowsHeight
				val size = getSizeManger().apply {
					setWindwosSize()
				}
				icons.add(Image("assets/icon/ico.jpg"))
				isResizable = isDebug
				setOnCloseRequest {
					logmaker.info(size)
					try {
						currentSetting.changeSettingToFile {
							windowsWidth = width
							windowsHeight = height
						}
					} catch (e: IOException) {
						setErrorWin(e)
					}
					Platform.exit()
				}
			}.run {
				HomeWindow().run { setHome(mainStage) }
				logmaker.info("Window Show")
				show()
			}
		} catch (e: Exception) {
			setErrorWin(e)
		}
	}
}
