package org.wdt.wdtc.core.openapi.plugins.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineName
import org.wdt.utils.gson.*
import org.wdt.wdtc.core.openapi.game.LibraryObjectList
import org.wdt.wdtc.core.openapi.manager.loadPluginsListFile
import org.wdt.wdtc.core.openapi.manager.pluginsDirectory
import org.wdt.wdtc.core.openapi.plugins.config.PluginsObjectSequence.Companion.asPluginsObjectSequence
import org.wdt.wdtc.core.openapi.plugins.config.PluginsObjectSequence.Companion.serializePluginsObjectSequenceGsonBuilder
import org.wdt.wdtc.core.openapi.plugins.loader.PluginLoader
import org.wdt.wdtc.core.openapi.utils.gson.PluginsObjectSequenceTypeAdapter
import org.wdt.wdtc.core.openapi.utils.gson.TypeAdapters
import org.wdt.wdtc.core.openapi.utils.gson.defaultGsonBuilder
import org.wdt.wdtc.core.openapi.utils.openZip
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext

const val pluginConfigFile = "plugin.json"

class Plugin(
	val config: PluginConfigFileObject,
	val loader: PluginLoader
) {
	val name: String
		get() = config.name
	
	val context: CoroutineContext
		get() = CoroutineName(name)
	
}

interface ModuleActionMaps {
	val actions: List<ActionMapObject>
	val typeAdapter: String
}

class PluginConfigFileObject(
	@field:SerializedName("name")
	val name: String,
	@field:SerializedName("developer")
	val developer: String,
	@field:SerializedName("core")
	val coreActions: ModuleActionMaps,
	@field: SerializedName("console")
	val consoleActions: ModuleActionMaps,
	@field:SerializedName("dependencies")
	val dependencies: LibraryObjectList
)

class CoreActionMaps(
	override val actions: List<ActionMapObject>
) : ModuleActionMaps {
	override val typeAdapter: String = "org.wdt.wdtc.core.openapi.plugins.config.CoreActionMapsTypeAdapter"
}

class CoreActionMapsTypeAdapter : TypeAdapters<ModuleActionMaps> {
	override fun serialize(src: ModuleActionMaps, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return (src as CoreActionMaps).actions.asJsonArray()
	}
	
	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): CoreActionMaps {
		return CoreActionMaps(json.asJsonArray.parseObjectList())
	}
}

open class ActionMapObject(
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
		get() = jarFile.openZip { zip ->
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
	