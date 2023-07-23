package org.wdt.wdtc.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.GetGamePath;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DeleteVersion {
    private static final Logger logmaker = getWdtcLogger.getLogger(DeleteVersion.class);


    public static void getStartList(Stage MainStage) throws IOException {
        Stage Delete = new Stage();
        VBox vbox = new VBox();
        ScrollPane sp = new ScrollPane();
        logmaker.info("* 开始加载版本列表");
        GetGamePath GetPath = new GetGamePath(AboutSetting.GetDefaultGamePath());
        File version_path = new File(GetPath.getGameVersionPath());
        File[] files = version_path.listFiles();
        if (Objects.nonNull(files)) {
            for (File file2 : files) {
                Button button = new Button(file2.getName());
                vbox.getChildren().add(button);
                button.setMaxSize(100, 50);
                button.setOnAction(event -> {
                    try {
                        vbox.getChildren().clear();
                        Delete.close();
                        Launcher launcher = new Launcher(button.getText(), AboutSetting.GetDefaultGamePath());
                        FileUtils.deleteDirectory(new File(launcher.getVersionPath()));
                        logmaker.info("* 版本删除成功");
                    } catch (IOException e) {
                        ErrorWin.setErrorWin(e);
                    }

                });
            }
            sp.setContent(vbox);
            Delete.setHeight(500);
            Delete.setWidth(500);
            Delete.getIcons().add(new Image("ico.jpg"));
            Delete.setTitle("Start Version List");
            Delete.setScene(new Scene(sp));
            Delete.setResizable(false);
            Delete.initOwner(MainStage);
            Delete.initModality(Modality.WINDOW_MODAL);
            logmaker.info("* 版本列表加载成功");
            Delete.show();
            Delete.setOnCloseRequest(windowEvent -> vbox.getChildren().clear());
        } else {
            VersionDirNull.setNullWin(MainStage);
        }
    }
}
