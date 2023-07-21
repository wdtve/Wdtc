package org.wdt.wdtc.WdtcUI;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.platform.Starter;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.IOException;

public class HomeWin {
    private static final Logger logmaker = getWdtcLogger.getLogger(HomeWin.class);

    public static void setHome(Stage MainStage) {
        MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion());
        Pane Menu = new Pane();
        Menu.setPrefSize(64.0, 450);
        JFXButton home = new JFXButton("首页");
        home.setPrefSize(64.0, 23.0);
        JFXButton downgame = new JFXButton("下载游戏");
        downgame.setLayoutY(23.0);
        downgame.setPrefSize(64.0, 23.0);
        downgame.setOnAction(event -> NewDownloadWin.SetWin(MainStage));
        JFXButton startgame = new JFXButton("启动游戏");
        startgame.setLayoutY(46.0);
        startgame.setPrefSize(64.0, 23.0);
        startgame.setOnAction(event -> LauncherWin.setLauncherWin(MainStage));
        JFXButton github = new JFXButton("GitHub");
        github.setLayoutY(427.0);
        github.setPrefSize(64.0, 23.0);
        github.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("cmd.exe /C start https://github.com/Wd-t/Wdtc");
            } catch (IOException | RuntimeException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        JFXButton setting = new JFXButton("设置");
        setting.setLayoutY(404.0);
        setting.setPrefSize(64.0, 23.0);
        setting.setOnMousePressed(event -> {
            if (event.isControlDown()) {
                try {
                    Runtime.getRuntime().exec("cmd.exe /c start " + FilePath.getWdtcConfig());
                    logmaker.info("* 配置目录" + FilePath.getWdtcConfig() + "已打开");
                } catch (IOException e) {
                    ErrorWin.setErrorWin(e);
                }
            } else {
                try {
                    SettingWin.setSettingWin(MainStage);
                } catch (IOException e) {
                    ErrorWin.setErrorWin(e);
                }
            }
        });
        Label name = new Label("Wdtc\n" + Starter.getLauncherVersion());
        name.setLayoutX(17.0);
        name.setLayoutY(161.0);
        Label readme = new Label("一个简单到不能再简单的我的世界Java版启动器");
        readme.setLayoutX(172.0);
        readme.setLayoutY(166.0);
        WindwosSize windwosSize = new WindwosSize(MainStage);
        windwosSize.windwosSize(Menu, home, downgame, startgame, github, setting, name);
        Pane pane = new Pane();
        windwosSize.windwosSize(pane, readme, Menu);
        pane.getStylesheets().addAll(Consoler.getCssFile());
        pane.setBackground(Consoler.getBackground());
        Scene scene = new Scene(pane, 600, 450);
        MainStage.setScene(scene);
    }
}
