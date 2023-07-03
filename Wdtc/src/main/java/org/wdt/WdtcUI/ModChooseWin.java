package org.wdt.WdtcUI;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.wdt.game.Launcher;
import org.wdt.game.ModList;

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
        Label Fabric = new Label();
        Pane ForgePane = new Pane();
        ForgePane.setLayoutX(62.0);
        ForgePane.setLayoutY(96.0);
        ForgePane.setPrefHeight(87.0);
        ForgePane.setPrefWidth(160.0);
        forge.setLayoutX(36.0);
        forge.setLayoutY(27.0);
        JFXButton install = new JFXButton("->");
        install.setLayoutX(67.0);
        install.setLayoutY(53.0);
        install.setPrefHeight(23.0);
        install.setPrefWidth(47.0);
        install.setStyle(Consoler.BlackBorder());
        JFXButton cancel = new JFXButton("X");
        cancel.setLayoutX(36.0);
        cancel.setLayoutY(53.0);
        cancel.setStyle(Consoler.BlackBorder());
        ForgePane.getChildren().addAll(forge, install, cancel);
        ForgePane.setStyle("-fx-border-color: #000000; -fx-background-color: #FFFFFF");
        Pane FabricPane = new Pane();
        FabricPane.setLayoutX(220.0);
        FabricPane.setLayoutY(96.0);
        FabricPane.setPrefHeight(87.0);
        FabricPane.setPrefWidth(160.0);
        Fabric.setLayoutX(43.0);
        Fabric.setLayoutY(27.0);
        JFXButton DownloadFabric = new JFXButton("->");
        DownloadFabric.setLayoutX(73.0);
        DownloadFabric.setLayoutY(53.0);
        DownloadFabric.setPrefHeight(23.0);
        DownloadFabric.setPrefWidth(47.0);
        DownloadFabric.setStyle(Consoler.BlackBorder());
        JFXButton CancelFabric = new JFXButton("X");
        CancelFabric.setLayoutX(43.0);
        CancelFabric.setLayoutY(53.0);
        CancelFabric.setStyle(Consoler.BlackBorder());
        FabricPane.getChildren().addAll(Fabric, DownloadFabric, CancelFabric);
        FabricPane.setStyle("-fx-border-color: #000000; -fx-background-color: #FFFFFF");
        JFXButton confirm = new JFXButton("安装游戏");
        confirm.setLayoutX(435.0);
        confirm.setLayoutY(337.0);
        confirm.setPrefHeight(57.0);
        confirm.setPrefWidth(107.0);
        confirm.setStyle(Consoler.BlackBorder());
        pane.setBackground(Consoler.getBackground());
        pane.getChildren().addAll(back, title, ForgePane, FabricPane, confirm);
        MainStage.setScene(new Scene(pane));
        install.setOnAction(event -> {
            try {
                ModChoose Choose = new ModChoose(MainStage, launcher, ModList.KindOfMod.FORGE);
                Choose.setModChooser();
                DownloadFabric.setDisable(true);

            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        cancel.setOnAction(event -> {
            launcher.setKind(ModList.KindOfMod.Original);
            DownloadFabric.setDisable(false);
            forge.setText("Forge : 不安装");
            Fabric.setText("Fabric : 不安装");
        });
        confirm.setOnAction(event -> {
            DownloadGameWin downloadGameWin = new DownloadGameWin(launcher);
            downloadGameWin.setDownGameWin(MainStage);
        });
        DownloadFabric.setOnAction(event -> {
            try {
                ModChoose Choose = new ModChoose(MainStage, launcher, ModList.KindOfMod.FABRIC);
                Choose.setModChooser();
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        CancelFabric.setOnAction(event -> {
            launcher.setKind(ModList.KindOfMod.Original);
            install.setDisable(false);
            forge.setText("Forge : 不安装");
            Fabric.setText("Fabric : 不安装");

        });
        if (ModList.GameModIsForge(launcher)) {
            forge.setText("Froge : " + launcher.getForgeDownloadTask().getForgeVersion());
            Fabric.setText("Fabric : 与Forge不兼容");
            DownloadFabric.setDisable(true);
        } else {
            forge.setText("Forge : 不安装");
        }
        if (ModList.GameModIsFabric(launcher)) {
            Fabric.setText("Fabric : " + launcher.getFabricModDownloadTask().getFabricVersionNumber());
            forge.setText("Forge : 与Fabric不兼容");
            install.setDisable(true);
        } else {
            Fabric.setText("Fabric : 不安装");
        }
        back.setOnAction(event -> NewDownloadWin.SetWin(MainStage));
    }
}
