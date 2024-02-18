package org.wdt.wdtc.core.download.game

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import org.wdt.wdtc.core.download.SpeedOfProgress
import org.wdt.wdtc.core.game.LibraryObject
import org.wdt.wdtc.core.game.LibraryObject.Companion.nativesLibraryArtifact
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.launch.GameRuntimeData
import org.wdt.wdtc.core.launch.GameRuntimeFile
import org.wdt.wdtc.core.manger.TaskKind
import org.wdt.wdtc.core.manger.TaskManger
import org.wdt.wdtc.core.utils.ckeckIsNull
import org.wdt.wdtc.core.utils.compareFile
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toCoroutineName
import java.io.File

class DownloadGameRuntime(
  private val version: Version, private val gameRuntimeFile: GameRuntimeFile, private val speed: SpeedOfProgress
) {

  fun start() {
    GameRuntimeData(version).run {
      gameRuntimeFile.libraryObject.run {
        if (gameRuntimeFile.nativesLibrary) {
          nativesLibraryArtifact.ckeckIsNull().let {
            DownloadGameRuntimeTask(it.changedNativesLibraryFile, it.apply { url = changedNativesLibraryUrl }, speed)
          }
        } else {
          DownloadGameRuntimeTask(changedLibraryFile, downloads.artifact.apply { url = changedLibraryUrl }, speed)
        }
      }
    }.run {
      start()
    }
  }

  class DownloadGameRuntimeTask(
    private val file: File, private val artifact: LibraryObject.Artifact, private val speed: SpeedOfProgress
  ) : TaskManger(file.name, TaskKind.COROUTINES) {
    init {
      coroutinesAction = speed.coroutineScope.launch(
        actionName.toCoroutineName(),
        CoroutineStart.LAZY
      ) {
        startDownloadTask(artifact.url, file)
        speed.countDown()
      }
    }

    override fun start() {
      if (file.compareFile(artifact)) {
        coroutinesAction.ckeckIsNull().start()
      } else {
        speed.countDown()
      }
    }
  }
}

