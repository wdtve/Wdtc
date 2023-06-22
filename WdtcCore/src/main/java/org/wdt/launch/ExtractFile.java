package org.wdt.launch;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.PlatformUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractFile {
    private static final Logger logmaker = Logger.getLogger(ExtractFile.class);

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
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = in.read(buf)) != -1) fos.write(buf, 0, len);
                        fos.close();
                        in.close();
                    }
                }
            }
            zip.close();
        } catch (Exception e) {
            logmaker.error("* 压缩包提取发生错误:", e);
        }
    }
}