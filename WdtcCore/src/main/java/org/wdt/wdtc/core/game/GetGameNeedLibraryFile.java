package org.wdt.wdtc.core.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetGameNeedLibraryFile {
  private final Launcher launcher;

  public GetGameNeedLibraryFile(Launcher launcher) {
    this.launcher = launcher;
  }

  public List<LibraryFile> getFileList() throws IOException {
    List<LibraryFile> FileList = new ArrayList<>();
    GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
    for (LibraryObject LibraryObject : VersionJsonObject.getLibraries()) {
      if (LibraryObject.getNatives() != null) {
        JsonObject NativesJson = LibraryObject.getNatives();
        if (NativesJson.has("windows")) {
          FileList.add(new LibraryFile(LibraryObject, true));
        }
      } else {
        JsonArray rules = LibraryObject.getRules();
        if (rules != null) {
          JsonObject ActionObject = rules.get(rules.size() - 1).getAsJsonObject();
          String action = ActionObject.get("action").getAsString();
          String OsName = ActionObject.getAsJsonObject("os").get("name").getAsString();
          if (Objects.equals(action, "disallow") && Objects.equals(OsName, "osx")) {
            FileList.add(new LibraryFile(LibraryObject));
          } else if (Objects.equals(action, "allow") && Objects.equals(OsName, "windows")) {
            FileList.add(new LibraryFile(LibraryObject));
          }
        } else {
          FileList.add(new LibraryFile(LibraryObject));
        }
      }
    }
    return FileList;
  }

  @Getter
  public static class LibraryFile {
    private final LibraryObject libraryObject;
    private final boolean NativesLibrary;

    public LibraryFile(LibraryObject libraryObject, boolean nativesLibrary) {
      this.libraryObject = libraryObject;
      NativesLibrary = nativesLibrary;
    }

    public LibraryFile(LibraryObject libraryObject) {
      this(libraryObject, false);
    }

  }
}
