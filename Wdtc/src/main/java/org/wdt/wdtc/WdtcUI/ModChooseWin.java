package org.wdt.wdtc.WdtcUI;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModList;

import java.io.IOException;

public class ModChooseWin {
    private static final Logger logmaker = Logger.getLogger(ModChooseWin.class);
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
        Label Forge = new Label(Tips.ForgeNo);
        Label Fabric = new Label(Tips.FabricNo);
        Label FabricAPI = new Label(Tips.FabricAPINo);
        Label Quilt = new Label(Tips.QuiltNo);

        Pane ForgePane = new Pane();
        ForgePane.setLayoutX(60.0);
        ForgePane.setLayoutY(96.0);
        ForgePane.setPrefSize(160, 87);
        Forge.setLayoutX(36.0);
        Forge.setLayoutY(27.0);
        JFXButton DownloadForge = new JFXButton("->");
        DownloadForge.setLayoutX(67.0);
        DownloadForge.setLayoutY(53.0);
        DownloadForge.setPrefSize(47, 23);
        DownloadForge.setStyle(Consoler.BlackBorder());
        JFXButton CancelForge = new JFXButton("X");
        CancelForge.setLayoutX(36.0);
        CancelForge.setLayoutY(53.0);
        CancelForge.setStyle(Consoler.BlackBorder());
        ForgePane.getChildren().addAll(Forge, DownloadForge, CancelForge);
        ForgePane.setStyle("-fx-border-color: #000000; -fx-background-color: #FFFFFF");

        Pane FabricPane = new Pane();
        FabricPane.setLayoutX(220.0);
        FabricPane.setLayoutY(96.0);
        FabricPane.setPrefSize(160, 87);
        Fabric.setLayoutX(43.0);
        Fabric.setLayoutY(27.0);
        JFXButton DownloadFabric = new JFXButton("->");
        DownloadFabric.setLayoutX(73.0);
        DownloadFabric.setLayoutY(53.0);
        DownloadFabric.setPrefSize(47, 23);
        DownloadFabric.setStyle(Consoler.BlackBorder());
        JFXButton CancelFabric = new JFXButton("X");
        CancelFabric.setLayoutX(43.0);
        CancelFabric.setLayoutY(53.0);
        CancelFabric.setStyle(Consoler.BlackBorder());
        FabricPane.getChildren().addAll(Fabric, DownloadFabric, CancelFabric);
        FabricPane.setStyle("-fx-border-color: #000000; -fx-background-color: #FFFFFF");

        Pane FabricAPIPane = new Pane();
        FabricAPIPane.setLayoutX(380.0);
        FabricAPIPane.setLayoutY(96.0);
        FabricAPIPane.setPrefSize(160, 87);
        FabricAPI.setLayoutX(43.0);
        FabricAPI.setLayoutY(27.0);
        JFXButton DownloadFabricAPI = new JFXButton("->");
        DownloadFabricAPI.setLayoutX(73.0);
        DownloadFabricAPI.setLayoutY(53.0);
        DownloadFabricAPI.setPrefSize(47, 23);
        DownloadFabricAPI.setStyle(Consoler.BlackBorder());
        JFXButton CancelFabricAPI = new JFXButton("X");
        CancelFabricAPI.setLayoutX(43.0);
        CancelFabricAPI.setLayoutY(53.0);
        CancelFabricAPI.setStyle(Consoler.BlackBorder());
        FabricAPIPane.getChildren().addAll(FabricAPI, DownloadFabricAPI, CancelFabricAPI);
        FabricAPIPane.setStyle("-fx-border-color: #000000; -fx-background-color: #FFFFFF");

        Pane QuiltPane = new Pane();
        QuiltPane.setLayoutX(60.0);
        QuiltPane.setLayoutY(183.0);
        QuiltPane.setPrefSize(160, 87);
        Quilt.setLayoutX(43.0);
        Quilt.setLayoutY(27.0);
        JFXButton DownloadQuilt = new JFXButton("->");
        DownloadQuilt.setLayoutX(73.0);
        DownloadQuilt.setLayoutY(53.0);
        DownloadQuilt.setPrefSize(47, 23);
        DownloadQuilt.setStyle(Consoler.BlackBorder());
        JFXButton CancelQuilt = new JFXButton("X");
        CancelQuilt.setLayoutX(43.0);
        CancelQuilt.setLayoutY(53.0);
        CancelQuilt.setStyle(Consoler.BlackBorder());
        QuiltPane.getChildren().addAll(Quilt, DownloadQuilt, CancelQuilt);
        QuiltPane.setStyle("-fx-border-color: #000000; -fx-background-color: #FFFFFF");

