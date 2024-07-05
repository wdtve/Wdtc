package org.wdt.wdtc.core.openapi.game

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.async
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.toJsonString
import org.wdt.wdtc.core.openapi.game.LibraryObject.Companion.generateLibraryObject
import org.wdt.wdtc.core.openapi.manager.SystemKind
import org.wdt.wdtc.core.openapi.manager.currentSystem
import org.wdt.wdtc.core.openapi.utils.*
import org.wdt.wdtc.core.openapi.utils.gson.ActionTypeAdapter
import org.wdt.wdtc.core.openapi.utils.gson.LibraryObjectListTypeAdapter
import org.wdt.wdtc.core.openapi.utils.gson.SystemKindTypeAdapter
import java.net.URL
import java.util.*

class LibraryObject(
	@field:SerializedName("downloads") var downloads: Downloads,
	
	@field:SerializedName("name") var libraryName: GameRuntimeDependency
) {
	@SerializedName("rules")
	var rules: Rules? = null
	
	@SerializedName("natives")
	var natives: Natives? = null
	
	class Downloads(
		@field:SerializedName("artifact") var artifact: Artifact
	) {
		
		@SerializedName("classifiers")
		var classifiers: Classifiers? = null
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is Downloads) return false
			
			if (artifact != other.artifact) return false
			if (classifiers != other.classifiers) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			var result = artifact.hashCode()
			result = 31 * result + (classifiers?.hashCode() ?: 0)
			return result
		}
		
		override fun toString(): String {
			return toJsonString()
		}
		
	}
	
	class Artifact(
		var path: String, override val sha1: String, override val size: Long, var url: URL
	) : FileData {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is Artifact) return false
			
			if (path != other.path) return false
			if (sha1 != other.sha1) return false
			if (size != other.size) return false
			if (url != other.url) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			var result = path.hashCode()
			result = 31 * result + sha1.hashCode()
			result = 31 * result + size.hashCode()
			result = 31 * result + url.hashCode()
			return result
		}
		
		override fun toString(): String {
			return toJsonString()
		}
	}
	
	class Classifiers {
		@SerializedName("natives-macos")
		var nativesMacos: Artifact? = null
		
		@SerializedName("natives-linux")
		var nativesLinux: Artifact? = null
		
		@SerializedName("natives-windows")
		var nativesWindows: Artifact? = null
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is Classifiers) return false
			
			if (nativesMacos != other.nativesMacos) return false
			if (nativesLinux != other.nativesLinux) return false
			if (nativesWindows != other.nativesWindows) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			var result = nativesMacos?.hashCode() ?: 0
			result = 31 * result + (nativesLinux?.hashCode() ?: 0)
			result = 31 * result + (nativesWindows?.hashCode() ?: 0)
			return result
		}
		
		override fun toString(): String {
			return toJsonString()
		}
		
		
	}
	
	class Natives {
		@SerializedName("osx")
		var osx: String? = null
		
		@SerializedName("windows")
		var windows: String? = null
		
		@SerializedName("linux")
		var linux: String? = null
		
		val isUseForCurrent: Boolean
			get() {
				return when (currentSystem) {
					SystemKind.WINDOWS -> windows != null
					SystemKind.LINUX -> linux != null
					SystemKind.MAC -> osx != null
				}
			}
		
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is Natives) return false
			
			if (osx != other.osx) return false
			if (windows != other.windows) return false
			if (linux != other.linux) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			var result = osx?.hashCode() ?: 0
			result = 31 * result + (windows?.hashCode() ?: 0)
			result = 31 * result + (linux?.hashCode() ?: 0)
			return result
		}
		
		override fun toString(): String {
			return toJsonString()
		}
		
		
	}
	
	companion object {
		
		suspend fun GameRuntimeDependency.generateLibraryObject(): LibraryObject {
			return getLibraryObject(this, libraryRepositoriesUrl)
		}
		
		suspend fun getLibraryObject(dependency: GameRuntimeDependency, defaultUrl: URL): LibraryObject {
			val connection = runOnIO { async { dependency.libraryUrl.openConnection() } }
			val downloads = Downloads(
				artifact = Artifact(
					path = dependency.formJar(),
					sha1 = runOnIO {
						connection.await().getInputStream()
					}.use { it.sha1() },
					size = connection.await().contentLengthLong,
					url = dependency.apply {
						libraryRepositoriesUrl = defaultUrl
					}.libraryUrl
				)
			)
			return LibraryObject(downloads, dependency).also {
				logmaker.info(it)
			}
		}
		
		fun getLibraryObject(jsonObject: JsonObject): LibraryObject {
			return jsonObject.parseObject(serializeLibraryObjectGsonBuilder)
		}
		
		fun getLibraryObject(jsonStr: String): LibraryObject {
			return jsonStr.parseObject(serializeLibraryObjectGsonBuilder)
		}
		
		val Classifiers?.currentNativesOS: Artifact?
			get() = when (currentSystem) {
				SystemKind.WINDOWS -> this?.nativesWindows
				SystemKind.MAC -> this?.nativesMacos
				SystemKind.LINUX -> this?.nativesLinux
			}
		private val LibraryObject.libraryArtifact
			get() = this.downloads.artifact
		val LibraryObject.nativesLibraryArtifact: Artifact?
			get() = this.downloads.classifiers.currentNativesOS
		val LibraryObject.officialLibraryUrl
			get() = this.libraryArtifact.url
		
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is LibraryObject) return false
		
		if (downloads != other.downloads) return false
		if (libraryName != other.libraryName) return false
		if (rules != other.rules) return false
		if (natives != other.natives) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = downloads.hashCode()
		result = 31 * result + libraryName.hashCode()
		result = 31 * result + (rules?.hashCode() ?: 0)
		result = 31 * result + (natives?.hashCode() ?: 0)
		return result
	}
	
	override fun toString(): String {
		return toJsonString(serializeLibraryObjectGsonBuilder)
	}
	
}


