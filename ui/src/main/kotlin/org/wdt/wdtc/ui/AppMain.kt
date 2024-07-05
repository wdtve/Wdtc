package org.wdt.wdtc.ui

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.image.Image
import javafx.stage.Stage
import kotlinx.coroutines.Dispatchers
import org.wdt.wdtc.core.openapi.manager.currentSetting
import org.wdt.wdtc.core.openapi.manager.isDebug
import org.wdt.wdtc.core.openapi.manager.saveSettingToFile
import org.wdt.wdtc.core.openapi.utils.*
import org.wdt.wdtc.ui.window.*

class AppMain : Application() {
	override fun start(mainStage: Stage) {
		if (!isOnline) logmaker.warning("当前无网络连接,下载功能无法正常使用!")
		val stage = mainStage.apply {
			title = if (isOnline) windowsTitle else getWindowsTitle("无网络")
			minWidth = windowsWidht
			minHeight = windowsHeight
			val size = getSizeManger().also {
				it.setWindwosSize()
			}
			icons.add(Image("assets/icon/ico.jpg"))
			isResizable = isDebug
			setOnCloseRequest {
				logmaker.info(size)
				Dispatchers.Default.launch {
					tryCatching {
						runOnIO {
							currentSetting.saveSettingToFile {
								windowsWidth = width
								windowsHeight = height
							}
						}
					}
					runOnJavaFx {
						Platform.exit()
					}
				}
			}
		}
		launchOnJavaFx {
			stage.also {
				HomeWindow().setHome(it)
			}.show()
			logmaker.info("Window Show")
		}
		
	}
}
