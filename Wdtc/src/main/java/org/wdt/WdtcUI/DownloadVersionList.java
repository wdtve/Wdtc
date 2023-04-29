package org.wdt.WdtcUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.AboutSetting;
import org.wdt.WdtcDownload.SelectGameVersion;
import org.wdt.WdtcDownload.VersionList;

import java.io.IOException;
import java.util.List;

public class DownloadVersionList {
    private static final Logger logmaker = Logger.getLogger(DownloadVersionList.class);
    private static final Stage stage = new Stage();
    private static final ScrollPane sp = new ScrollPane();
    private static final VBox vBox = new VBox();
    private static final Scene SCENE = new Scene(sp);
    private static boolean BMCLAPI;
    private static TextField textField;
    private static VersionList versionList;

    public DownloadVersionList(TextField textField) throws IOException {
        DownloadVersionList.textField = textField;
        versionList = new VersionList(AboutSetting.GetBmclSwitch());
        BMCLAPI = AboutSetting.GetBmclSwitch();
    }

    public void getVersion_List() {
        logmaker.info("* 开始加载版本列表");
        List<String> versionIdList = versionList.getVersionList();
        for (String version_id : versionIdList) {
            try {
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
            } catch (NullPointerException e) {
                ErrorWin.setErrorWin(e);
            }
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
