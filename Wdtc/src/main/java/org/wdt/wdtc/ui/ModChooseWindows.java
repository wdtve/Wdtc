package org.wdt.wdtc.ui;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModUtils;
import org.wdt.wdtc.platform.Starter;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;

public class ModChooseWindows {
    private static final Logger logmaker = WdtcLogger.getLogger(ModChooseWindows.class);
    private final Launcher launcher;
    private final Stage MainStage;
    private final WindwosSize size;

    public ModChooseWindows(Launcher launcher, Stage MainStage) {
        this.launcher = launcher;
        this.MainStage = MainStage;
        this.size = new WindwosSize(MainStage);
    }

    public void setChooseWin() {
        Pane pane = new Pane();
        JFXButton back = new JFXButton("返回");
        Label title = new Label(launcher.getVersionNumber());
        title.setLayoutX(283.0);
        title.setLayoutY(69.0);
        Label Forge = new Label(Tips.ForgeNo);
        Label Fabric = new Label(Tips.FabricNo);
        Label FabricAPI = new Label(Tips.FabricAPINo);
        Label Quilt = new Label(Tips.QuiltNo);

        Pane ForgePane = new Pane();
        ForgePane.setLayoutX(60.0);
        ForgePane.setLayoutY(96.0);
        ForgePane.setPrefSize(160, 90);
        Forge.setLayoutX(36.0);
        Forge.setLayoutY(27.0);
        JFXButton DownloadForge = new JFXButton("->");
        DownloadForge.setLayoutX(67.0);
        DownloadForge.setLayoutY(53.0);
        DownloadForge.setPrefSize(47, 23);
        DownloadForge.setDisable(!Starter.getForgeSwitch());
        JFXButton CancelForge = new JFXButton("X");
        CancelForge.setLayoutX(36.0);
        CancelForge.setLayoutY(53.0);
        size.ModifyWindwosSize(ForgePane, Forge, DownloadForge, CancelForge);

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
        JFXButton CancelFabric = new JFXButton("X");
        CancelFabric.setLayoutX(43.0);
        CancelFabric.setLayoutY(53.0);
        size.ModifyWindwosSize(FabricPane, Fabric, DownloadFabric, CancelFabric);

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
        JFXButton CancelFabricAPI = new JFXButton("X");
        CancelFabricAPI.setLayoutX(43.0);
        CancelFabricAPI.setLayoutY(53.0);
        size.ModifyWindwosSize(FabricAPIPane, FabricAPI, DownloadFabricAPI, CancelFabricAPI);

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
        JFXButton CancelQuilt = new JFXButton("X");
        CancelQuilt.setLayoutX(43.0);
        CancelQuilt.setLayoutY(53.0);
        size.ModifyWindwosSize(QuiltPane, Quilt, DownloadQuilt, CancelQuilt);

        JFXButton confirm = new JFXButton("安装游戏");
        confirm.setLayoutX(435.0);
        confirm.setLayoutY(337.0);
        AnchorPane.setRightAnchor(confirm, 30.0);
        AnchorPane.setBottomAnchor(confirm, 30.0);
        confirm.getStyleClass().add("BackGroundWriteButton");
        confirm.setPrefSize(107, 57);

        pane.setBackground(Consoler.getBackground());
        size.ModifyWindwosSize(pane, back, title, confirm, ForgePane, FabricPane, FabricAPIPane, QuiltPane);
        Consoler.setCss("ModChoosePane", FabricPane, ForgePane, FabricAPIPane, QuiltPane);
        Consoler.setCss("BlackBorder", back, DownloadForge, DownloadFabric, DownloadFabricAPI, DownloadQuilt);
        Consoler.setCss("BlackBorder", CancelForge, CancelFabric, CancelFabricAPI, CancelQuilt);
        Consoler.setStylesheets(pane);
        MainStage.setScene(new Scene(pane));

        DownloadForge.setOnAction(event -> {
            try {
                ModChoose Choose = new ModChoose(ModUtils.KindOfMod.FORGE, MainStage, launcher);
                Choose.setModChooser();
            } catch (IOException e) {
                ErrorWindow.setErrorWin(e);
            }
        });
        CancelForge.setOnAction(event -> {
            launcher.setKind(ModUtils.KindOfMod.Original);
            DownloadFabricAPI.setDisable(false);
            DownloadFabric.setDisable(false);
            Forge.setText(Tips.ForgeNo);
            Fabric.setText(Tips.FabricNo);
        });

        DownloadFabric.setOnAction(event -> {
            try {
                ModChoose Choose = new ModChoose(ModUtils.KindOfMod.FABRIC, MainStage, launcher);
                Choose.setModChooser();
            } catch (IOException e) {
                ErrorWindow.setErrorWin(e);
            }
        });
        CancelFabric.setOnAction(event -> {
            try {
                launcher.setKind(ModUtils.KindOfMod.Original);
                launcher.getFabricModInstallInfo().setAPIDownloadTask(null);
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
                ModChoose Choose = new ModChoose(ModUtils.KindOfMod.FABRICAPI, MainStage, launcher);
                Choose.setModChooser();
            } catch (IOException e) {
                ErrorWindow.setErrorWin(e);
            }
        });
        CancelFabricAPI.setOnAction(event -> {
            try {
                launcher.getFabricModInstallInfo().setAPIDownloadTask(null);
                FabricAPI.setText(Tips.FabricAPINo);
            } catch (NullPointerException e) {
                logmaker.warn("warn:", e);
            }
        });

        DownloadQuilt.setOnAction(event -> {
            try {
                ModChoose Choose = new ModChoose(ModUtils.KindOfMod.QUILT, MainStage, launcher);
                Choose.setModChooser();
            } catch (IOException e) {
                ErrorWindow.setErrorWin(e);
            }
        });
        CancelQuilt.setOnAction(event -> {
            Quilt.setText(Tips.QuiltNo);
            Forge.setText(Tips.ForgeNo);
            Fabric.setText(Tips.FabricNo);
            DownloadForge.setDisable(false);
            DownloadFabric.setDisable(false);
        });

        if (ModUtils.GameModIsForge(launcher)) {
            Forge.setText("Froge : " + launcher.getForgeDownloadInfo().getForgeVersionNumber());
            Fabric.setText("Fabric : 与Forge不兼容");
            DownloadFabricAPI.setDisable(true);
            DownloadFabric.setDisable(true);
        } else {
            Forge.setText(Tips.ForgeNo);
        }

        if (ModUtils.GameModIsFabric(launcher)) {
            Fabric.setText("Fabric : " + launcher.getFabricModInstallInfo().getFabricVersionNumber());
            Forge.setText("Forge : 与Fabric不兼容");
            DownloadFabricAPI.setDisable(false);
            DownloadForge.setDisable(true);
            if (launcher.getFabricModInstallInfo().getAPIDownloadTaskNoNull()) {
                FabricAPI.setText(launcher.getFabricModInstallInfo().getAPIDownloadTask().getFabricAPIVersionNumber());
            }
        } else {
            Fabric.setText(Tips.FabricNo);
            DownloadFabricAPI.setDisable(true);
        }

        if (ModUtils.GameModIsQuilt(launcher)) {
            Quilt.setText("Quilt : " + launcher.getQuiltModDownloadInfo().getQuiltVersionNumber());
            Forge.setText("Forge : 与Quilt不兼容");
            Fabric.setText("Fabric :  与Quilt不兼容");
            DownloadFabric.setDisable(true);
            DownloadForge.setDisable(true);
        }

        back.setOnAction(event -> NewDownloadWindow.SetWin(MainStage));
        confirm.setOnAction(event -> {
            DownloadGameWindows downloadGameWindows = new DownloadGameWindows(launcher);
            downloadGameWindows.setDownGameWin(MainStage);
        });
    }

    private static class Tips {
        public static String ForgeNo = "Forge : 不安装";
        public static String FabricNo = "Fabric : 不安装";
        public static String FabricAPINo = "FabricAPI : 不安装";
        public static String QuiltNo = "Quilt : 不安装";
    }
}
