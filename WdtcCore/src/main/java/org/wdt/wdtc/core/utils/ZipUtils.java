package org.wdt.wdtc.core.utils;


import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.FilenameUtils;
import org.wdt.utils.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {
  private static final Logger logmaker = WdtcLogger.getLogger(ZipUtils.class);

  public static void unzipByFile(File file, String path) {
    try {
      ZipFile zip = new ZipFile(file);
      for (ZipEntry entry : zip.stream().toList()) {
        String name = entry.getName();
        if (Objects.equals(FilenameUtils.getExtension(name), "dll")) {
          File unfile = new File(path + File.separator + name);
          if (FileUtils.isFileNotExists(unfile)) {
            logmaker.info("提取natives库dll文件" + name + "中");
            Files.createFile(Paths.get(path + File.separator + name));
            InputStream in = zip.getInputStream(entry);
            FileOutputStream fos = new FileOutputStream(unfile);
            IOUtils.copy(in, fos);
          }
        }
      }
      zip.close();
    } catch (Exception e) {
      logmaker.error("压缩包提取发生错误:", e);
    }
  }

  public static void unZipBySpecifyFile(File zipFile, File unFilePath) {
    try {
      ZipFile zip = new ZipFile(zipFile);
      for (ZipEntry entry : zip.stream().toList()) {
        String name = entry.getName();
        if (Pattern.compile(unFilePath.getName()).matcher(name).find()) {
          logmaker.info("提取 " + unFilePath + " 中");
          FileUtils.touch(unFilePath);
          InputStream in = zip.getInputStream(entry);
          FileOutputStream fos = new FileOutputStream(unFilePath);
          IOUtils.copy(in, fos);
        }
      }
      zip.close();
    } catch (Exception e) {
      logmaker.error("压缩包提取发生错误:", e);
    }
  }

  public static void unZipToFile(File ZipPath, File unFile, String unFileName) {
    try {
      ZipFile zipFile = new ZipFile(ZipPath);
      FileOutputStream outputStream = new FileOutputStream(unFile);
      InputStream stream = zipFile.getInputStream(zipFile.getEntry(unFileName));
      IOUtils.copy(stream, outputStream);
    } catch (IOException e) {
      logmaker.error("压缩包提取发生错误:", e);
    }
  }
}