package org.wdt.wdtc.ui.image;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.wdt.wdtc.core.download.InstallGameVersion;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.utils.ThreadUtils;
import org.wdt.wdtc.core.utils.URLUtils;


public class DownloadGameWindows {
    private final Launcher launcher;

    public DownloadGameWindows(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setDownGameWin(Stage MainStage) {
        WindwosSizeManger size = new WindwosSizeManger(MainStage);
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
        AnchorPane.setTopAnchor(textField, 350.0);
        AnchorPane.setLeftAnchor(textField, 150.0);
        AnchorPane.setRightAnchor(textField, 150.0);

        Label time = new Label("下载时间不会太长");
        Label status_bar = new Label("下面是状态栏");
        time.setLayoutX(240.0);
        time.setLayoutY(160.0);
        status_bar.setLayoutX(253.0);
        status_bar.setLayoutY(305.0);
        Button bmclHome = new Button("BMCLAPI");
        bmclHome.setOnAction(event -> {
            URLUtils.openSomething("https://bmclapidoc.bangbang93.com/");
        });
        AnchorPane.setRightAnchor(bmclHome, 0.0);
        AnchorPane.setTopAnchor(bmclHome, 0.0);
        Label read_bmcl = new Label("国内快速下载源→");
        AnchorPane.setRightAnchor(read_bmcl, 70.0);
        AnchorPane.setTopAnchor(read_bmcl, 4.0);
        textField.setText(launcher.getVersionNumber() + "开始下载,下载源: " + launcher.getDownloadSourceKind());
        ThreadUtils.startThread(() -> {
            InstallGameVersion installGameVersion = new InstallGameVersion(launcher, true);
            installGameVersion.setSetUIText(textField::setText);
            installGameVersion.InstallGame();
        }).setName("Download Game");
        pane.setBackground(Consoler.getBackground());
        size.ModifyWindwosSize(pane, back, time, status_bar, bmclHome, read_bmcl, textField);
        Scene down_scene = new Scene(pane);
        MainStage.setScene(down_scene);
    }
}
