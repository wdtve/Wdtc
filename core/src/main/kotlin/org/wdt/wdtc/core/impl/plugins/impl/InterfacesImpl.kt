package org.wdt.wdtc.core.impl.plugins.impl

import org.wdt.wdtc.core.openapi.plugins.config.Plugin
import org.wdt.wdtc.core.openapi.plugins.interfaces.StartupAction
import org.wdt.wdtc.core.openapi.utils.noNull

fun Plugin.getStartupActions(): List<StartupAction> {
	return config.coreActions.actions.filter {
		it.action == "startupAction"
	}.map {
		(loader.loadClass(it.clazz).getDeclaredConstructor().newInstance() as? StartupAction).noNull()
	}
}