        JFXButton confirm = new JFXButton("安装游戏");
        confirm.setLayoutX(435.0);
        confirm.setLayoutY(337.0);
        confirm.setPrefSize(107, 57);
        confirm.setStyle(Consoler.BlackBorder());

        pane.setBackground(Consoler.getBackground());
        pane.getChildren().addAll(back, title, confirm);
        pane.getChildren().addAll(ForgePane, FabricPane, FabricAPIPane, QuiltPane);
        MainStage.setScene(new Scene(pane));

        DownloadForge.setOnAction(event -> {
            try {
                ModChoose Choose = new ModChoose(ModList.KindOfMod.FORGE, MainStage, launcher);
                Choose.setModChooser();
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        CancelForge.setOnAction(event -> {
            launcher.setKind(ModList.KindOfMod.Original);
            DownloadFabricAPI.setDisable(false);
            DownloadFabric.setDisable(false);
            Forge.setText(Tips.ForgeNo);
            Fabric.setText(Tips.FabricNo);
        });

        DownloadFabric.setOnAction(event -> {
            try {
                ModChoose Choose = new ModChoose(ModList.KindOfMod.FABRIC, MainStage, launcher);
                Choose.setModChooser();
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        CancelFabric.setOnAction(event -> {
            try {
                launcher.setKind(ModList.KindOfMod.Original);
                launcher.getFabricModDownloadTask().setAPIDownloadTask(null);
                DownloadForge.setDisable(false);
                DownloadFabricAPI.setDisable(true);
                Forge.setText(Tips.ForgeNo);
                Fabric.setText(Tips.FabricNo);
                FabricAPI.setText(Tips.FabricAPINo);
            } catch (NullPointerException e) {
                logmaker.warn("warn:", e);
            }
        });

        DownloadFabricAPI.setOnAction(event -> {
            try {
                ModChoose Choose = new ModChoose(ModList.KindOfMod.FABRICAPI, MainStage, launcher);
                Choose.setModChooser();
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        CancelFabricAPI.setOnAction(event -> {
            try {
                launcher.getFabricModDownloadTask().setAPIDownloadTask(null);
                FabricAPI.setText(Tips.FabricAPINo);
            } catch (NullPointerException e) {
                logmaker.warn("warn:", e);
            }
        });

        DownloadQuilt.setOnAction(event -> {
            try {
                ModChoose Choose = new ModChoose(ModList.KindOfMod.QUILT, MainStage, launcher);
                Choose.setModChooser();
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        CancelQuilt.setOnAction(event -> {
            Quilt.setText(Tips.QuiltNo);
            Forge.setText(Tips.ForgeNo);
            Fabric.setText(Tips.FabricNo);
            DownloadForge.setDisable(false);
            DownloadFabric.setDisable(false);
        });

        if (ModList.GameModIsForge(launcher)) {
            Forge.setText("Froge : " + launcher.getForgeDownloadTask().getForgeVersion());
            Fabric.setText("Fabric : 与Forge不兼容");
            DownloadFabricAPI.setDisable(true);
            DownloadFabric.setDisable(true);
        } else {
            Forge.setText(Tips.ForgeNo);
        }

        if (ModList.GameModIsFabric(launcher)) {
            Fabric.setText("Fabric : " + launcher.getFabricModDownloadTask().getFabricVersionNumber());
            Forge.setText("Forge : 与Fabric不兼容");
            DownloadFabricAPI.setDisable(false);
            DownloadForge.setDisable(true);
            if (launcher.getFabricModDownloadTask().getAPIDownloadTaskNoNull()) {
                FabricAPI.setText(launcher.getFabricModDownloadTask().getAPIDownloadTask().getFabricAPIVersionNumber());
            }
        } else {
            Fabric.setText(Tips.FabricNo);
            DownloadFabricAPI.setDisable(true);
        }

        if (ModList.GameModIsQuilt(launcher)) {
            Quilt.setText("Quilt : " + launcher.getQuiltModDownloadTask().getQuiltVersionNumber());
            Forge.setText("Forge : 与Quilt不兼容");
            Fabric.setText("Fabric :  与Quilt不兼容");
            DownloadFabric.setDisable(true);
            DownloadForge.setDisable(true);
        }

        back.setOnAction(event -> NewDownloadWin.SetWin(MainStage));
        confirm.setOnAction(event -> {
            DownloadGameWin downloadGameWin = new DownloadGameWin(launcher);
            downloadGameWin.setDownGameWin(MainStage);
        });
    }

    private static class Tips {
        public static String ForgeNo = "Forge : 不安装";
        public static String FabricNo = "Fabric : 不安装";
        public static String FabricAPINo = "FabricAPI : 不安装";
        public static String QuiltNo = "Quilt : 不安装";
    }
}