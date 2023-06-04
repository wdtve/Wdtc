package org.wdt.WdtcUI;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.wdt.Launcher;
import org.wdt.download.forge.ForgeDownloadTask;

import java.util.List;
import java.util.Objects;

public class ModChoose {
    private final List<String> VersionList;
    private final String title;
    private final Stage MainStage;
    private final Launcher launcher;

    public ModChoose(List<String> versionList, String title, Stage mainStage, Launcher launcher) {
        VersionList = versionList;
        this.title = title;
        MainStage = mainStage;
        this.launcher = launcher;
    }

    public void setModChooser() {
        JFXButton back = new JFXButton("返回");
        back.setStyle(Consoler.BlackBorder());
        Pane pane = new Pane();
        VBox ButtonList = new VBox();
        ScrollPane list = new ScrollPane();
        Label tips = new Label("选择一个forge版本:");
        tips.setLayoutX(149.0);
        tips.setLayoutY(67.0);
        list.setLayoutY(134.0);
        list.setPrefHeight(316.0);
        list.setPrefWidth(600.0);
        if (Objects.nonNull(VersionList)) {
            for (String s : VersionList) {
                JFXButton VersionButton = new JFXButton(s);
                VersionButton.setStyle("-fx-border-color: #000000");
                VersionButton.setPrefWidth(600.0);
                VersionButton.setOnAction(event -> {
                    ForgeDownloadTask forgeDownloadTask = new ForgeDownloadTask(launcher, s);
                    launcher.setDownloadTask(forgeDownloadTask);
                    ModChooseWin Choose = new ModChooseWin(launcher, MainStage);
                    Choose.setChooseWin();
                    ButtonList.getChildren().clear();
                });
                ButtonList.getChildren().add(VersionButton);
            }
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
