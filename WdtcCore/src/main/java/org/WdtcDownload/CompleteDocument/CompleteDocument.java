package org.WdtcDownload.CompleteDocument;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import javafx.concurrent.Task;
import org.WdtcDownload.SetFilePath.SetPath;
import org.WdtcLauncher.Version;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

//1.19-
public class CompleteDocument {
    private static final File f_u = new File("WdtcCore/ResourceFile/Download/file_url.json");
    private static File v_j;
    private static String v_path;
    private static String version_number;
    private static boolean BMCLAPI;
    private static String BMCLAPI_Libraries;
    private static String MOJANG_Libraries;
    private static Version version;

    public CompleteDocument(String version_number, boolean BMCLAPI) throws IOException {
        CompleteDocument.version = new Version(version_number);
        CompleteDocument.v_j = new File(version.getVersion_json());
        CompleteDocument.v_path = version.getVersion_path();
        CompleteDocument.version_number = version_number;
        CompleteDocument.BMCLAPI = BMCLAPI;
        JSONObject f_e_j = JSONObject.parseObject(FileUtils.readFileToString(f_u, "UTF-8"));
        JSONObject BMCLAPI_J = f_e_j.getJSONObject("BMCLAPI");
        CompleteDocument.BMCLAPI_Libraries = BMCLAPI_J.getString("Libraries");
        JSONObject MOJANG_J = f_e_j.getJSONObject("MOJANG");
        CompleteDocument.MOJANG_Libraries = MOJANG_J.getString("Libraries");
    }

    public static File readnatives_lib(JSONObject lib_j) throws IOException {
        String game_lib_path = SetPath.getGame_lib_path();
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject natives_j = lib_j.getJSONObject("natives");
        JSONObject classifiers_j = downloads_j.getJSONObject("classifiers");
        String natives_name = natives_j.getString("windows");
        JSONObject natives_os = classifiers_j.getJSONObject(natives_name);
        String natives_lib_path = game_lib_path + natives_os.getString("path");
        natives_lib_path = natives_lib_path.replaceAll("/", "\\\\");
        return new File(natives_lib_path);
    }

    public static URL readnatives_url(JSONObject lib_j) throws IOException {
        String game_lib_path = SetPath.getGame_lib_path();
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject natives_j = lib_j.getJSONObject("natives");
        JSONObject classifiers_j = downloads_j.getJSONObject("classifiers");
        String natives_name = natives_j.getString("windows");
        JSONObject natives_os = classifiers_j.getJSONObject(natives_name);
        String natives_lib_url = natives_os.getString("url");
        if (BMCLAPI) {
            natives_lib_url = natives_lib_url.replaceAll(MOJANG_Libraries, BMCLAPI_Libraries);
            return new URL(natives_lib_url);
        } else {
            return new URL(natives_lib_url);
        }
    }

    public static File readlib_path(JSONObject lib_j) throws IOException {
        String game_lib_path = SetPath.getGame_lib_path();
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject artifact_j = downloads_j.getJSONObject("artifact");
        String lib_path = game_lib_path + artifact_j.getString("path");
        lib_path = lib_path.replaceAll("/", "\\\\");
        return new File(lib_path);

    }

    public static URL readlib_url(JSONObject lib_j) throws IOException {
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject artifact_j = downloads_j.getJSONObject("artifact");
        String lib_url = artifact_j.getString("url");
        if (BMCLAPI) {
            lib_url = lib_url.replaceAll(MOJANG_Libraries, BMCLAPI_Libraries);
            return new URL(lib_url);
        } else {
            return new URL(lib_url);
        }
    }

    public void readdown() throws IOException, RuntimeException {
        File lib_pay = new File(version.getVersionNativesPath());
        if (!lib_pay.exists()) {
            lib_pay.mkdir();
        }
        JSONObject v_e_j = JSONObject.parseObject(FileUtils.readFileToString(v_j, "UTF-8"));
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

//                            System.out.println(readlib_url(lib_j));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                TimeUnit.MICROSECONDS.sleep(160);
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
                                TimeUnit.MICROSECONDS.sleep(160);
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

                File log4j2 = new File("WdtcCore/ResourceFile/Download/log4j2.xml");
                File v_log = new File(v_path + "log4j2.xml");

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
                if (!new File(v_path + version_number + ".jar").exists()) {
                    new WGet(jar_url, new File(v_path + version_number + ".jar")).download();
                }
            } catch (MalformedURLException | RuntimeException e) {
                e.printStackTrace();
            }
        });
        thread.start();


    }

    public void gethash() throws IOException, InterruptedException {
        String v_e = FileUtils.readFileToString(new File(version.getVersion_json()), "UTF-8");
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
        JSONObject f_e_j = JSON.parseObject(FileUtils.readFileToString(f_u, "UTF-8"));
        for (int i = 0; i < l_e_j.size(); i++) {
            String hash = l_e_j.getJSONObject(i).getString("hash");
            String hash_t = hash.substring(0, 2);
            if (BMCLAPI) {
                JSONObject BMCLAPI_J = f_e_j.getJSONObject("BMCLAPI");
                String hash_path = SetPath.getGame_assetsdir() + "objects\\" + hash_t + "\\" + hash;
                String hash_url = BMCLAPI_J.getString("Assets") + hash_t + "/" + hash;
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
                URL hash_url = new URL(f_e_j.getString("resources") + hash_t + "/" + hash);
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

