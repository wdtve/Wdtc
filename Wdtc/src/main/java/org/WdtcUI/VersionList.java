package org.WdtcUI;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.WdtcDownload.DownloadMain;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class VersionList {
    private static final File vm_j = new File("WdtcCore/ResourceFile/Download/version_manifest.json");
    private static final File f_u = new File("WdtcCore/ResourceFile/Download/file_url.json");
    private static final Logger logmaker = Logger.getLogger(VersionList.class);
    private static final Stage stage = new Stage();
    private static final ScrollPane sp = new ScrollPane();
    private static final VBox vBox = new VBox();
    private static final Scene SCENE = new Scene(sp);
    private static TextField textField;
    private static boolean BMCLAPI = true;

    public VersionList(TextField textField, boolean BMCLAPI) {
        VersionList.BMCLAPI = BMCLAPI;
        VersionList.textField = textField;
    }

    public void getVersion_List() {
        try {
            logmaker.info("* 开始加载版本列表");
            String f_e = FileUtils.readFileToString(f_u);
            JSONObject f_e_j = JSONObject.parseObject(f_e);
            String vm_e = null;
            try {
                if (BMCLAPI) {
                    JSONObject BMCLAPI_J = f_e_j.getJSONObject("BMCLAPI");
                    URL version_manifest_url = new URL(BMCLAPI_J.getString("version_manifest"));
                    URLConnection uc = version_manifest_url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
                    vm_e = in.readLine();
                } else {
                    URL version_manifest_url = new URL(f_e_j.getString("version_manifest"));
                    URLConnection uc = version_manifest_url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
                    vm_e = in.readLine();
                }
            } catch (IOException e) {
                Controller.MainStage.setTitle("下载游戏 (无网络)");
                logmaker.error("* 出现错误,可能是网络错误");
                ErrorWin.setErrorWin(e);
            }
            JSONObject vm_e_j = JSONObject.parseObject(vm_e);
            JSONArray version_list = vm_e_j.getJSONArray("versions");

            for (int i = 0; i < version_list.size(); i++) {
                String version_id = version_list.getJSONObject(i).getString("id");
                Button button = new Button(version_id);
                button.setMaxSize(100, 50);
                vBox.getChildren().addAll(button);
                button.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    vBox.getChildren().clear();
                    stage.close();
                    if (BMCLAPI) {
                        textField.setText("BMCLAPI下载加速已开启");
                    } else {
                        textField.setText("开始下载");
                    }
                    Thread thread = new Thread(() -> {
                        try {
                            DownloadMain.main(button.getText(), textField, BMCLAPI);
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread.start();

                });
            }
            stage.setTitle("Version List");
            stage.setWidth(500);
            stage.setHeight(500);
            stage.getIcons().add(new Image("ico.jpg"));
            sp.setContent(vBox);
            stage.setScene(SCENE);
            stage.setResizable(false);
            logmaker.info("* 版本列表加载成功");
            stage.show();
            stage.setOnCloseRequest(windowEvent -> vBox.getChildren().clear());
        } catch (IOException e) {
            logmaker.error("* 出现错误!");
            e.printStackTrace();
        }

    }
}
