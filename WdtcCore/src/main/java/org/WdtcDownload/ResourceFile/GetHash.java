package org.WdtcDownload.ResourceFile;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import javafx.scene.control.TextField;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class GetHash {
    private static final Logger LOGGER = Logger.getLogger(GetHash.class);
    private static final File f_u = new File("WdtcCore/ResourceFile/Download/file_url.json");
    private static final File s_j = new File("WdtcCore/ResourceFile/Download/starter.json");
    private static final File resources_zip = new File("WdtcCore/ResourceFile/Download/objects.jar");
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

    public void gethash() throws IOException, InterruptedException {
        String s_e = FileUtils.readFileToString(s_j);
        JSONObject s_e_j = JSON.parseObject(s_e);
        String f_e = FileUtils.readFileToString(f_u);
        JSONObject f_e_j = JSON.parseObject(f_e);
        JSONArray l_e_j = JSON.parseArray(list);
        CountDownLatch countDownLatch = new CountDownLatch(l_e_j.size());
        if (resources_zip.exists()) {
            unzipByFile(resources_zip, s_e_j.getString("Game_assetsDir"));
        } else {
            LOGGER.error("* 资源文件压缩包不存在!");
        }
        for (int i = 0; i < l_e_j.size(); i++) {
            String hash = l_e_j.getJSONObject(i).getString("hash");
            String hash_t = hash.substring(0, 2);
            if (BMCL) {
                JSONObject BMCLAPI_J = f_e_j.getJSONObject("BMCLAPI");
                File hash_path = new File(s_e_j.getString("Game_assetsDir") + "objects\\" + hash_t + "\\" + hash);
                URL hash_url = new URL(BMCLAPI_J.getString("Assets") + hash_t + "/" + hash);
                if (!hash_path.exists()) {
                    Thread thread = new Thread(() -> {
                        new WGet(hash_url, hash_path).download();
                        countDownLatch.countDown();
                    });
                    thread.start();
                }
            } else {
                File hash_path = new File(s_e_j.getString("Game_assetsDir") + "objects\\" + hash_t + "\\" + hash);
                URL hash_url = new URL(f_e_j.getString("resources") + hash_t + "/" + hash);
                if (!hash_path.exists()) {
                    Thread thread1 = new Thread(() -> {
                        new WGet(hash_url, hash_path).download();
                        countDownLatch.countDown();
                    });
                    thread1.start();
                }
            }
            if (i == l_e_j.size() - 1) {
                label.setText("下载完成");
            }
        }
        countDownLatch.await();
    }
}

