package org.WdtcDownload.CompleteDocument;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import javafx.concurrent.Task;
import org.WdtcDownload.FileUrl;
import org.WdtcDownload.SetFilePath.SetPath;
import org.WdtcLauncher.FilePath;
import org.WdtcLauncher.Version;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

//1.19-
public class CompleteDocument extends CompleteLib {


    private static Version version;

    public CompleteDocument(String version_number, boolean BMCLAPI) throws IOException {
        CompleteDocument.version = new Version(version_number);
        CompleteLib.BMCLAPI = BMCLAPI;
    }



    public void readdown() throws IOException, RuntimeException {
        File lib_pay = new File(version.getVersionNativesPath());
        if (!lib_pay.exists()) {
            lib_pay.mkdir();
        }
        JSONObject v_e_j = JSONObject.parseObject(FileUtils.readFileToString(new File(version.getVersionJson()), "UTF-8"));
        JSONArray libraries_j = v_e_j.getJSONArray("libraries");
        for (int i = 0; i < libraries_j.size(); i++) {
            JSONObject lib_j = libraries_j.getJSONObject(i);
            JSONObject natives_j = lib_j.getJSONObject("natives");
            if (natives_j == null) {
                JSONArray rules = lib_j.getJSONArray("rules");
                if (rules == null) {
                    if (!readlib_path(lib_j).exists()) {
                        Task<Void> voidTask = new Task<>() {
                            @Override
                            protected Void call() throws InterruptedException {
                                try {
                                    new WGet(readlib_url(lib_j), readlib_path(lib_j)).download();
                                } catch (IOException | RuntimeException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                        };
                        new Thread(voidTask).start();
                    }
                } else {
                    JSONObject action_j = rules.getJSONObject(rules.size() - 1);
                    String action = action_j.getString("action");
                    JSONObject os_j = action_j.getJSONObject("os");
                    String os_n = os_j.getString("name");
                    if (Objects.equals(action, "disallow") && Objects.equals(os_n, "osx")) {
                        if (readlib_path(lib_j).exists()) {
                            Task<Void> voidTask = new Task<>() {
                                @Override
                                protected Void call() {

                                    try {
                                        new WGet(readlib_url(lib_j), readlib_path(lib_j)).download();
                                    } catch (IOException | RuntimeException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                            };
                            new Thread(voidTask).start();

                        }
//                        TimeUnit.MICROSECONDS.sleep(160);
                    } else if (Objects.equals(action, "allow") && Objects.equals(os_n, "windows")) {
                        if (!readlib_path(lib_j).exists()) {
                            Task<Void> voidTask = new Task<>() {
                                @Override
                                protected Void call() {
                                    try {
                                        new WGet(readlib_url(lib_j), readlib_path(lib_j)).download();
                                    }
//                                System.out.println(readlib_url(lib_j));
                                    catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return null;
                                }
                            };
                            new Thread(voidTask).start();
                        }
                    }

                }
            } else {
                String natives_name = natives_j.getString("windows");
                if (natives_name != null) {
                    if (!readnatives_lib(lib_j).exists()) {
                        Task<Void> voidTask = new Task<>() {
                            @Override
                            protected Void call() throws Exception {
                                try {

                                    new WGet(readnatives_url(lib_j), readnatives_lib(lib_j)).download();
//                                System.out.println(readnatives_url(lib_j));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                return null;
                            }
                        };
                        new Thread(voidTask).start();
                    }
                }
            }
        }
        Thread thread = new Thread(() -> {
            try {

                File log4j2 = new File(FilePath.getLog4j2());
                File v_log = new File(version.getVersionLog4j2());

                if (!v_log.exists()) {
                    FileUtils.copyFile(log4j2, v_log);
                }
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }
            JSONObject downloads_j = v_e_j.getJSONObject("downloads");
            JSONObject client = downloads_j.getJSONObject("client");
            try {
                URL jar_url = new URL(client.getString("url"));
                if (!new File(version.getVersionJar()).exists()) {
                    new WGet(jar_url, new File(version.getVersionJar())).download();
                }
            } catch (MalformedURLException | RuntimeException e) {
                e.printStackTrace();
            }
        });
        thread.start();


    }

    public void gethash() throws IOException, InterruptedException {
        String v_e = FileUtils.readFileToString(new File(version.getVersionJson()), "UTF-8");
        JSONObject v_e_j = JSONObject.parseObject(v_e);
        JSONObject assetIndex_j = v_e_j.getJSONObject("assetIndex");
        String id = assetIndex_j.getString("id");
        String game_assetsDir_j = SetPath.getGame_assetsdir() + "indexes\\" + id + ".json";
        String a_e = FileUtils.readFileToString(new File(game_assetsDir_j), "UTF-8");
        JSONObject a_e_j = JSON.parseObject(a_e);
        JSONObject object_j = a_e_j.getJSONObject("objects");
        String file_list = object_j.values().toString();
        JSONArray l_e_j = JSONArray.parseArray(file_list);
        CountDownLatch countDownLatch = new CountDownLatch(l_e_j.size());
        for (int i = 0; i < l_e_j.size(); i++) {
            String hash = l_e_j.getJSONObject(i).getString("hash");
            String hash_t = hash.substring(0, 2);
            if (BMCLAPI) {
                String hash_path = SetPath.getGame_assetsdir() + "objects\\" + hash_t + "\\" + hash;
                String hash_url = FileUrl.getBmclapiAssets() + hash_t + "/" + hash;
                if (!new File(hash_path).exists()) {
                    Thread thread = new Thread(() -> {
                        try {
                            new WGet(new URL(hash_url), new File(hash_path)).download();
                            countDownLatch.countDown();
                        } catch (MalformedURLException | RuntimeException e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                    countDownLatch.await();
                }
            } else {

                String hash_path = SetPath.getGame_assetsdir() + "objects\\" + hash_t + "\\" + hash;
                URL hash_url = new URL(FileUrl.getMojangAssets() + hash_t + "/" + hash);
                if (!new File(hash_path).exists()) {
                    Thread thread1 = new Thread(() -> {
                        try {
                            new WGet(hash_url, new File(hash_path)).download();
                            countDownLatch.countDown();
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                    });
                    thread1.start();
                    countDownLatch.await();
                }

            }
        }
    }
}

