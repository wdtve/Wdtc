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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.wdt.wdtc.core.manger.TaskKind
import org.wdt.wdtc.core.manger.TaskManger
import org.wdt.wdtc.core.utils.*
import java.time.Instant
import java.util.*
import kotlin.concurrent.timer

class TaskWindow(
	private val mainStage: Stage, private val taskList: MutableList<TaskManger> = LinkedList()
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
		val parentPane = AnchorPane().apply {
			setPrefSize(400.0, 400.0)
			setStylesheets()
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
		
		javafxCoroutineScope.launch {
			
			taskList.forEach {
				val taskPane = AnchorPane().apply {
					setPrefSize(398.0, 43.0)
					styleClass.add("BlackBorder")
				}
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
				val name = Label("${it.actionName}:").apply {
					layoutX = 15.0
					layoutY = 15.0
				}
				val updateUI = javafxCoroutineScope.launch("update ui".toCoroutineName(), CoroutineStart.LAZY) {
					progressindicator.progress = 1.0
					progressbar.progress = 1.0
					taskList.remove(it)
					taskQuantity += 1
				}
				scwn("Run task") {
					it.run {
						if (actionKind == TaskKind.FUNCTION) {
							action.noNull().invoke()
							updateUI.start()
						} else if (actionKind == TaskKind.COROUTINES) {
							coroutinesAction.noNull().run {
								start()
								monitorJob(this)
								join()
								updateUI.start()
							}
						}
					}
				}
				taskPane.children.addAll(name, progressbar, progressindicator)
				sonPane.children.add(taskPane)
			}
		}
		val tips = Label("tips").apply {
			layoutX = 14.0
			layoutY = 375.0
			timer("Set tips", daemon = true, startAt = Date.from(Instant.now()), period = 1500) {
				javafxCoroutineScope.launch("Set tips".toCoroutineName()) {
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
		parentPane.children.addAll(taskListPane, tips, closeButton)
		sonStage.run {
			scene = Scene(parentPane)
			show()
		}
		timer("close task", daemon = true, startAt = Date.from(Instant.now()), period = 500) {
			if (taskList.isEmpty()) {
				javafxCoroutineScope.launch {
					sonStage.close()
				}
				logmaker.info("all tasks finish, quantity $taskQuantity")
				cancel()
			}
		}
	}
	
	private fun monitorJob(job: Job) {
		timer("cancel task", daemon = true, startAt = Date.from(Instant.now()), period = 100) {
			if (!runTask) {
				job.cancel()
				cancel()
			}
			if (taskList.isEmpty()) {
				cancel()
			}
		}
	}
	
	companion object {
		val taskPool = executorCoroutineScope(1, "Task pool")
	}
}