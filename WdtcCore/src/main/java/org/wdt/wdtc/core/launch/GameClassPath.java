package org.wdt.wdtc.core.launch;


import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.game.GetGameNeedLibraryFile;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.manger.SettingManger;
import org.wdt.wdtc.core.utils.ZipUtils;

import java.io.IOException;

public class GameClassPath extends AbstractGameCommand {
  private final Launcher launcher;

  public GameClassPath(Launcher launcher) {
    this.launcher = launcher;
  }

  public StringBuilder getCommand() {
    try {
      GameLibraryData gameLibraryData = new GameLibraryData(launcher);
      FileUtils.createDirectories(launcher.getVersionNativesPath());
      GetGameNeedLibraryFile FileList = new GetGameNeedLibraryFile(launcher);
      for (GetGameNeedLibraryFile.LibraryFile LibraryFile : FileList.getFileList()) {
        if (LibraryFile.isNativesLibrary()) {
          ZipUtils.unzipByFile(gameLibraryData.GetNativesLibraryFile(LibraryFile.getLibraryObject().getDownloads().getClassifiers().getNativesindows()), FileUtils.getCanonicalPath(launcher.getVersionNativesPath()));
        } else {
          insertclasspathSeparator(gameLibraryData.GetLibraryFile(LibraryFile.getLibraryObject()));
        }
      }
      command.append(launcher.getVersionJar());
      String Accounts = launcher.getAccounts().getJvmCommand();
      if (!Accounts.isEmpty()) {
        command.append(Accounts);
      }
      if (SettingManger.getSetting().isLlvmpipeLoader()) {
        command.append(LlbmpipeLoader());
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return command;
  }


  private String LlbmpipeLoader() {
    return " -javaagent:" + FileManger.getLlbmpipeLoader();
  }
}
