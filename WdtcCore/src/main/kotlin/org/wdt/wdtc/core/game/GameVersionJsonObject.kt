package org.wdt.wdtc.core.game

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.net.URL

class GameVersionJsonObject {
  @SerializedName("arguments")
  var arguments: Arguments? = null

  @SerializedName("assetIndex")
  var assetIndex: FileDataObject? = null

  @SerializedName("assets")
  var assets: String? = null

  @SerializedName("complianceLevel")
  var complianceLevel = 0

  @SerializedName("downloads")
  var downloads: Downloads? = null

  @SerializedName("id")
  var id: String? = null

  @SerializedName("javaVersion")
  var javaVersion: JsonObject? = null

  @SerializedName("libraries")
  var libraries: MutableList<LibraryObject>? = null

  @SerializedName("logging")
  var logging: JsonObject? = null

  @SerializedName("mainClass")
  var mainClass: String? = null

  @SerializedName("patches")
  var patches: List<JsonObject>? = null

  @SerializedName("minimumLauncherVersion")
  var minimumLauncherVersion = 0

  @SerializedName("releaseTime")
  var releaseTime: String? = null

  @SerializedName("time")
  var time: String? = null

  @SerializedName("type")
  var type: String? = null


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

  class FileDataObject {

    @SerializedName("sha1")
    var fileSha1: String? = null

    @SerializedName("size")
    var fileSize = 0

    @SerializedName("url")
    var listJsonURL: URL? = null
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false

      other as FileDataObject

      if (fileSha1 != other.fileSha1) return false
      if (fileSize != other.fileSize) return false
      if (listJsonURL != other.listJsonURL) return false

      return true
    }

    override fun hashCode(): Int {
      var result = fileSha1?.hashCode() ?: 0
      result = 31 * result + fileSize
      result = 31 * result + (listJsonURL?.hashCode() ?: 0)
      return result
    }

    override fun toString(): String {
      return "FileDataObject(fileSha1=$fileSha1, fileSize=$fileSize, listJsonURL=$listJsonURL)"
    }

  }

  class Downloads {
    @SerializedName("client")
    var client: FileDataObject? = null

    @SerializedName("client_mappings")
    var clientMappings: FileDataObject? = null

    @SerializedName("server")
    var server: FileDataObject? = null

    @SerializedName("server_mappings")
    var serverMappings: FileDataObject? = null

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
      var result = client?.hashCode() ?: 0
      result = 31 * result + (clientMappings?.hashCode() ?: 0)
      result = 31 * result + (server?.hashCode() ?: 0)
      result = 31 * result + (serverMappings?.hashCode() ?: 0)
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
