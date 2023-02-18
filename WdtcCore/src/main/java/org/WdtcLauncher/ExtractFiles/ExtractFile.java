package org.WdtcLauncher.ExtractFiles;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractFile {
    private static final Logger logmaker = Logger.getLogger(ExtractFile.class);

    public static void getExtractFiles(String natives_lib_path, String natives_path) {
        File file = new File(natives_lib_path);
        unzipByFile(file, natives_path);


    }

    public static boolean unzipByFile(File file, String path) {
        try {


            ZipFile zip = new ZipFile(file);
            for (Enumeration<?> entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String name = entry.getName();
                if (Objects.equals(lastName(new File(name)), ".dll")) {
                    logmaker.debug("* 提取natives库dll文件" + name + "中");
                    File dir = new File(path + File.separator + name);
                    if (!dir.exists()) {
                        if (entry.isDirectory()) {
                            dir.mkdir();
                        } else {
                            File unfile = new File(path + File.separator + name);
                            if (!unfile.getParentFile().exists()) unfile.getParentFile().mkdir();
                            unfile.createNewFile();
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
            }
            zip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    // split截取后缀名
    public static String lastName(File file) {
        if (file == null) return null;
        String filename = file.getName();
        if (filename.lastIndexOf(".") == -1) {
            return "";//文件没有后缀名的情况
        }
        //此时返回的是带有 . 的后缀名，
        return filename.substring(filename.lastIndexOf("."));
    }
}
