package org.wdt.WdtcLauncher;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.wdt.StringUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.*;

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
                    if (StringUtil.FileExistenceAndSize(unfile)) {
                        logmaker.debug("* 提取natives库dll文件" + name + "中");
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

    public static void compressedFile(String resourcesPath, String targetPath) throws Exception {
        File resourcesFile = new File(resourcesPath);
        File targetFile = new File(targetPath);

        if (!targetFile.exists()) {
            if (!targetFile.exists()) {
                if (targetFile.isDirectory()) {
                    targetFile.mkdirs();
                }
            }
        }

        FileOutputStream outputStream = new FileOutputStream(targetFile);
        CheckedOutputStream cos = new CheckedOutputStream(outputStream, new CRC32());
        ZipOutputStream out = new ZipOutputStream(cos);
        createCompressedFile(out, resourcesFile, "");
        out.close();

    }

    private static void createCompressedFile(ZipOutputStream out, File file, String dir) throws Exception {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            out.putNextEntry(new ZipEntry(dir + "/"));
            dir = dir.length() == 0 ? "" : dir + "/";
            for (File value : files) {
                createCompressedFile(out, value, dir + value.getName());
            }
        } else {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(dir);
            out.putNextEntry(entry);
            int j;
            byte[] buffer = new byte[1024];
            while ((j = bis.read(buffer)) > 0) {
                out.write(buffer, 0, j);
            }
            bis.close();
        }
    }
}