val serializeLibraryObjectGsonBuilder: GsonBuilder =
	serializeGameRuntimeDependencyGsonBuilder.registerTypeAdapter(SystemKind::class.java, SystemKindTypeAdapter)
		.registerTypeAdapter(Rules.Rule.Action::class.java, ActionTypeAdapter)


val serializeLibraryObjectListGsonBuilder: GsonBuilder =
	serializeLibraryObjectGsonBuilder.registerTypeAdapter(LibraryObjectList::class.java, LibraryObjectListTypeAdapter())

class LibraryObjectList(
	libraryList: MutableList<LibraryObject> = mutableListOf()
) : MutableList<LibraryObject> by libraryList {
	
	suspend fun add(dependency: GameRuntimeDependency, defaultUrl: URL) {
		this.add(LibraryObject.getLibraryObject(dependency, defaultUrl))
	}
	
	fun add(jsonObject: JsonObject) {
		this.add(LibraryObject.getLibraryObject(jsonObject))
	}
	
	fun add(jsonStr: String) {
		this.add(LibraryObject.getLibraryObject(jsonStr))
	}
	
	fun LibraryObject.addToList() {
		this@LibraryObjectList.add(this)
	}
	
	fun addAll(jsonArray: JsonArray) {
		this.addAll(jsonArray.map {
			LibraryObject.getLibraryObject(it.asJsonObject)
		})
	}
	
	override fun toString(): String {
		return toJsonString(serializeLibraryObjectListGsonBuilder)
	}
	
	companion object {
		
		suspend fun Iterable<GameRuntimeDependency>.generateLibraryObjectList(): LibraryObjectList {
			return map {
				it.generateLibraryObject()
			}.asLibraryObjectList()
		}
		
		private fun Iterable<LibraryObject>.asLibraryObjectList(): LibraryObjectList {
			return LibraryObjectList(toMutableList())
		}
		
	}
}

class Rules(
	private val rules: LinkedList<Rule> = LinkedList()
) : MutableList<Rules.Rule> by rules {
	
	val isUseForCurrent: Boolean
		get() = rules.all {
			it.isUseForCurrent
		}
	
	
	class Rule(
		@field:SerializedName("action") val action: Action, @field:SerializedName("os") var os: OS? = null
	) {
		
		@JsonAdapter(ActionTypeAdapter::class)
		enum class Action {
			ALLOW, DISALLOW;
		}
		
		val isUseForCurrent: Boolean
			get() = when (action) {
				Action.DISALLOW -> {
					os!!.name != currentSystem
				}
				
				Action.ALLOW -> {
					os.let {
						if (it != null)
							it.name == currentSystem
						else true
					}
				}
			}
		
		class OS(
			@field:SerializedName("name")
			val name: SystemKind
		)
	}
}

