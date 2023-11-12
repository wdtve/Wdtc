package org.wdt.wdtc.ui.window;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.core.auth.User;
import org.wdt.wdtc.core.auth.accounts.Accounts;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.launch.LaunchGame;
import org.wdt.wdtc.core.launch.LaunchProcess;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.manger.GameDirectoryManger;
import org.wdt.wdtc.core.manger.VMManger;
import org.wdt.wdtc.core.utils.ThreadUtils;
import org.wdt.wdtc.core.utils.URLUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.ui.window.user.NewUserWindows;

import java.io.IOException;
import java.util.Objects;

public class HomeWindow {
  private static final Logger logmaker = WdtcLogger.getLogger(HomeWindow.class);
  private Launcher launcher = Launcher.getPreferredLauncher();

  public HomeWindow(Launcher launcher) {
    this.launcher = launcher;
  }

  public HomeWindow() {
  }


  private static JFXButton getSettingButton(Stage MainStage) {
    JFXButton setting = new JFXButton("设置");
    setting.setPrefSize(128, 46);
    setting.setOnMousePressed(event -> {
      if (event.isControlDown()) {
        URLUtils.openSomething(FileManger.getWdtcCache());
      } else {
        try {
          SettingWindow.setSettingWin(MainStage);
        } catch (IOException e) {
          ExceptionWindow.setErrorWin(e);
        }
      }
    });
    return setting;
  }

  private static void NoUser(Stage MainStage) {
    NewUserWindows windows = new NewUserWindows(MainStage);
    windows.setTitle("您还没有账户呢!");
    windows.setType(Accounts.AccountsType.Offline);
    windows.show();
  }

  public void setHome(Stage MainStage) {
    AnchorPane pane = new AnchorPane();
    WindwosSizeManger windwosSizeManger = new WindwosSizeManger(MainStage);
    MainStage.setTitle(Consoler.getWindowsTitle());
    VBox Menu = new VBox();
    Menu.setPrefSize(128, 450);
    JFXButton home = new JFXButton("首页");
    home.setPrefSize(128, 46);

    JFXButton UserWindow = new JFXButton("修改账户");
    UserWindow.setPrefSize(128, 46);
    UserWindow.setOnAction(event -> {
      NewUserWindows windows = new NewUserWindows(MainStage);
      windows.setTitle("注册账户");
      windows.show();
    });

    JFXButton downgame = new JFXButton("下载游戏");
    downgame.setDisable(!URLUtils.isOnline());
    downgame.setPrefSize(128, 46);
    downgame.setOnAction(event -> GameVersionListWindow.setWindowScene(MainStage));

    JFXButton startgame = new JFXButton("选择版本");
    startgame.setLayoutY(69);
    startgame.setPrefSize(128, 46);
    startgame.setOnAction(event -> {
      VersionChooseWindow choose = new VersionChooseWindow(Objects.requireNonNullElseGet(launcher, GameDirectoryManger::new));
      choose.setWindow(MainStage);
    });

    JFXButton VersionSetting = new JFXButton("版本设置");
    VersionSetting.setPrefSize(128, 46);
    if (launcher != null) {
      VersionSetting.setDisable(false);
      VersionSettingWindow windows = new VersionSettingWindow(launcher, MainStage);
      VersionSetting.setOnAction(event -> windows.setWindow());
    } else {
      VersionSetting.setDisable(true);
    }

    JFXButton setting = getSettingButton(MainStage);

    JFXButton github = new JFXButton("GitHub");
    github.setPrefSize(128, 46);
    github.setOnAction(event -> {
      URLUtils.openSomething("https://github.com/Wd-t/Wdtc");
    });

    Label name = new Label("Wdtc\n" + VMManger.getLauncherVersion());
    name.setLayoutX(17.0);
    name.setLayoutY(161.0);
    Label readme = new Label("一个简单到不能再简单的我的世界Java版启动器");
    readme.setLayoutX(180.0);
    readme.setLayoutY(180.0);
    readme.getStyleClass().add("readme");
    JFXButton LaunchGameButton = new JFXButton();
    if (launcher != null) {
      LaunchGameButton.setText("启动游戏\n" + launcher.getVersionNumber());
    } else {
      LaunchGameButton.setText("当前无游戏版本");
    }
    LaunchGameButton.setPrefSize(227, 89);
    LaunchGameButton.setLayoutX(335);
    LaunchGameButton.setLayoutY(316);
    LaunchGameButton.getStyleClass().add("BackGroundWriteButton");
    AnchorPane.setBottomAnchor(LaunchGameButton, 30.0);
    AnchorPane.setRightAnchor(LaunchGameButton, 30.0);
    LaunchGameButton.setOnAction(event -> {
      if (launcher != null) {
        if (User.isExistUserJsonFile()) {
          ThreadUtils.startThread(() -> {
            try {
              LaunchGame launch = LaunchGame.create(launcher);
              LaunchProcess launchProcess = launch.getLaunchProcess();
              launchProcess.setSetUIText(string -> ExceptionWindow.setWin(string, "Launch Error"));
              launchProcess.startLaunchGame();
            } catch (IOException e) {
              ExceptionWindow.setErrorWin(e);
            }
          }).setName("Launch Game");
        } else {
          NoUser(MainStage);
        }
      } else {
        GameVersionListWindow.setWindowScene(MainStage);
      }
    });
    Menu.getChildren().addAll(home, UserWindow, downgame, startgame, VersionSetting, setting, github);
    Menu.getStyleClass().add("BlackBorder");
    AnchorPane.setTopAnchor(Menu, 0.0);
    AnchorPane.setBottomAnchor(Menu, 0.0);
    AnchorPane.setLeftAnchor(Menu, 0.0);
    pane.getChildren().addAll(Menu, LaunchGameButton);
    windwosSizeManger.ModifyWindwosSize(pane, readme);
    Consoler.setStylesheets(pane);
    pane.setBackground(Consoler.getBackground());
    Scene scene = new Scene(pane, 600, 450);
    MainStage.setScene(scene);
    if (!User.isExistUserJsonFile()) {
      NoUser(MainStage);
    }
  }
}
