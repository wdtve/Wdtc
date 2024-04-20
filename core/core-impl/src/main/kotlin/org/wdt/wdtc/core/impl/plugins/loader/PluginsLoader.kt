package org.wdt.wdtc.core.impl.plugins.loader

import org.wdt.utils.gson.parseJsonStreamToJsonObject
import org.wdt.utils.gson.parseObject
import org.wdt.wdtc.core.openapi.manger.wdtcDependenciesDirectory
import org.wdt.wdtc.core.openapi.plugins.config.ConfigFileObject
import org.wdt.wdtc.core.openapi.plugins.config.configFile
import org.wdt.wdtc.core.openapi.plugins.config.currentEnabledPlugins
import org.wdt.wdtc.core.openapi.utils.noNull
import java.net.URLClassLoader

object PluginsLoader {
	
	private val loader: URLClassLoader
	
	init {
		val classPath = currentEnabledPlugins.map {
			it.path.toFile()
		}.toMutableSet()
		classPath.forEach { paths ->
			val config: ConfigFileObject = URLClassLoader(arrayOf(paths.toURI().toURL()))
				.getResourceAsStream(configFile)
				.noNull()
				.parseJsonStreamToJsonObject()
				.parseObject()
			
			classPath.addAll(config.dependencies.map { library ->
				library.libraryName.apply {
					libraryDirectory = wdtcDependenciesDirectory
				}.libraryFile
			})
			
		}
		loader = classPath.map {
			it.toURI().toURL()
		}.toTypedArray().let {
			URLClassLoader(it)
		}
		
	}
	
	fun newInstance(name: String, vararg init: Any): Any? {
		return loader.loadClass(name).newInstances(init)
	}
}

fun <T> Class<T>.newInstances(vararg init: Any): Any? {
	return getConstructor(*init.map { it::class.java }.toTypedArray()).newInstance(init)
}