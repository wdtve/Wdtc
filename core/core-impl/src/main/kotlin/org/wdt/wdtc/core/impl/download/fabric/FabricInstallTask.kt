package org.wdt.wdtc.core.impl.download.fabric

import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.openapi.download.interfaces.ModInstallTaskInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.utils.STRING_SPACE
import org.wdt.wdtc.core.openapi.utils.cleanStrInString
import org.wdt.wdtc.core.openapi.utils.noNull
import org.wdt.wdtc.core.openapi.utils.startDownloadTask

class FabricInstallTask(version: Version, fabricVersionNumber: String) :
	FabricDonwloadInfo(version, fabricVersionNumber), ModInstallTaskInterface {
	
	constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) : this(
		version, versionsJsonObjectInterface.versionNumber.noNull()
	)
	
	override fun overwriteVersionJson() {
		fabricProfileJsonObject.also { fabirc ->
			version.run {
				gameVersionJsonObject.apply {
					id = "${versionNumber}-fabric-$modVersion"
					mainClass = fabirc.mainClass
					arguments = arguments.apply {
						jvmList = jvmList.also { game ->
							fabirc.arguments.jvmList?.onEach {
								game!!.add(it.asString.cleanStrInString(STRING_SPACE))
							}
						}
						gameList = gameList.also { game ->
							fabirc.arguments.gameList?.onEach {
								game!!.add(it.asString)
							}
						}
					}
					libraries.addAll(fabirc.libraries.map {
						it.toLibraryObject()
					})
				}.also {
					it.putToVersionJson()
				}
			}
		}
	}
	
	override fun writeVersionJsonPatches() {
		version.run {
			gameVersionJsonObject.apply {
				patches = listOf(
					version.versionJson.readFileToJsonObject(), fabricVersionJson.readFileToJsonObject()
				)
			}.putToVersionJson()
		}
	}
	
	override suspend fun afterDownloadTask() {
		if (isHasApiDownloadTask) {
			apiDownloadTask.noNull().startDownloadFabricAPI()
		}
	}
	
	override suspend fun beforInstallTask() {
		startDownloadTask(
			fabricVersionFileUrl.format(version.versionNumber, modVersion), fabricVersionJson
		)
	}
}