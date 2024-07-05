@file:JvmName("PluginsLoader")

package org.wdt.wdtc.core.openapi.plugins.loader

import org.wdt.wdtc.core.openapi.manager.wdtcDependencies
import org.wdt.wdtc.core.openapi.plugins.config.PluginsObject
import java.net.URLClassLoader

val currentClassLoader: ClassLoader = ClassLoader.getSystemClassLoader()

class PluginLoader(
	plugin: PluginsObject
) : URLClassLoader(
	run {
		val dependencies = plugin.pluginConfigFileObject.dependencies.map { library ->
			library.libraryName.apply {
				libraryDirectory = wdtcDependencies
			}.libraryFile
		}
		
		listOf(plugin.jarFile).plus(dependencies).map { file ->
			file.toURI().toURL()
		}.toTypedArray()
		
	},
	currentClassLoader
)