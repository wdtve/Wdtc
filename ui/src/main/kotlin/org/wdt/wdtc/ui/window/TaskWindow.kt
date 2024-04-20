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
import org.wdt.wdtc.core.openapi.manger.TaskKind
import org.wdt.wdtc.core.openapi.manger.TaskManger
import org.wdt.wdtc.core.openapi.utils.*
import java.util.*

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
				val updateUI = javafxScope.launch("update ui".toCoroutineName(), CoroutineStart.LAZY) {
					progressindicator.progress = 1.0
					progressbar.progress = 1.0
					taskList.remove(it)
					taskQuantity += 1
				}
				launchScope("Run task") {
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
				val taskPane = AnchorPane().apply {
					setPrefSize(398.0, 43.0)
					styleClass.add("BlackBorder")
					children.addAll(name, progressbar, progressindicator)
				}
				
				sonPane.children.add(taskPane)
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
	
	private fun monitorJob(job: Job) {
		timer("cancel job", 100) {
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