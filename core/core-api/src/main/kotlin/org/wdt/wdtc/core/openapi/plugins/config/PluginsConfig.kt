package org.wdt.wdtc.core.openapi.plugins.config

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineName
import org.wdt.utils.gson.parseJsonStreamToJsonObject
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.toJsonString
import org.wdt.wdtc.core.openapi.game.LibraryObjectList
import org.wdt.wdtc.core.openapi.manger.loadPluginsListFile
import org.wdt.wdtc.core.openapi.manger.pluginsDirectory
import org.wdt.wdtc.core.openapi.plugins.config.PluginsObjectSequence.Companion.asPluginsObjectSequence
import org.wdt.wdtc.core.openapi.plugins.config.PluginsObjectSequence.Companion.serializePluginsObjectSequenceGsonBuilder
import org.wdt.wdtc.core.openapi.utils.gson.PluginsObjectSequenceTypeAdapter
import org.wdt.wdtc.core.openapi.utils.gson.defaultGsonBuilder
import java.net.URLClassLoader
import java.util.zip.ZipFile
import kotlin.coroutines.CoroutineContext

const val pluginConfigFile = "plugin.json"

class Plugin(
	val config: PluginConfigFileObject,
	val loader: URLClassLoader
) {
	val name: String
		get() = config.name
	
	val context: CoroutineContext
		get() = CoroutineName(name)
	
}

class PluginConfigFileObject(
	@field:SerializedName("name")
	val name: String,
	@field:SerializedName("developer")
	val developer: String,
	@field:SerializedName("actionMaps")
	val actions: List<ActionMapObject>,
	@field:SerializedName("dependencies")
	val dependencies: LibraryObjectList
)

class ActionMapObject(
	@field:SerializedName("action")
	val action: String,
	@field:SerializedName("impl")
	val clazz: String
	
)

class PluginsObjectSequence(
	private val sequence: Sequence<PluginsObject> = emptySequence()
) : Sequence<PluginsObject> by sequence {
	companion object {
		fun Sequence<PluginsObject>.asPluginsObjectSequence(): PluginsObjectSequence {
			return PluginsObjectSequence(this)
		}
		
		val serializePluginsObjectSequenceGsonBuilder: GsonBuilder = defaultGsonBuilder.registerTypeAdapter(
			PluginsObjectSequence::class.java,
			PluginsObjectSequenceTypeAdapter
		)
	}
	
	override fun toString(): String {
		return "PluginsObjectSequence(${toJsonString(serializePluginsObjectSequenceGsonBuilder)})"
	}
	
}

class PluginsObject(
	@field:SerializedName("name")
	val name: String,
	@field:SerializedName("developer")
	val developer: String,
	@field:SerializedName("file")
	val path: String,
	@field:SerializedName("enable")
	val enabled: Boolean
) {
	
	val jarFile
		get() = pluginsDirectory.resolve(path)
	
	val pluginConfigFileObject: PluginConfigFileObject
		get() = ZipFile(jarFile).let { zip ->
			zip.getInputStream(zip.getEntry(pluginConfigFile)).use {
				it.parseJsonStreamToJsonObject().parseObject()
			}
		}
}

val currentEnabledPlugins: PluginsObjectSequence = loadPluginsListFile.readFileToClass<PluginsObjectSequence>(
	serializePluginsObjectSequenceGsonBuilder
).filter {
	it.enabled
}.asPluginsObjectSequence()
	