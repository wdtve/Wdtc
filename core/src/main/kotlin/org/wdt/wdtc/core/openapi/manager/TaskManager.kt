package org.wdt.wdtc.core.openapi.manager

enum class TaskKind {
	FUNCTION, COROUTINE
}

abstract class AbstractTask(
	val taskName: String? = null, val kind: TaskKind? = null
) {
	abstract suspend fun start()
}

class CoroutineTask(
	taskName: String, private val action: suspend () -> Unit
) : AbstractTask(taskName, TaskKind.COROUTINE) {
	
	override suspend fun start() {
		action()
	}
	
}

class CommonTask(
	taskName: String, private val action: () -> Unit
) : AbstractTask(taskName, TaskKind.FUNCTION) {
	override suspend fun start() {
		action()
	}
}

interface TaskExecutor {
	fun before()
	suspend fun run(task: AbstractTask)
	fun after()
}

interface RunTaskExecutor {
	suspend fun doExecutor()
}

suspend fun doExecutor(runTaskExecutor: RunTaskExecutor) {
	runTaskExecutor.doExecutor()
}


fun createCoroutineTask(name: String, action: suspend () -> Unit): CoroutineTask {
	return CoroutineTask(name, action)
}
