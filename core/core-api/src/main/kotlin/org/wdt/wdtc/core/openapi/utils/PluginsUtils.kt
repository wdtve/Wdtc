@file:Suppress("NOTHING_TO_INLINE")

package org.wdt.wdtc.core.openapi.utils

import kotlinx.coroutines.*
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseJsonStreamToJsonObject
import org.wdt.wdtc.core.openapi.game.GameRuntimeDependency
import org.wdt.wdtc.core.openapi.game.LibraryObjectList
import org.wdt.wdtc.core.openapi.game.LibraryObjectList.Companion.generateLibraryObjectList
import org.wdt.wdtc.core.openapi.plugins.config.ActionMapObject
import org.wdt.wdtc.core.openapi.plugins.config.Plugin
import org.wdt.wdtc.core.openapi.plugins.config.PluginsObject
import org.wdt.wdtc.core.openapi.plugins.config.PluginsObjectSequence
import org.wdt.wdtc.core.openapi.plugins.interfaces.Action
import java.net.URLClassLoader


val points = getResourceAsStream("/assets/plugin/points.json").use { it.parseJsonStreamToJsonObject() }

suspend fun List<String>.generateLibraryObjectList(): LibraryObjectList {
	return map {
		GameRuntimeDependency(it)
	}.generateLibraryObjectList()
}

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
suspend fun <T> Plugin.runOnPluginThread(block: CoroutineScope.() -> T): T {
	return withContext(newSingleThreadContext(name)) {
		try {
			block()
		} catch (e: Throwable) {
			throw exceptionMessage(e = e)
		}
	}
}

fun PluginsObjectSequence.associatePluginBy(block: (PluginsObject) -> URLClassLoader): List<Plugin> {
	return map {
		Plugin(it.pluginConfigFileObject, block(it))
	}.toList()
}

fun generateActionMapObject(action: Action): ActionMapObject {
	val actionClass = points.getJsonObject("map").asMap().filterValues {
		action::class.java.interfaces.first().canonicalName == it.asString
	}.keys.first()
	return ActionMapObject(actionClass, action::class.java.canonicalName)
}

class PluginsException(
	plugin: Plugin, messages: String? = null, e: Throwable
) : RuntimeException(buildString {
	append("Exception on '${plugin.name}' plugin")
	messages.let {
		if (it != null) {
			append(": $it")
		}
	}
}, e)

inline fun Plugin.exceptionMessage(messages: String? = null, e: Throwable): PluginsException {
	return PluginsException(this, messages, e)
}
