package org.wdt.WdtcUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.AboutSetting;
import org.wdt.Launcher;
import org.wdt.download.SelectGameVersion;
import org.wdt.launch.GetGamePath;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CompletionGame {
    private static final Logger logmaker = Logger.getLogger(CompletionGame.class);
    private static final Stage stage = new Stage();

    private static final VBox V_BOX = new VBox();
    private static final ScrollPane SCROLL_PANE = new ScrollPane();
    private static final Scene SCENE = new Scene(SCROLL_PANE);


    public static void CompletionGameVersion(Stage MainStage) {
        try {
            GetGamePath getGamePath = new GetGamePath(AboutSetting.GetDefaultGamePath());
            File version_path = new File(getGamePath.getGameVersionPath());
            File[] files = version_path.listFiles();
            if (Objects.nonNull(files)) {

                for (File file2 : files) {
                    Button button = new Button(file2.getName());
                    V_BOX.getChildren().add(button);
                    button.setMaxSize(100, 50);
                    button.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                        V_BOX.getChildren().clear();
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
                SCROLL_PANE.setContent(V_BOX);
                stage.setHeight(500);
                stage.setWidth(500);
                stage.getIcons().add(new Image("ico.jpg"));
                stage.setTitle("Start Version List");
                stage.setScene(SCENE);
                stage.setResizable(false);
                stage.show();
                stage.setOnCloseRequest(windowEvent -> V_BOX.getChildren().clear());
            } else {
                VersionDirNull.setNullWin(MainStage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
