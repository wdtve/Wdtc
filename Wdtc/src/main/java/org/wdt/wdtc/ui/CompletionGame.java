package org.wdt.wdtc.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.download.SelectGameVersion;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.GetGamePath;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CompletionGame {
    private static final Logger logmaker = getWdtcLogger.getLogger(CompletionGame.class);


    public static void CompletionGameVersion(Stage MainStage) {
        Stage stage = new Stage();
        VBox vBox = new VBox();
        ScrollPane sp = new ScrollPane();
        GetGamePath getGamePath = new GetGamePath(AboutSetting.GetDefaultGamePath());
        File version_path = new File(getGamePath.getGameVersionPath());
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
                        Launcher version = new Launcher(button.getText());
                        SelectGameVersion gameVersion = new SelectGameVersion(version);
                        gameVersion.DownloadGame();
                        logmaker.info("* 版本补全完成");
                    } catch (IOException | InterruptedException | RuntimeException e) {
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
            stage.show();
            stage.setOnCloseRequest(windowEvent -> vBox.getChildren().clear());
        } else {
            VersionDirNull.setNullWin(MainStage);
        }
    }
}
