package org.wdt.wdtc.core.openapi.manger

import kotlinx.coroutines.Job
import org.wdt.wdtc.core.openapi.utils.noNull

open class TaskManger(
	val actionName: String,
	val actionKind: TaskKind = TaskKind.FUNCTION,
	var coroutinesAction: Job? = null,
	var action: (() -> Unit)? = null
) {
	open fun start() {
		if (actionKind == TaskKind.FUNCTION) {
			action.noNull().invoke()
		} else {
			coroutinesAction.noNull().start()
		}
	}
	
	final override fun toString(): String {
		return "TaskManger(actionName='$actionName', actionKind=$actionKind, coroutinesAction=$coroutinesAction, action=$action)"
	}
	
	
}

enum class TaskKind {
	FUNCTION, COROUTINES
}