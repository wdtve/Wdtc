package org.wdt.wdtc.core.openapi.game

import com.google.gson.*
import org.wdt.wdtc.core.openapi.game.GameRuntimeDependency.Companion.cleanString
import org.wdt.wdtc.core.openapi.utils.cleanStrInString
import org.wdt.wdtc.core.openapi.utils.gson.TypeAdapters
import org.wdt.wdtc.core.openapi.utils.gson.prettyGsonBuilder
import org.wdt.wdtc.core.openapi.utils.toURL
import java.io.File
import java.lang.reflect.Type
import java.net.URL
import java.util.regex.Pattern

data class GameRuntimeDependency(
	val libraryName: String,
	
	val groupId: String,
	
	val artifactId: String,
	
	val version: String,
	
	var spaec: String? = null
) {
	
	var fileExtension = "jar"
	
	var libraryDirectory: File = File(".")
	
	var libraryRepositoriesUrl = "https://repo1.maven.org/maven2/".toURL()
	
	
	fun formPom(): String {
		return "$replacedGroupId/$artifactId/$version/$artifactId-$version.pom"
	}
	
	private val replacedGroupId = groupId.replace(".", "/")
	
	fun formJar(): String {
		return if (spaec == null) {
			"$replacedGroupId/$artifactId/$version/$artifactId-$version.$fileExtension"
		} else {
			"$replacedGroupId/$artifactId/$version/$artifactId-$version-$spaec.$fileExtension"
		}
	}
	
	
	val libraryFilePath: String
		get() = libraryFile.canonicalPath
	val libraryFile: File
		get() = File(libraryDirectory, formJar())
	
	val libraryUrl: URL
		get() = URL(libraryRepositoriesUrl, formJar())
	
	companion object {
		val String.cleanString: String
			get() {
				return this.cleanStrInString(":").cleanStrInString("[").cleanStrInString("]")
			}
		
	}
}

fun GameRuntimeDependency(libraryName: String): GameRuntimeDependency {
	return Pattern.compile("([^: ]+):([^: ]+):(([^: ]*)(:([^: ]+))?)?(:([^: ]+))?").matcher(libraryName).also {
		require(it.matches()) {
			"Bad artifact coordinates $libraryName, expected format is <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>"
		}
	}.let {
		GameRuntimeDependency(
			libraryName,
			it.group(1).cleanString,
			it.group(2).cleanString,
			it.group(4).cleanString,
			it.group(6)?.cleanString
		)
	}
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
		return GameRuntimeDependency(json.asString)
	}
	
}