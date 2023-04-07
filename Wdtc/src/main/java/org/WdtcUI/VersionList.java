package org.WdtcUI;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.WdtcDownload.FileUrl;
import org.WdtcDownload.SelectGameVersion;
import org.WdtcLauncher.FilePath;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class VersionList {
    private static final File v_m = new File(FilePath.getVersionManifestJson());
    private static final Logger logmaker = Logger.getLogger(VersionList.class);
    private static final Stage stage = new Stage();
    private static final ScrollPane sp = new ScrollPane();
    private static final VBox vBox = new VBox();
    private static final Scene SCENE = new Scene(sp);
    private static TextField textField;
    private static boolean BMCLAPI = true;
    private static FileUrl fileUrl;

    public VersionList(TextField textField, boolean BMCLAPI) {
        VersionList.BMCLAPI = BMCLAPI;
        VersionList.textField = textField;
        VersionList.fileUrl = new FileUrl(BMCLAPI);
    }

    public void getVersion_List() {
        logmaker.info("* 开始加载版本列表");
        String vm_e = null;
        try {
            if (!v_m.exists()) {
                URL version_manifest_url = new URL(fileUrl.getVersionManifest());
                URLConnection uc = version_manifest_url.openConnection();
                vm_e = IOUtils.toString(uc.getInputStream(), StandardCharsets.UTF_8);
                FileUtils.writeStringToFile(v_m, vm_e, "UTF-8");
            } else {
                vm_e = FileUtils.readFileToString(v_m, "UTF-8");
            }
        } catch (IOException e) {
            logmaker.error("* 出现错误,可能是网络错误");
            ErrorWin.setErrorWin(e);
        }
        try {
            JSONObject vm_e_j = JSONObject.parseObject(vm_e);
            JSONArray version_list = vm_e_j.getJSONArray("versions");
            for (int i = 0; i < version_list.size(); i++) {
                String version_id = version_list.getJSONObject(i).getString("id");
                Button button = new Button(version_id);
                button.setMaxSize(100, 50);
                vBox.getChildren().addAll(button);
                button.setOnAction(mouseEvent -> {
                    vBox.getChildren().clear();
                    stage.close();
                    if (BMCLAPI) {
                        textField.setText("BMCLAPI下载加速已开启");
                    } else {
                        textField.setText("开始下载");
                    }
                    Thread thread = new Thread(() -> {
                        try {

                            new SelectGameVersion(button.getText(), textField, BMCLAPI).selectversion();
                        } catch (Exception e) {
                            ErrorWin.setErrorWin(e);
                        }
                    });
                    thread.start();

                });
            }
        } catch (NullPointerException e) {
            ErrorWin.setErrorWin(e);
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
    }
}
