package org.wdt.WdtcUI;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.wdt.download.fabric.FabricDownloadTask;
import org.wdt.download.fabric.FabricVersionList;
import org.wdt.download.forge.ForgeDownloadTask;
import org.wdt.download.forge.ForgeVersionList;
import org.wdt.game.Launcher;
import org.wdt.game.ModList;

import java.io.IOException;
import java.util.List;

public class ModChoose {
    private final ModList.KindOfMod kind;

    private final Stage MainStage;
    private final Launcher launcher;

    public ModChoose(Stage mainStage, Launcher launcher, ModList.KindOfMod kind) {
        this.kind = kind;
        MainStage = mainStage;
        this.launcher = launcher;
    }

    public List<String> ModVersionList() throws IOException {
        if (kind == ModList.KindOfMod.FORGE) {
            return new ForgeVersionList(launcher).getForgeVersion();
        } else {
            return FabricVersionList.getList();
        }
    }

    public void setModChooser() throws IOException {
        JFXButton back = new JFXButton("返回");
        back.setStyle(Consoler.BlackBorder());
        Pane pane = new Pane();
        VBox ButtonList = new VBox();
        ScrollPane list = new ScrollPane();
        Label tips = new Label("选择一个Mod版本:");
        tips.setLayoutX(149.0);
        tips.setLayoutY(67.0);
        list.setLayoutY(134.0);
        list.setPrefHeight(316.0);
        list.setPrefWidth(600.0);
        for (String s : ModVersionList()) {
            JFXButton VersionButton = new JFXButton(s);
            VersionButton.setStyle("-fx-border-color: #000000");
            VersionButton.setPrefWidth(600.0);
            VersionButton.setOnAction(event -> {
                if (kind == ModList.KindOfMod.FORGE) {
                    launcher.setForgeModDownloadTask(new ForgeDownloadTask(launcher, s));
                } else if (kind == ModList.KindOfMod.FABRIC) {
                    launcher.setFabricModDownloadTask(new FabricDownloadTask(s, launcher));
                }
                ModChooseWin Choose = new ModChooseWin(launcher, MainStage);
                Choose.setChooseWin();
                ButtonList.getChildren().clear();
            });
            ButtonList.getChildren().add(VersionButton);
        }

        list.setContent(ButtonList);
        pane.getChildren().addAll(list, tips, back);
        pane.setBackground(Consoler.getBackground());
        MainStage.setScene(new Scene(pane));
        back.setOnAction(event -> {
            ModChooseWin Choose = new ModChooseWin(launcher, MainStage);
            Choose.setChooseWin();
        });
    }
}
