@file:JvmName("Consoler") @file:Suppress("NOTHING_TO_INLINE")

package org.wdt.wdtc.ui.window

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.layout.*
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import org.wdt.utils.io.isFileExists
import org.wdt.utils.io.readFileToLine
import org.wdt.utils.io.readLines
import org.wdt.wdtc.core.openapi.manager.isDebug
import org.wdt.wdtc.core.openapi.manager.launcherVersion
import org.wdt.wdtc.core.openapi.manager.tipsFile
import org.wdt.wdtc.core.openapi.utils.getResourceAsStream
import org.wdt.wdtc.core.openapi.utils.launch
import org.wdt.wdtc.core.openapi.utils.noNull

//Form:https://www.bilibili.com/video/BV1EY411m7uZ
val wdtcBackground: Background = Image(
	getResourceAsStream("/assets/blackGround/BlackGround${System.getProperty("wdtc.ui.background", "0")}.jpg")
).let {
	Background(
		BackgroundImage(
			it, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize(
				windowsWidht, windowsHeight, false, false, true, true
			)
		)
	)
}


val cssFile: String
	get() = object {}.javaClass.getResource("/css/color.css").noNull().toString()

fun setCss(id: String, vararg pane: Region) {
	pane.forEach {
		it.styleClass.add(id)
	}
}

fun Pane.setStylesheets() {
	this.stylesheets.add(cssFile)
}

inline fun Node.setTopGrid() {
	setTopLowerLeft()
	this.setTopAnchor(0.0)
	this.setLeftAnchor(0.0)
}

inline fun Node.setTopLowerLeft() {
	this.setBottomAnchor(0.0)
	this.setLeftAnchor(0.0)
}

inline fun Node.setTopLowerRight() {
	this.setBottomAnchor(0.0)
	this.setRightAnchor(0.0)
}

fun getWindowsTitle(windowsName: String?): String {
	return String.format(if (isDebug) "Wdtc - Debug - %s - %s" else "Wdtc - %s - %s", launcherVersion, windowsName)
}

inline fun Node.setTopAnchor(double: Double) {
	AnchorPane.setTopAnchor(this, double)
}

inline fun Node.setBottomAnchor(double: Double) {
	AnchorPane.setBottomAnchor(this, double)
}

inline fun Node.setLeftAnchor(double: Double) {
	AnchorPane.setLeftAnchor(this, double)
}

inline fun Node.setRightAnchor(double: Double) {
	AnchorPane.setRightAnchor(this, double)
}

val windowsTitle: String
	get() = String.format(if (isDebug) "Wdtc - Debug - %s" else "Wdtc - %s", launcherVersion)

fun getTips(length: Int): String {
	return getTips().let {
		if (it.length < length) {
			it
		} else {
			getTips(length)
		}
	}
}

fun getTips(): String = getResourceAsStream("/assets/tips.txt").readLines().let {
	if (tipsFile.isFileExists()) it.plus(tipsFile.readFileToLine()) else it
}.let {
	it[it.indices.random()].replace("\$version_number", launcherVersion)
}

suspend fun <T> runOnJavaFx(block: suspend CoroutineScope.() -> T): T {
	return withContext(Dispatchers.JavaFx, block)
}

fun launchOnJavaFx(start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend CoroutineScope.() -> Unit): Job =
	javafxScope.launch(start = start, block = block)

val javafxScope = CoroutineScope(Dispatchers.JavaFx)

fun eventHandler(
	dispatcher: CoroutineDispatcher = Dispatchers.JavaFx, block: suspend CoroutineScope.() -> Unit
): EventHandler<ActionEvent> {
	return EventHandler {
		dispatcher.launch(block = block)
	}
}