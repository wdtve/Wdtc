package org.wdt.wdtc.core.impl.plugins.loader

import org.wdt.wdtc.core.openapi.manger.wdtcDependenciesDirectory
import org.wdt.wdtc.core.openapi.plugins.config.Plugin
import org.wdt.wdtc.core.openapi.plugins.config.currentEnabledPlugins
import org.wdt.wdtc.core.openapi.plugins.loader.currentClassLoader
import org.wdt.wdtc.core.openapi.utils.associatePluginBy
import java.net.URLClassLoader

object PluginsLoader {
	
	private val plugins = currentEnabledPlugins.associatePluginBy { plugin ->
		
		val dependencies = plugin.pluginConfigFileObject.dependencies.map { library ->
			library.libraryName.apply {
				libraryDirectory = wdtcDependenciesDirectory
			}.libraryFile
		}
		
		val classPaths = mutableListOf(plugin.jarFile).plus(dependencies).map { file ->
			file.toURI().toURL()
		}.toTypedArray()
		
		URLClassLoader(classPaths, currentClassLoader)
	}
	
	fun getPluginLoaders(): List<Plugin> = plugins
}