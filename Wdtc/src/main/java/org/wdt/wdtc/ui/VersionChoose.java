package org.wdt.wdtc.ui;

import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.game.DownloadedGameVersion;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModList;
import org.wdt.wdtc.launch.GetGamePath;
import org.wdt.wdtc.platform.AboutSetting;

import java.io.IOException;
import java.util.List;

public class VersionChoose {
    private final GetGamePath path;

    public VersionChoose(GetGamePath path) {
        this.path = path;
    }

    public void setWindow(Stage MainStage) {
        WindwosSize size = new WindwosSize(MainStage);
        AnchorPane ParentPane = new AnchorPane();
        ScrollPane SonScrollPane = new ScrollPane();
        AnchorPane.setLeftAnchor(SonScrollPane, 100.0);
        Consoler.setTopLowerRight(SonScrollPane);
        AnchorPane.setTopAnchor(SonScrollPane, 70.0);
        VBox VersionList = new VBox();
        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> {
            HomeWindow window = new HomeWindow(null);
            window.setHome(MainStage);
        });
        List<Launcher> GameVersionList = DownloadedGameVersion.getGameVersionList(path);
        if (GameVersionList != null) {
            for (Launcher GameVersion : GameVersionList) {
                Pane pane = new Pane();
                pane.setPrefSize(514, 40);
                RadioButton VersionId = new RadioButton(GameVersion.getVersion());
                VersionId.setLayoutX(14);
                VersionId.setLayoutY(12);
                ModList.getModTask(GameVersion);
                Label ModKind = new Label();
                if (GameVersion.getKind() != ModList.KindOfMod.Original) {
                    ModKind.setText("Mod : " + GameVersion.getKind().toString());
                } else {
                    ModKind.setText("Mod : 无");
                }
                ModKind.setLayoutX(86);
                ModKind.setLayoutY(13);
                size.ModifyWindwosSize(pane, VersionId, ModKind);
                size.ModifyWindwosSize(VersionList, pane);
                VersionId.setOnAction(event -> {
                    try {
                        JsonObject SettingObject = AboutSetting.SettingObject().getJsonObjects();
                        SettingObject.addProperty("PreferredVersion", VersionId.getText());
                        FileUtils.writeStringToFile(AboutSetting.GetSettingFile(), JSONObject.toJSONString(SettingObject), "UTF-8");
                        HomeWindow win = new HomeWindow(GameVersion);
                        win.setHome(MainStage);
                    } catch (IOException e) {
                        ErrorWin.setErrorWin(e);
                    }
                });
            }
        }
        VersionList.setPrefSize(517, 416);
        SonScrollPane.setContent(VersionList);
        JFXButton NewGame = new JFXButton("安装新游戏");
        NewGame.setPrefSize(100, 30);
        NewGame.getStyleClass().add("BackGroundWriteButton");
        Consoler.setTopLowerLeft(NewGame);
        Label tips = new Label("\t当前目录");
        tips.setPrefWidth(96);
        tips.setLayoutY(70);
        ParentPane.getChildren().addAll(NewGame, back, SonScrollPane, tips);
        ParentPane.setBackground(Consoler.getBackground());
        Consoler.setStylesheets(ParentPane);
        Consoler.setCss("BlackBorder", back);
        MainStage.setScene(new Scene(ParentPane));
        NewGame.setOnAction(event -> NewDownloadWin.SetWin(MainStage));
    }

}
