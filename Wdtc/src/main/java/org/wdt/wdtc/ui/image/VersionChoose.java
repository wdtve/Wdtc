package org.wdt.wdtc.ui.image;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.wdt.wdtc.core.game.DownloadedGameVersion;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.GameDirectoryManger;
import org.wdt.wdtc.core.manger.SettingManger;
import org.wdt.wdtc.core.utils.ModUtils;

import java.util.List;

public class VersionChoose {
    private final GameDirectoryManger path;

    public VersionChoose(GameDirectoryManger path) {
        this.path = path;
    }

    public void setWindow(Stage MainStage) {
        WindwosSizeManger size = new WindwosSizeManger(MainStage);
        AnchorPane ParentPane = new AnchorPane();
        ScrollPane SonScrollPane = new ScrollPane();
        AnchorPane.setLeftAnchor(SonScrollPane, 100.0);
        Consoler.setTopLowerRight(SonScrollPane);
        AnchorPane.setTopAnchor(SonScrollPane, 70.0);
        VBox VersionList = new VBox();
        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> {
            HomeWindow window;
            if (path instanceof Launcher launcher) {
                window = new HomeWindow(launcher);
            } else {
                window = new HomeWindow();
            }
            window.setHome(MainStage);
        });
        if (DownloadedGameVersion.isDownloadedGame(path)) {
            List<Launcher> GameVersionList = DownloadedGameVersion.getGameVersionList(path);
            for (Launcher GameVersion : GameVersionList) {
                Pane pane = new Pane();
                pane.setPrefSize(514, 40);
                RadioButton VersionId = new RadioButton(GameVersion.getVersionNumber());
                if (Launcher.getPreferredLauncher() != null && Launcher.getPreferredLauncher().equals(GameVersion)) {
                    VersionId.setSelected(true);
                }
                VersionId.setLayoutX(14);
                VersionId.setLayoutY(12);
                ModUtils.getModTask(GameVersion);
                Label ModKind = new Label();
                if (GameVersion.getKind() != ModUtils.KindOfMod.Original) {
                    ModKind.setText("Mod : " + GameVersion.getKind().toString());
                } else {
                    ModKind.setText("Mod : 无");
                }
                ModKind.setLayoutX(86);
                ModKind.setLayoutY(13);
                size.ModifyWindwosSize(pane, VersionId, ModKind);
                size.ModifyWindwosSize(VersionList, pane);
                VersionId.setOnAction(event -> {
                    SettingManger.Setting setting = SettingManger.getSetting();
                    setting.setPreferredVersion(VersionId.getText());
                    SettingManger.putSettingToFile(setting);
                    HomeWindow win = new HomeWindow(GameVersion);
                    win.setHome(MainStage);
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
        NewGame.setOnAction(event -> NewDownloadWindow.setWindowScene(MainStage));
    }

}
