package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.wdt.wdtc.core.openapi.manager.CoroutineTask
import org.wdt.wdtc.core.openapi.utils.launch
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.core.openapi.utils.timer
import java.util.*

class TaskWindow(
	private val mainStage: Stage, private val taskList: MutableList<CoroutineTask> = LinkedList()
) {
	private var taskQuantity = 0
	private var runTask = true
	
	fun showTaskStage() {
		val sonStage = Stage().apply {
			width = 400.0
			height = 400.0
			isResizable = false
			initOwner(mainStage)
			initStyle(StageStyle.UNDECORATED)
		}
		val sonPane = VBox().apply {
			setPrefSize(398.0, 311.0)
		}
		val taskListPane = ScrollPane().apply {
			setTopGrid()
			setRightAnchor(0.0)
			setPrefSize(400.0, 311.0)
			content = sonPane
		}
		
		launchOnJavaFx {
			taskList.forEach {
				sonPane.children.add(getTaskPane(it))
			}
		}
		val tips = Label("tips").apply {
			layoutX = 14.0
			layoutY = 375.0
			timer("Set tips", 1500) {
				launchOnJavaFx {
					text = "Tips:${getTips(15)}"
				}
				if (!runTask) cancel()
			}
		}
		val closeButton = JFXButton("取消").apply {
			layoutX = 331.0
			layoutY = 370.0
			styleClass.add("BlackBorder")
			onAction = EventHandler {
				runTask = false
				sonStage.close()
			}
		}
		AnchorPane().apply {
			setPrefSize(400.0, 400.0)
			setStylesheets()
			children.addAll(taskListPane, tips, closeButton)
		}.also {
			sonStage.run {
				scene = Scene(it)
				show()
			}
		}
		timer("close window", 500) {
			if (taskList.isEmpty()) {
				launchOnJavaFx {
					sonStage.close()
				}
				logmaker.info("all tasks finish, quantity $taskQuantity")
				cancel()
			}
		}
	}
	
	private fun getTaskPane(task: CoroutineTask): AnchorPane {
		val progressbar = ProgressBar().apply {
			layoutX = 63.0
			layoutY = 15.0
			prefHeight = 18.0
			prefWidth = 286.0
		}
		val progressindicator = ProgressIndicator().apply {
			layoutX = 362.0
			layoutY = 8.0
			prefHeight = 25.0
			prefWidth = 25.0
		}
		val name = Label("${task.taskName}:").apply {
			layoutX = 15.0
			layoutY = 15.0
		}
		val updateUI = launchOnJavaFx(CoroutineStart.LAZY) {
			progressindicator.progress = 1.0
			progressbar.progress = 1.0
			taskList.remove(task)
			taskQuantity += 1
		}
		
		dispatcher.launch {
			task.start()
			updateUI.start()
		}.monitorJob()
		
		return AnchorPane().apply {
			setPrefSize(398.0, 43.0)
			styleClass.add("BlackBorder")
			children.addAll(name, progressbar, progressindicator)
		}
	}
	
	private fun Job.monitorJob() {
		timer("cancel job", 100) {
			if (!runTask) {
				this@monitorJob.cancel()
				cancel()
			}
			if (taskList.isEmpty()) {
				cancel()
			}
		}
	}
	
	companion object {
		@OptIn(ExperimentalCoroutinesApi::class)
		val dispatcher = Dispatchers.IO.limitedParallelism(1)
	}
	
}