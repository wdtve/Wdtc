package org.wdt.wdtc.ui;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.wdt.wdtc.download.InstallGameVersion;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.ThreadUtils;


public class DownloadGameWindows {
    private final Launcher launcher;

    public DownloadGameWindows(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setDownGameWin(Stage MainStage) {
        WindwosSize size = new WindwosSize(MainStage);
        MainStage.setTitle(Consoler.getWindowsTitle("下载游戏"));
        AnchorPane pane = new AnchorPane();
        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> {
            HomeWindow win = new HomeWindow();
            win.setHome(MainStage);
        });
        back.setStyle("-fx-border-color: #000000");
        TextField textField = new TextField();
        textField.setPromptText("三个阶段");
        textField.setLayoutX(218.0);
        textField.setLayoutY(320.0);
        textField.setPrefSize(200, 10);
        Label time = new Label("下载时间不会太长");
        Label status_bar = new Label("下面是状态栏");
        time.setLayoutX(241.0);
        time.setLayoutY(160.0);
        status_bar.setLayoutX(253.0);
        status_bar.setLayoutY(305.0);
        Button bmclHome = new Button("BMCLAPI");
        bmclHome.setOnAction(event -> {
            PlatformUtils.StartSomething("https://bmclapidoc.bangbang93.com/");
        });
        AnchorPane.setRightAnchor(bmclHome, 0.0);
        AnchorPane.setTopAnchor(bmclHome, 0.0);
        Label read_bmcl = new Label("国内快速下载源→");
        read_bmcl.setLayoutX(429.0);
        read_bmcl.setLayoutY(4.0);
        textField.setText(launcher.getVersionNumber() + "开始下载,下载源: " + launcher.getDownloadSourceKind());
        ThreadUtils.StartThread(() -> {
            new InstallGameVersion(launcher, textField).InstallGame();
        }).setName("Download Game");
        pane.setBackground(Consoler.getBackground());
        size.ModifyWindwosSize(pane, back, time, status_bar, bmclHome, read_bmcl, textField);
        Scene down_scene = new Scene(pane);
        MainStage.setScene(down_scene);
    }
}
