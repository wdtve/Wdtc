package org.wdt.wdtc.core.impl.download.quilt

import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.openapi.download.interfaces.ModInstallTaskInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.utils.STRING_SPACE
import org.wdt.wdtc.core.openapi.utils.cleanStrInString
import org.wdt.wdtc.core.openapi.utils.startDownloadTask

class QuiltInstallTask(
	private val version: QuiltVersionImpl,
) : ModInstallTaskInterface {
	
	constructor(version: Version, quiltVersionNumber: String) : this(QuiltVersionImpl(version, quiltVersionNumber))
	
	constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) : this(
		version, versionsJsonObjectInterface.versionNumber
	)
	
	override suspend fun beforInstallTask() {
		startDownloadTask(version.quiltVersionJsonUrl, version.quiltVersionJson)
	}
	
	override suspend fun overwriteVersionJson() {
		version.quiltProfileJsonObject.also { quilt ->
			version.run {
				gameVersionJsonObject.apply {
					gameVersionJsonObject.apply {
						id = "${versionNumber}-quilt-$modVersion"
						mainClass = quilt.mainClass
						arguments = arguments.apply {
							jvmList = jvmList.also { game ->
								quilt.arguments.jvmList?.onEach {
									game!!.add(it.asString.cleanStrInString(STRING_SPACE))
								}
							}
							gameList = gameList.also { game ->
								quilt.arguments.gameList?.onEach {
									game!!.add(it.asString)
								}
							}
						}
						libraries.addAll(quilt.libraries.map {
							it.toLibraryObject()
						})
					}.also {
						it.putToVersionJson()
					}
					
				}
			}
		}
	}
	
	override fun writeVersionJsonPatches() {
		version.run {
			gameVersionJsonObject.apply {
				patches = mutableListOf(
					versionJson.readFileToJsonObject(),
					quiltVersionJson.readFileToJsonObject()
				)
			}.putToVersionJson()
		}
	}
	
	override suspend fun afterDownloadTask() {}
}
