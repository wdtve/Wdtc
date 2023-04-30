package org.wdt.WdtcUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.AboutSetting;
import org.wdt.GetGamePath;
import org.wdt.Launcher;

import java.io.File;
import java.io.IOException;

public class DeleteVersion {
    private static final Logger logmaker = Logger.getLogger(DeleteVersion.class);
    private static final Stage Delete = new Stage();

    private static final VBox V_BOX = new VBox();
    private static final ScrollPane SCROLL_PANE = new ScrollPane();
    private static final Scene SCENE = new Scene(SCROLL_PANE);


    public static void getStartList() {
        logmaker.info("* 开始加载版本列表");
        //foreach遍历数组
        try {
            GetGamePath GetPath = new GetGamePath(AboutSetting.GetDefaultGamePath());
            File version_path = new File(GetPath.getGameVersionPath());
            File[] files = version_path.listFiles();
            for (File file2 : files) {
                Button button = new Button(file2.getName());
                V_BOX.getChildren().add(button);
                button.setMaxSize(100, 50);
                button.setOnAction(event -> {
                    try {
                        V_BOX.getChildren().clear();
                        Delete.close();
                        Launcher launcher = new Launcher(button.getText(), AboutSetting.GetDefaultGamePath());
                        FileUtils.deleteDirectory(new File(launcher.getVersionPath()));
                        logmaker.info("* 版本删除成功");
                    } catch (IOException e) {
                        ErrorWin.setErrorWin(e);
                    }

                });
            }
            SCROLL_PANE.setContent(V_BOX);
            Delete.setHeight(500);
            Delete.setWidth(500);
            Delete.getIcons().add(new Image("ico.jpg"));
            Delete.setTitle("Start Version List");
            Delete.setScene(SCENE);
            Delete.setResizable(false);
            logmaker.info("* 版本列表加载成功");
            Delete.show();
            Delete.setOnCloseRequest(windowEvent -> V_BOX.getChildren().clear());
        } catch (NullPointerException e) {
            Label label = new Label("您没有游戏版本,请去下载");
            Pane pane = new Pane();
            pane.prefHeight(400.0);
            pane.prefWidth(600.0);
            label.setLayoutX(217.0);
            label.setLayoutY(193.0);
            pane.getChildren().add(label);
            Scene scene = new Scene(pane, 600, 400);
            Delete.setScene(scene);
            Delete.setResizable(false);
            Delete.getIcons().add(new Image("/ico.jpg"));
            Delete.setTitle("版本文件夹为空");
            Delete.show();
            logmaker.error("* 版本文件夹为空");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
