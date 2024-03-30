package org.wdt.wdtc.core.game

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.utils.FileData
import java.net.URL
import java.util.*

class GameVersionJsonObject(
	
	@field:SerializedName("arguments")
	var arguments: Arguments,
	
	@field:SerializedName("assetIndex")
	var assetIndex: FileDataObject,
	
	@field:SerializedName("assets")
	var assets: String,
	
	@SerializedName("complianceLevel")
	var complianceLevel: Int = 0,
	
	@field:SerializedName("downloads")
	var downloads: Downloads,
	
	@field:SerializedName("id")
	override var id: String,
	
	@field:SerializedName("javaVersion")
	var javaVersion: JsonObject,
	
	@field:SerializedName("libraries")
	override var libraries: LibraryObjectList,
	
	@field:SerializedName("logging")
	var logging: JsonObject,
	
	@field:SerializedName("mainClass")
	var mainClass: String,
	
	@field:SerializedName("patches")
	var patches: List<JsonObject>? = null,
	
	@field:SerializedName("minimumLauncherVersion")
	var minimumLauncherVersion: Int = 0,
	
	@field:SerializedName("releaseTime")
	var releaseTime: Date,
	
	@field:SerializedName("time")
	var time: Date,
	
	@field:SerializedName("type")
	var type: String,
) : VersionJsonObjectInterface {
	
	
	class FileDataObject(
		@field:SerializedName("sha1")
		override val sha1: String,
		
		@field:SerializedName("size")
		override val size: Long = 0,
		
		@field:SerializedName("url")
		var url: URL
	) : FileData {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false
			
			other as FileDataObject
			
			if (sha1 != other.sha1) return false
			if (size != other.size) return false
			if (url != other.url) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			var result = sha1.hashCode()
			result = (31 * result + size).toInt()
			result = 31 * result + url.hashCode()
			return result
		}
		
		override fun toString(): String {
			return "FileDataObject(fileSha1=$sha1, fileSize=$size, listJsonURL=$url)"
		}
		
	}
	
	class Downloads(
		@field:SerializedName("client")
		var client: FileDataObject,
		
		@field:SerializedName("client_mappings")
		var clientMappings: FileDataObject,
		
		@field:SerializedName("server")
		var server: FileDataObject,
		
		@field:SerializedName("server_mappings")
		var serverMappings: FileDataObject,
	) {
		
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false
			
			other as Downloads
			
			if (client != other.client) return false
			if (clientMappings != other.clientMappings) return false
			if (server != other.server) return false
			if (serverMappings != other.serverMappings) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			var result = client.hashCode()
			result = 31 * result + clientMappings.hashCode()
			result = 31 * result + server.hashCode()
			result = 31 * result + serverMappings.hashCode()
			return result
		}
		
		override fun toString(): String {
			return "Downloads(client=$client, clientMappings=$clientMappings, server=$server, serverMappings=$serverMappings)"
		}
	}
	
	override fun toString(): String {
		return "GameVersionJsonObject(arguments=$arguments, assetIndex=$assetIndex, assets=$assets, complianceLevel=$complianceLevel, downloads=$downloads, id=$id, javaVersion=$javaVersion, libraries=$libraries, logging=$logging, mainClass=$mainClass, JsonObject=$patches, minimumLauncherVersion=$minimumLauncherVersion, releaseTime=$releaseTime, time=$time, type=$type)"
	}
}

class Arguments {
	@SerializedName("game")
	var gameList: JsonArray? = null
	
	@SerializedName("jvm")
	var jvmList: JsonArray? = null
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as Arguments
		
		if (gameList != other.gameList) return false
		if (jvmList != other.jvmList) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = gameList?.hashCode() ?: 0
		result = 31 * result + (jvmList?.hashCode() ?: 0)
		return result
	}
	
	override fun toString(): String {
		return "Arguments(GameList=$gameList, JvmList=$jvmList)"
	}
	
}


val serializeGameVersionJsonObjectGson: GsonBuilder = serializeLibraryObjectListGsonBuilder
