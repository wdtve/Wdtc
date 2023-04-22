package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.WdtcLauncher.ExtractFile;
import org.wdt.WdtcLauncher.FilePath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DownloadGameResourceFile extends DownloadTask {
    private static final Logger LOGGER = Logger.getLogger(DownloadGameResourceFile.class);
    private static final File resources_zip = new File(FilePath.getResources_zip());
    private static String list;

    public DownloadGameResourceFile(File game_dir, boolean BMCL) throws IOException {
        super(BMCL);
        JSONObject a_e_j = JSON.parseObject(FileUtils.readFileToString(game_dir, "UTF-8"));
        JSONObject object_j = a_e_j.getJSONObject("objects");
        DownloadGameResourceFile.list = object_j.values().toString();
    }

    public static void unzipByFile(File file, String path) {
        try {


            ZipFile zip = new ZipFile(file);
            for (Enumeration<?> entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String name = entry.getName();
                File dir = new File(path + File.separator + name);
                if (!dir.exists()) {
                    LOGGER.debug("* 解压 " + name + " 中");
                    if (entry.isDirectory()) {
                        dir.mkdirs();
                    } else {
                        File unfile = new File(path + File.separator + name);
                        if (!unfile.getParentFile().exists()) unfile.getParentFile().mkdirs();
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
            zip.close();
        } catch (Exception e) {
            LOGGER.error("* 文件提取发生错误!");

        }
    }

    public void gethash() throws Exception {
        JSONArray l_e_j = JSON.parseArray(list);
        CountDownLatch countDownLatch = new CountDownLatch(l_e_j.size());
        if (resources_zip.exists()) {
            unzipByFile(resources_zip, GetGamePath.getGameAssetsdir());
        } else {
            LOGGER.error("* 资源文件压缩包不存在!");
        }
        for (int i = 0; i < l_e_j.size(); i++) {
            String hash = l_e_j.getJSONObject(i).getString("hash");
            new Thread(StartDownloadHashTask(hash, countDownLatch)).start();

        }
        countDownLatch.await();
        Thread thread = new Thread(() -> {
            if (!resources_zip.exists()) {
                try {
                    ExtractFile.compressedFile(GetGamePath.getGameObjects(), resources_zip.getCanonicalPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}

