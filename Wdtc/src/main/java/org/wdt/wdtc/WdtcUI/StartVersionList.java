package org.wdt.wdtc.WdtcUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModList;
import org.wdt.wdtc.launch.GetGamePath;
import org.wdt.wdtc.launch.LauncherGame;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class StartVersionList {

    private static final Logger logmaker = getWdtcLogger.getLogger(StartVersionList.class);
    private final Label start_label;
    private final TextField textField;

    public StartVersionList(Label start_label, TextField textField) {
        this.start_label = start_label;
        this.textField = textField;
    }

    public void getStartList(Stage MainStage) {
        Stage stage = new Stage();
        VBox vBox = new VBox();
        ScrollPane sp = new ScrollPane();
        logmaker.info("* 开始加载版本列表");
        GetGamePath GetPath = new GetGamePath(AboutSetting.GetDefaultGamePath());
        File version_path = new File(GetPath.getGameVersionPath());
        File[] files = version_path.listFiles();
        if (Objects.nonNull(files)) {
            for (File file2 : files) {
                Button button = new Button(file2.getName());
                vBox.getChildren().add(button);
                button.setMaxSize(100, 50);
                button.setOnAction(event -> {
                    vBox.getChildren().clear();
                    stage.close();
                    try {
                        start_label.setText("\t\t\t\t开始启动");
                        logmaker.info("* 开始启动");
                        Launcher launcher = new Launcher(button.getText());
                        ModList.getModTask(launcher);
                        new Thread(() -> new LauncherGame(launcher).LauncherVersion()).start();
                        textField.setText(FilePath.getStarterBat().getCanonicalPath());
                    } catch (IOException e) {
                        ErrorWin.setErrorWin(e);
                    }

                });
            }
            sp.setContent(vBox);
            stage.setHeight(500);
            stage.setWidth(500);
            stage.getIcons().add(new Image("ico.jpg"));
            stage.setTitle("Start Version List");
            stage.setScene(new Scene(sp));
            stage.setResizable(false);
            stage.initOwner(MainStage);
            stage.initModality(Modality.WINDOW_MODAL);
            logmaker.info("* 版本列表加载成功");
            stage.show();
            stage.setOnCloseRequest(windowEvent -> vBox.getChildren().clear());
        } else {
            VersionDirNull.setNullWin(MainStage);
        }
    }
}
