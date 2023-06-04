package org.wdt.WdtcUI;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.wdt.Launcher;
import org.wdt.download.forge.ForgeVersionList;

import java.io.IOException;

public class ModChooseWin {
    private final Launcher launcher;
    private final Stage MainStage;

    public ModChooseWin(Launcher launcher, Stage MainStage) {
        this.launcher = launcher;
        this.MainStage = MainStage;
    }

    public void setChooseWin() {
        Pane pane = new Pane();
        JFXButton back = new JFXButton("返回");
        back.setStyle(Consoler.BlackBorder());
        Label title = new Label(launcher.getVersion());
        title.setLayoutX(283.0);
        title.setLayoutY(69.0);
        Label forge = new Label();
        if (launcher.getForgeDownloadTaskIsNull()) {
            forge.setText("Froge : " + launcher.getForgeDownloadTask().getForgeVersion());
        } else {
            forge.setText("Forge : 不安装");
        }
        forge.setLayoutX(104.0);
        forge.setLayoutY(124.0);
        JFXButton install = new JFXButton("->");
        install.setLayoutX(136.0);
        install.setLayoutY(146.0);
        install.setPrefHeight(23.0);
        install.setPrefWidth(47.0);
        install.setStyle(Consoler.BlackBorder());
        JFXButton cancel = new JFXButton("X");
        cancel.setLayoutX(104.0);
        cancel.setLayoutY(146.0);
        cancel.setStyle(Consoler.BlackBorder());
        JFXButton confirm = new JFXButton("安装游戏");
        confirm.setLayoutX(435.0);
        confirm.setLayoutY(337.0);
        confirm.setPrefHeight(57.0);
        confirm.setPrefWidth(107.0);
        confirm.setStyle(Consoler.BlackBorder());
        pane.setBackground(Consoler.getBackground());
        pane.getChildren().addAll(back, title, forge, install, cancel, confirm);
        MainStage.setScene(new Scene(pane));
        install.setOnAction(event -> {
            try {
                ForgeVersionList versionList = new ForgeVersionList(launcher);
                ModChoose Choose = new ModChoose(versionList.getForgeVersion(), "forge", MainStage, launcher);
                Choose.setModChooser();
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        cancel.setOnAction(event -> {
            launcher.setDownloadTask(null);
            forge.setText("Forge : 不安装");
        });
        confirm.setOnAction(event -> {
            DownloadGameWin downloadGameWin = new DownloadGameWin(launcher);
            downloadGameWin.setDownGameWin(MainStage);
        });
        back.setOnAction(event -> NewDownloadWin.SetWin(MainStage));
    }
}
