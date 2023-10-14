package org.wdt.wdtc.utils.gson;

import com.google.gson.JsonIOException;
import org.wdt.utils.io.FileUtils;

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
