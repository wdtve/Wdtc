package org.WdtcDownload.DownloadResourceFile;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.github.axet.wget.WGet;
import javafx.scene.control.TextField;
import org.WdtcDownload.FileUrl;
import org.WdtcDownload.SetFilePath.SetPath;
import org.WdtcLauncher.ExtractFiles.ExtractFile;
import org.WdtcLauncher.FilePath;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class GetHash {
    private static final Logger LOGGER = Logger.getLogger(GetHash.class);
    private static final File resources_zip = new File(FilePath.getResources_zip());
    private static String list;
    private static boolean BMCL;
    private final TextField label;

    public GetHash(String list, boolean BMCL, TextField label) {
        GetHash.list = list;
        this.label = label;
        GetHash.BMCL = BMCL;
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
            unzipByFile(resources_zip, SetPath.getGameAssetsdir());
        } else {
            LOGGER.error("* 资源文件压缩包不存在!");
        }
        for (int i = 0; i < l_e_j.size(); i++) {
            String hash = l_e_j.getJSONObject(i).getString("hash");
            String hash_t = hash.substring(0, 2);
            if (BMCL) {
                File hash_path = new File(SetPath.getGameAssetsdir() + "objects\\" + hash_t + "\\" + hash);
                URL hash_url = new URL(FileUrl.getBmclapiAssets() + hash_t + "/" + hash);
                if (!hash_path.exists()) {
                    Thread thread = new Thread(() -> {
                        LOGGER.info("* " + hash_url + " 开始下载");
                        new WGet(hash_url, hash_path).download();
                        LOGGER.info("* " + hash + " 下载完成");
                        countDownLatch.countDown();
                    });
                    thread.start();
                }
            } else {
                File hash_path = new File(SetPath.getGameAssetsdir() + "objects\\" + hash_t + "\\" + hash);
                URL hash_url = new URL(FileUrl.getMojangAssets() + hash_t + "/" + hash);
                if (!hash_path.exists()) {
                    Thread thread1 = new Thread(() -> {
                        LOGGER.info("* " + hash_url + " 开始下载");
                        new WGet(hash_url, hash_path).download();
                        LOGGER.info("* " + hash + " 下载完成");
                        countDownLatch.countDown();
                    });
                    thread1.start();
                }
            }
        }
        countDownLatch.await();
        label.setText("下载完成");
        LOGGER.info("下载完成");
        Thread thread = new Thread(() -> {
            if (!resources_zip.exists()) {
                try {
                    ExtractFile.compressedFile(SetPath.getGameObjects(), resources_zip.getCanonicalPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}

