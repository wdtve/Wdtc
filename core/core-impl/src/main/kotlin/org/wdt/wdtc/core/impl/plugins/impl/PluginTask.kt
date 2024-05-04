package org.wdt.wdtc.core.impl.plugins.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import org.wdt.wdtc.core.impl.plugins.loader.PluginsLoader
import org.wdt.wdtc.core.openapi.plugins.config.Plugin
import org.wdt.wdtc.core.openapi.utils.runOnPluginThread
import kotlin.coroutines.CoroutineContext


val Plugin.dispatcher: CoroutineDispatcher
	get() = PluginDispatcher(this)

class PluginDispatcher(
	private val plugin: Plugin
) : CoroutineDispatcher() {
	override fun dispatch(context: CoroutineContext, block: Runnable) {
		Dispatchers.IO.dispatch(context + plugin.context, block)
	}
}

suspend fun runStartupActions() {
	PluginsLoader.getPluginLoaders().forEach {
		it.runOnPluginThread {
			it.getStartupActions().forEach { action ->
				action.invoke()
			}
		}
	}
}