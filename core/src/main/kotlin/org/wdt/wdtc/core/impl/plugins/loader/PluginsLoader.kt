package org.wdt.wdtc.core.impl.plugins.loader

import org.wdt.wdtc.core.openapi.plugins.config.Plugin
import org.wdt.wdtc.core.openapi.plugins.config.currentEnabledPlugins
import org.wdt.wdtc.core.openapi.plugins.loader.PluginLoader

object PluginsLoader {
	
	private val plugins = currentEnabledPlugins.map {
		Plugin(it.pluginConfigFileObject, PluginLoader(it))
	}.toList()
	
	fun getPluginLoaders(): List<Plugin> = plugins
}