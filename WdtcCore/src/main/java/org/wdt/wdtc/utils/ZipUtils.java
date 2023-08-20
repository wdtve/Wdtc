package org.wdt.wdtc.utils;


import org.apache.log4j.Logger;
import org.wdt.utils.FileUtils;
import org.wdt.utils.FilenameUtils;
import org.wdt.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(ZipUtils.class);

    public static void unzipByFile(File file, String path) {
        try {
            ZipFile zip = new ZipFile(file);
            for (Enumeration<?> entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String name = entry.getName();
                if (Objects.equals(FilenameUtils.getExtension(name), "dll")) {
                    File unfile = new File(path + File.separator + name);
                    if (PlatformUtils.FileExistenceAndSize(unfile)) {
                        logmaker.info("* 提取natives库dll文件" + name + "中");
                        Files.createFile(Paths.get(path + File.separator + name));
                        InputStream in = zip.getInputStream(entry);
                        FileOutputStream fos = new FileOutputStream(unfile);
                        IOUtils.copyLarge(in, fos);
                    }
                }
            }
            zip.close();
        } catch (Exception e) {
            logmaker.error("* 压缩包提取发生错误:", e);
        }
    }

    public static void unZipBySpecifyFile(String ZipFile, String unFilePath) {
        try {
            File unZipPath = new File(unFilePath);
            ZipFile zip = new ZipFile(new File(FilenameUtils.separatorsToWindows(ZipFile)));
            for (Enumeration<?> entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String name = entry.getName();
                if (Pattern.compile(unZipPath.getName()).matcher(name).find()) {
                    logmaker.info("* 提取 " + unZipPath + " 中");
                    File unfile = new File(FilenameUtils.separatorsToWindows(unFilePath));
                    FileUtils.touch(unZipPath);
                    InputStream in = zip.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(unfile);
                    IOUtils.copyLarge(in, fos);
                }
            }
            zip.close();
        } catch (Exception e) {
            logmaker.error("* 压缩包提取发生错误:", e);
        }
    }

    public static void unZipToFile(String ZipPath, String unFilePath, String unFileName) {
        try {
            ZipFile zipFile = new ZipFile(new File(ZipPath));
            FileOutputStream outputStream = new FileOutputStream(unFilePath);
            InputStream stream = zipFile.getInputStream(zipFile.getEntry(unFileName));
            IOUtils.copyLarge(stream, outputStream);
        } catch (IOException e) {
            logmaker.error("* 压缩包提取发生错误:", e);
        }
    }
}
