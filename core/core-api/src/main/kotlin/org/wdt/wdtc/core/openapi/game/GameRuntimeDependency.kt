package org.wdt.wdtc.core.openapi.game

import com.google.gson.*
import org.wdt.wdtc.core.openapi.utils.cleanStrInString
import org.wdt.wdtc.core.openapi.utils.gson.TypeAdapters
import org.wdt.wdtc.core.openapi.utils.gson.prettyGsonBuilder
import org.wdt.wdtc.core.openapi.utils.toURL
import java.io.File
import java.lang.reflect.Type
import java.net.URL
import java.util.regex.Pattern

class GameRuntimeDependency(val libraryName: String) {
	
	val groupId: String
	
	val artifactId: String
	
	val version: String
	
	var spaec: String? = null
	
	var fileExtension = "jar"
	
	private val pomFileExtension = "pom"
	
	var libraryDirectory: File = File(".")
	
	var libraryRepositoriesUrl = "https://repo1.maven.org/maven2/".toURL()
	
	
	init {
		Pattern.compile("([^: ]+):([^: ]+):(([^: ]*)(:([^: ]+))?)?(:([^: ]+))?").matcher(libraryName).let {
			require(it.matches()) {
				"Bad artifact coordinates $libraryName, expected format is <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>"
			}
			groupId = it.group(1).cleanString
			artifactId = it.group(2).cleanString
			version = it.group(4).cleanString
			it.group(6).let { spaec ->
				this.spaec = spaec?.cleanString
			}
		}
	}
	
	
	fun formPom(): String {
		return "$replacedGroupId/$artifactId/$version/$artifactId-$version.$pomFileExtension"
	}
	
	private val replacedGroupId = groupId.replace(".", "/")
	
	fun formJar(): String {
		return if (spaec == null) {
			"$replacedGroupId/$artifactId/$version/$artifactId-$version.$fileExtension"
		} else {
			"$replacedGroupId/$artifactId/$version/$artifactId-$version-$spaec.$fileExtension"
		}
	}
	
	private val String.cleanString: String
		get() {
			return this.cleanStrInString(":").cleanStrInString("[").cleanStrInString("]")
		}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is GameRuntimeDependency) return false
		
		if (libraryName != other.libraryName) return false
		if (groupId != other.groupId) return false
		if (artifactId != other.artifactId) return false
		if (version != other.version) return false
		if (spaec != other.spaec) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = libraryName.hashCode()
		result = 31 * result + groupId.hashCode()
		result = 31 * result + artifactId.hashCode()
		result = 31 * result + version.hashCode()
		result = 31 * result + (spaec?.hashCode() ?: 0)
		return result
	}
	
	override fun toString(): String {
		return "GameRuntimeDependency(libraryName='$libraryName', groupId='$groupId', artifactId='$artifactId', version='$version', spaec=$spaec)"
	}
	
	
	val libraryFilePath: String
		get() = libraryFile.canonicalPath
	val libraryFile: File
		get() = File(libraryDirectory, formJar())
	
	val libraryUrl: URL
		get() = URL(libraryRepositoriesUrl, formJar())
}

fun String.toDependency(): GameRuntimeDependency = GameRuntimeDependency(this)

val serializeGameRuntimeDependencyGsonBuilder: GsonBuilder =
	prettyGsonBuilder.registerTypeAdapter(GameRuntimeDependency::class.java, GameRuntimeDependencyTypeAdapters())

class GameRuntimeDependencyTypeAdapters : TypeAdapters<GameRuntimeDependency> {
	override fun serialize(
		src: GameRuntimeDependency, typeOfSrc: Type?, context: JsonSerializationContext?
	): JsonElement {
		return JsonPrimitive(src.libraryName)
	}
	
	override fun deserialize(
		json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?
	): GameRuntimeDependency {
		if (!json.isJsonPrimitive) error("json must be string")
		return GameRuntimeDependency(json.asString)
	}
	
}