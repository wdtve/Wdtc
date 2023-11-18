package org.wdt.wdtc.core.utils.gson;

import lombok.SneakyThrows;
import org.wdt.utils.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JSONFileUtils {
  @SneakyThrows(IOException.class)
  public static void formatJsonFile(File file) {
    FileUtils.writeStringToFile(file, JSON.FILE_GSON.toJson(JSONUtils.readFiletoJsonObject(file)));
  }
}
