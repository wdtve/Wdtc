package org.WdtcDownload.DownloadLib;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import javafx.concurrent.Task;
import javafx.scene.control.TextField;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

//1.19-
public class GetLibUrl {
    private static final File s_j = new File("WdtcCore/ResourceFile/Download/starter.json");
    private static final File f_u = new File("WdtcCore/ResourceFile/Download/file_url.json");
    private static final Logger logmaker = Logger.getLogger(GetLibUrl.class);
    private static File v_j;
    private static String v_path;
    private static String version;
    private static boolean BMCLAPI;
    private static TextField label;
    private static String BMCLAPI_Libraries;
    private static String MOJANG_Libraries;


    public GetLibUrl(File v_j, String v_path, String version, boolean BMCLAPI, TextField label) throws IOException {
        GetLibUrl.v_j = v_j;
        GetLibUrl.v_path = v_path;
        GetLibUrl.version = version;
        GetLibUrl.BMCLAPI = BMCLAPI;
        GetLibUrl.label = label;
        String f_e = FileUtils.readFileToString(f_u);
        JSONObject f_e_j = JSONObject.parseObject(f_e);
        JSONObject BMCLAPI_J = f_e_j.getJSONObject("BMCLAPI");
        GetLibUrl.BMCLAPI_Libraries = BMCLAPI_J.getString("Libraries");
        JSONObject MOJANG_J = f_e_j.getJSONObject("MOJANG");
        GetLibUrl.MOJANG_Libraries = MOJANG_J.getString("Libraries");
    }

    public static File readnatives_lib(JSONObject lib_j) throws IOException {
        String s_e = FileUtils.readFileToString(s_j);
        JSONObject s_e_J = JSONObject.parseObject(s_e);
        String game_lib_path = s_e_J.getString("game_lib_path");
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
        String s_e = FileUtils.readFileToString(s_j);
        JSONObject s_e_J = JSONObject.parseObject(s_e);
        String game_lib_path = s_e_J.getString("game_lib_path");
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
        String s_e = FileUtils.readFileToString(s_j);
        JSONObject s_e_J = JSONObject.parseObject(s_e);
        String game_lib_path = s_e_J.getString("game_lib_path");
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject artifact_j = downloads_j.getJSONObject("artifact");
        String lib_path = game_lib_path + artifact_j.getString("path");
        lib_path = lib_path.replaceAll("/", "\\\\");
        return new File(lib_path);

    }

    public static URL readlib_url(JSONObject lib_j) throws IOException {
        String s_e = FileUtils.readFileToString(s_j);
        JSONObject s_e_J = JSONObject.parseObject(s_e);
        String game_lib_path = s_e_J.getString("game_lib_path");
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

    public void readdown() throws IOException, InterruptedException, RuntimeException {
        String library_p = v_path + "natives-windows-x86_64";
        File lib_pay = new File(library_p);
        if (!lib_pay.exists()) {
            lib_pay.mkdirs();
        }
        String v_e = FileUtils.readFileToString(v_j);
        JSONObject v_e_j = JSONObject.parseObject(v_e);
        JSONArray libraries_j = v_e_j.getJSONArray("libraries");
        for (int i = 0; i < libraries_j.size(); i++) {
            JSONObject lib_j = libraries_j.getJSONObject(i);
            JSONObject natives_j = lib_j.getJSONObject("natives");
            if (natives_j == null) {
                JSONArray rules = lib_j.getJSONArray("rules");
                if (rules == null) {
                    Task<Void> voidTask = new Task<Void>() {
                        @Override
                        protected Void call() throws InterruptedException {
                            try {
                                if (!readlib_path(lib_j).exists()) {
                                    new WGet(readlib_url(lib_j), readlib_path(lib_j)).download();
                                }
//                            System.out.println(readlib_url(lib_j));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return null;
                        }

                    };
                    new Thread(voidTask).start();
                } else {
                    JSONObject action_j = rules.getJSONObject(rules.size() - 1);
                    String action = action_j.getString("action");
                    JSONObject os_j = action_j.getJSONObject("os");
                    String os_n = os_j.getString("name");
                    if (Objects.equals(action, "disallow") && Objects.equals(os_n, "osx")) {
                        Task<Void> voidTask = new Task<Void>() {
                            @Override
                            protected Void call() throws InterruptedException {

                                try {
                                    if (!readlib_path(lib_j).exists()) {
                                        new WGet(readlib_url(lib_j), readlib_path(lib_j)).download();
                                    }
//                                System.out.println(readlib_url(lib_j));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                return null;
                            }

                        };
                        new Thread(voidTask).start();
//                        TimeUnit.MICROSECONDS.sleep(160);
                    } else if (Objects.equals(action, "allow") && Objects.equals(os_n, "windows")) {
                        Task<Void> voidTask = new Task<Void>() {
                            @Override
                            protected Void call() throws InterruptedException {
                                try {
                                    if (!readlib_path(lib_j).exists()) {
                                        new WGet(readlib_url(lib_j), readlib_path(lib_j)).download();
                                    }
//                                System.out.println(readlib_url(lib_j));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
//                                TimeUnit.MICROSECONDS.sleep(160);
                                return null;
                            }
                        };
                        new Thread(voidTask).start();
                    }

                }
            } else {
                String natives_name = natives_j.getString("windows");
                if (natives_name != null) {
                    Task<Void> voidTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            try {
                                if (!readnatives_lib(lib_j).exists()) {
                                    new WGet(readnatives_url(lib_j), readnatives_lib(lib_j)).download();
                                }
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
        Thread thread = new Thread(() -> {
            try {
                File log4j2 = new File("WdtcCore/ResourceFile/Download/log4j2.xml");
                File v_log = new File(v_path + "log4j2.xml");
                if (!v_log.exists()) {
                    FileUtils.copyFile(log4j2, v_log);
                }
            } catch (RuntimeException | IOException e) {
                logmaker.error("* logej.xml不存在或路径错误!");
            }
            JSONObject downloads_j = v_e_j.getJSONObject("downloads");
            JSONObject client = downloads_j.getJSONObject("client");
            try {
                URL jar_url = new URL(client.getString("url"));
                if (!new File(v_path + version + ".jar").exists()) {
                    new WGet(jar_url, new File(v_path + version + ".jar")).download();
                }
            } catch (MalformedURLException | RuntimeException e) {
                e.printStackTrace();
            }
        });
        thread.start();

    }
}

