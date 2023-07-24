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
import org.wdt.wdtc.download.fabric.FabricDownloadTask;
import org.wdt.wdtc.download.fabric.FabricVersionList;
import org.wdt.wdtc.download.forge.ForgeDownloadTask;
import org.wdt.wdtc.download.forge.ForgeVersionList;
import org.wdt.wdtc.download.quilt.QuiltDownloadTask;
import org.wdt.wdtc.download.quilt.QuiltVersionList;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModList;

import java.io.IOException;
import java.util.List;

public class ModChoose {
    private final ModList.KindOfMod kind;

    private final Stage MainStage;
    private final WindwosSize size;
    private final Launcher launcher;

    public ModChoose(ModList.KindOfMod kind, Stage MainStage, Launcher launcher) {
        this.kind = kind;
        this.MainStage = MainStage;
        this.launcher = launcher;
        this.size = new WindwosSize(MainStage);
    }

    public List<String> ModVersionList() throws IOException {
        if (kind == ModList.KindOfMod.FORGE) {
            return new ForgeVersionList(launcher).getForgeVersion();
        } else if (kind == ModList.KindOfMod.FABRICAPI) {
            return new FabricAPIVersionList(launcher).getFabricAPIVersionList();
        } else if (kind == ModList.KindOfMod.QUILT) {
            return new QuiltVersionList(launcher).VersionList();
        } else {
            return FabricVersionList.getList();
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
                for (String s : ModVersionList()) {
                    JFXButton VersionButton = getVersionButton(s, ButtonList);
                    size.ModifyWindwosSize(ButtonList, VersionButton);
                }
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        list.setContent(ButtonList);
        size.ModifyWindwosSize(pane, list, back, tips);
        pane.setBackground(Consoler.getBackground());
        Consoler.setStylesheets(pane);
        MainStage.setScene(new Scene(pane));
        back.setOnAction(event -> {
            ModChooseWin Choose = new ModChooseWin(launcher, MainStage);
            Choose.setChooseWin();
        });

    }

    private JFXButton getVersionButton(String s, VBox ButtonList) {
        JFXButton VersionButton = new JFXButton(s);
        VersionButton.setStyle("-fx-border-color: #000000");
        VersionButton.setPrefWidth(600.0);
        VersionButton.setOnAction(event -> {
            if (kind == ModList.KindOfMod.FORGE) {
                launcher.setForgeModDownloadTask(new ForgeDownloadTask(launcher, s));
            } else if (kind == ModList.KindOfMod.FABRIC) {
                launcher.setFabricModDownloadTask(new FabricDownloadTask(s, launcher));
            } else if (kind == ModList.KindOfMod.FABRICAPI) {
                launcher.getFabricModDownloadTask().setAPIDownloadTask(new FabricAPIDownloadTask(launcher, s));
            } else if (kind == ModList.KindOfMod.QUILT) {
                launcher.setQuiltModDownloadTask(new QuiltDownloadTask(launcher, s));
            }
            ModChooseWin Choose = new ModChooseWin(launcher, MainStage);
            Choose.setChooseWin();
            ButtonList.getChildren().clear();
        });
        return VersionButton;
    }
}
