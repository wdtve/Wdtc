package org.wdt.wdtc.core.impl.plugins.impl

import org.wdt.wdtc.core.impl.plugins.loader.PluginsLoader
import org.wdt.wdtc.core.openapi.utils.runOnPluginThread


suspend fun runStartupActions() {
	PluginsLoader.getPluginLoaders().forEach {
		it.runOnPluginThread {
			it.getStartupActions().forEach { action ->
				action.invoke()
			}
		}
	}
}