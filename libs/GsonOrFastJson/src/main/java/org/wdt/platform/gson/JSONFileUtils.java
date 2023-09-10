package org.wdt.platform.gson;

import com.google.gson.JsonIOException;
import org.wdt.utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class JSONFileUtils {
    public static void FormatJsonFile(File file) {
        try {
            FileUtils.writeStringToFile(file, JSON.FILE_GSON.toJson(JSONUtils.getJsonObject(file)));
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }
}
