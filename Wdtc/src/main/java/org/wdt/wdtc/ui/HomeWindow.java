package org.wdt.wdtc.ui;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.auth.Users;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModList;
import org.wdt.wdtc.launch.GetGamePath;
import org.wdt.wdtc.launch.LauncherGame;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.Starter;
import org.wdt.wdtc.ui.users.UsersWin;
import org.wdt.wdtc.utils.ThreadUtils;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.IOException;

public class HomeWindow {
    private static final Logger logmaker = getWdtcLogger.getLogger(HomeWindow.class);
    private final Launcher launcher;

    public HomeWindow(Launcher launcher) {
        this.launcher = launcher;
    }

    public HomeWindow() {
        if (AboutSetting.SettingObject().has("PreferredVersion")) {
            this.launcher = ModList.getModTask(new Launcher(AboutSetting.getPreferredVersion()));
        } else {
            this.launcher = null;
        }
    }


    public void setHome(Stage MainStage) {
        MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion());
        Pane Menu = new Pane();
        Menu.setPrefSize(64.0, 450);
        JFXButton home = new JFXButton("首页");
        home.setPrefSize(64.0, 23.0);

        JFXButton User = new JFXButton("修改账户");
        User.setLayoutY(23.0);
        User.setPrefSize(64, 23);
        User.setOnAction(event -> UsersWin.setUserWin(User.getText(), MainStage));

        JFXButton downgame = new JFXButton("下载游戏");
        downgame.setLayoutY(46);
        downgame.setPrefSize(64.0, 23.0);
        downgame.setOnAction(event -> NewDownloadWin.SetWin(MainStage));

        JFXButton startgame = new JFXButton("选择版本");
        startgame.setLayoutY(69);
        startgame.setPrefSize(64.0, 23.0);
        startgame.setOnAction(event -> {
            GetGamePath path = new GetGamePath();
            VersionChoose choose = new VersionChoose(path);
            choose.setWindow(MainStage);
        });

        JFXButton VersionSetting = new JFXButton("版本设置");
        VersionSetting.setLayoutY(92);
        VersionSetting.setPrefSize(64, 23);
        if (launcher != null) {
            VersionSetting.setDisable(false);
            VersionSettingWindows windows = new VersionSettingWindows(launcher);
            VersionSetting.setOnAction(event -> windows.setWindow(MainStage));
        } else {
            VersionSetting.setDisable(true);
        }

        JFXButton setting = new JFXButton("设置");
        setting.setLayoutY(402.0);
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

        JFXButton github = new JFXButton("GitHub");
        github.setLayoutY(425);
        github.setPrefSize(64.0, 23.0);
        github.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("cmd.exe /C start https://github.com/Wd-t/Wdtc");
            } catch (IOException | RuntimeException e) {
                ErrorWin.setErrorWin(e);
            }
        });

        Label name = new Label("Wdtc\n" + Starter.getLauncherVersion());
        name.setLayoutX(17.0);
        name.setLayoutY(161.0);
        Label readme = new Label("一个简单到不能再简单的我的世界Java版启动器");
        readme.setLayoutX(172.0);
        readme.setLayoutY(166.0);
        JFXButton LaunchGameButton = new JFXButton();
        if (launcher != null) {
            LaunchGameButton.setText("启动游戏\n" + launcher.getVersion());
        } else {
            LaunchGameButton.setText("当前无游戏版本");
        }
        LaunchGameButton.setPrefSize(227, 89);
        LaunchGameButton.setLayoutX(335);
        LaunchGameButton.setLayoutY(316);
        LaunchGameButton.getStyleClass().add("BlackBorder");
        LaunchGameButton.setOnAction(event -> {
            if (launcher != null) {
                if (Users.SetUserJson()) {
                    ThreadUtils.StartThread(() -> {
                        try {
                            LauncherGame launch = new LauncherGame(launcher);
                            LauncherGameWindow launcherGameWindow = new LauncherGameWindow(launch.getStart());
                            launcherGameWindow.startGame();
                        } catch (IOException e) {
                            ErrorWin.setErrorWin(e);
                        }
                    }).setName("Launch Game");
                } else {
                    UsersWin.setUserWin("您当前还没有账户呢!", MainStage);
                }
            } else {
                NewDownloadWin.SetWin(MainStage);
            }
        });
        WindwosSize windwosSize = new WindwosSize(MainStage);
        windwosSize.ModifyWindwosSize(Menu, home, downgame, startgame, github, setting, name, VersionSetting, User);
        Menu.getStyleClass().add("BlackBorder");
        Pane pane = new Pane();
        windwosSize.ModifyWindwosSize(pane, readme, Menu, LaunchGameButton);
        Consoler.setStylesheets(pane);
        pane.setBackground(Consoler.getBackground());
        Scene scene = new Scene(pane, 600, 450);
        MainStage.setScene(scene);
        if (!Users.SetUserJson()) {
            UsersWin.setUserWin("您当前还没有账户呢!", MainStage);
        }
    }
}
