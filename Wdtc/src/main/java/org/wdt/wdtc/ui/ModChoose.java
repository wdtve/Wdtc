package org.wdt.wdtc.ui;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.wdt.wdtc.download.fabric.FabricAPIDownloadTask;
import org.wdt.wdtc.download.fabric.FabricAPIVersionList;
import org.wdt.wdtc.download.fabric.FabricDonwloadInfo;
import org.wdt.wdtc.download.fabric.FabricVersionList;
import org.wdt.wdtc.download.forge.ForgeDownloadInfo;
import org.wdt.wdtc.download.forge.ForgeVersionList;
import org.wdt.wdtc.download.infterface.VersionList;
import org.wdt.wdtc.download.quilt.QuiltInstallTask;
import org.wdt.wdtc.download.quilt.QuiltVersionList;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModUtils;

import java.io.IOException;

public class ModChoose {
    private final ModUtils.KindOfMod kind;

    private final Stage MainStage;
    private final WindwosSize size;
    private final Launcher launcher;

    public ModChoose(ModUtils.KindOfMod kind, Stage MainStage, Launcher launcher) {
        this.kind = kind;
        this.MainStage = MainStage;
        this.launcher = launcher;
        this.size = new WindwosSize(MainStage);
    }

    public VersionList ModVersionList() throws IOException {
        if (kind == ModUtils.KindOfMod.FORGE) {
            return new ForgeVersionList(launcher);
        } else if (kind == ModUtils.KindOfMod.FABRICAPI) {
            return new FabricAPIVersionList(launcher);
        } else if (kind == ModUtils.KindOfMod.QUILT) {
            return new QuiltVersionList(launcher);
        } else {
            return new FabricVersionList();
        }
    }

    public void setModChooser() throws IOException {
        JFXButton back = new JFXButton("返回");
        back.getStyleClass().add("BlackBorder");
        Pane pane = new Pane();
        VBox ButtonList = new VBox();
        ScrollPane list = new ScrollPane();
        Label tips = new Label("选择一个Mod版本:");
        tips.setLayoutX(149.0);
        tips.setLayoutY(67.0);
        list.setLayoutY(134.0);
        list.setPrefSize(600, 316);
        Platform.runLater(() -> {
            try {
                for (String s : ModVersionList().getVersionList()) {
                    JFXButton VersionButton = getVersionButton(s, ButtonList);
                    size.ModifyWindwosSize(ButtonList, VersionButton);
                }
            } catch (IOException e) {
                ErrorWindow.setErrorWin(e);
            }
        });
        list.setContent(ButtonList);
        size.ModifyWindwosSize(pane, list, back, tips);
        pane.setBackground(Consoler.getBackground());
        Consoler.setStylesheets(pane);
        MainStage.setScene(new Scene(pane));
        back.setOnAction(event -> {
            ModChooseWindows Choose = new ModChooseWindows(launcher, MainStage);
            Choose.setChooseWin();
        });

    }

    private JFXButton getVersionButton(String ModVersion, VBox ButtonList) {
        JFXButton VersionButton = new JFXButton(ModVersion);
        VersionButton.setStyle("-fx-border-color: #000000");
        VersionButton.setPrefWidth(600.0);
        VersionButton.setOnAction(event -> {
            switch (kind) {
                case FORGE -> launcher.setForgeModDownloadInfo(new ForgeDownloadInfo(launcher, ModVersion));
                case FABRIC -> launcher.setFabricModInstallInfo(new FabricDonwloadInfo(launcher, ModVersion));
                case QUILT -> launcher.setQuiltModDownloadInfo(new QuiltInstallTask(launcher, ModVersion));
                case FABRICAPI ->
                        launcher.getFabricModInstallInfo().setAPIDownloadTask(new FabricAPIDownloadTask(launcher, ModVersion));
            }
            ModChooseWindows Choose = new ModChooseWindows(launcher, MainStage);
            Choose.setChooseWin();
            ButtonList.getChildren().clear();
        });
        return VersionButton;
    }
}
