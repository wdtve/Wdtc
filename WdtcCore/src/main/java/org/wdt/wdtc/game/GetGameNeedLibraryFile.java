package org.wdt.wdtc.game;

import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;

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
                JSONObject NativesJson = new JSONObject(LibraryObject.getNatives());
                if (NativesJson.has("windows")) {
                    FileList.add(new LibraryFile(LibraryObject, true));
                }
            } else {
                JSONArray rules = new JSONArray(LibraryObject.getRules());
                if (LibraryObject.getRules() != null) {
                    JSONObject ActionObject = rules.getJSONObject(rules.size() - 1);
                    String action = ActionObject.getString("action");
                    String OsName = ActionObject.getJSONObject("os").getString("name");
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

        public LibraryObject getLibraryObject() {
            return libraryObject;
        }

        public boolean isNativesLibrary() {
            return NativesLibrary;
        }

    }
}
