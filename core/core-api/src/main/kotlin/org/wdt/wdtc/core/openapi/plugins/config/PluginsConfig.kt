package org.wdt.wdtc.core.openapi.plugins.config

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.readFileToClass
import org.wdt.wdtc.core.openapi.game.LibraryObjectList
import org.wdt.wdtc.core.openapi.manger.loadPluginsListFile
import org.wdt.wdtc.core.openapi.plugins.interfaces.Action
import org.wdt.wdtc.core.openapi.plugins.interfaces.ActionImpls
import java.nio.file.Path
import java.util.*

const val configFile = "plugin.json"

class ConfigFileObject(
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
	val action: Action,
	@field:SerializedName("task")
	val clazz: ActionImpls
)

class PluginsObject(
	@field:SerializedName("name")
	val name: String,
	@field:SerializedName("developer")
	val developer: String,
	@field:SerializedName("file")
	val path: Path,
	@field:SerializedName("enable")
	val enabled: Boolean
)

val currentEnabledPlugins: List<PluginsObject>
	get() = loadPluginsListFile.readFileToClass<LinkedList<PluginsObject>>().filter {
		it.enabled
	}
	