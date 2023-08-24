package org.wdt.wdtc.ui;

import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.utils.FileUtils;
import org.wdt.wdtc.download.InstallGameVersion;
import org.wdt.wdtc.download.infterface.DownloadInfo;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModUtils;
import org.wdt.wdtc.game.config.DefaultGameConfig;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.utils.JavaHomePath;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.ThreadUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;

import static java.util.Objects.requireNonNull;


public class VersionSettingWindows extends AboutSetting {
    private static final double layoutX = 10;
    private static final Logger logmaker = WdtcLogger.getLogger(VersionSettingWindows.class);
    private final Launcher launcher;
    private final DefaultGameConfig.Config config;
    private final WindwosSize size;
    private final Stage MainStage;

    public VersionSettingWindows(Launcher launcher, Stage MainStage) {
        this.launcher = launcher;
        this.config = launcher.getGameConfig().getConfig();
        this.size = new WindwosSize(MainStage);
        this.MainStage = MainStage;
    }

    public void setWindow() {
        HomeWindow window = new HomeWindow(launcher);

        AnchorPane ParentPane = new AnchorPane();
        ScrollPane SonScrollPane = new ScrollPane();
        SonScrollPane.setLayoutX(105);
        SonScrollPane.setLayoutY(52);
        AnchorPane.setTopAnchor(SonScrollPane, 50.0);
        AnchorPane.setLeftAnchor(SonScrollPane, 105.0);
        AnchorPane.setBottomAnchor(SonScrollPane, 0.0);
        AnchorPane.setRightAnchor(SonScrollPane, 0.0);

        AnchorPane pane = new AnchorPane();
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);

        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> window.setHome(MainStage));

        JFXButton GameSetting = new JFXButton("游戏设置");
        GameSetting.setPrefSize(105, 30);
        AnchorPane.setTopAnchor(GameSetting, 50.0);
        AnchorPane.setLeftAnchor(GameSetting, 0.0);
        JFXButton AutoDownload = new JFXButton("自动下载");
        AutoDownload.setPrefSize(105, 30);
        AnchorPane.setTopAnchor(AutoDownload, 80.0);
        AnchorPane.setLeftAnchor(AutoDownload, 0.0);
        GameSetting.setDisable(true);
        setVersionSettingPane(SonScrollPane);
        GameSetting.setOnAction(event -> {
            AutoDownload.setDisable(false);
            GameSetting.setDisable(true);
            setVersionSettingPane(SonScrollPane);
        });
        AutoDownload.setOnAction(event -> {
            GameSetting.setDisable(false);
            AutoDownload.setDisable(true);
            setAutoDownload(SonScrollPane);
        });
        JFXButton completion = new JFXButton("补全游戏文件");
        completion.setLayoutY(395);
        completion.setPrefSize(105, 30);
        AnchorPane.setBottomAnchor(completion, 30.0);
        AnchorPane.setLeftAnchor(completion, 0.0);
        JFXButton delete = new JFXButton("删除该版本");
        delete.setLayoutY(425);
        delete.setPrefSize(105, 30);
        AnchorPane.setBottomAnchor(delete, 0.0);
        AnchorPane.setLeftAnchor(delete, 0.0);
        ParentPane.getChildren().addAll(SonScrollPane, completion, delete, back, GameSetting, AutoDownload);
        Consoler.setCss("BlackBorder", back);
        ParentPane.setBackground(Consoler.getBackground());
        Consoler.setStylesheets(ParentPane);
        MainStage.setScene(new Scene(ParentPane));
        Consoler.setCss("BackGroundWriteButton", delete, completion, GameSetting, AutoDownload);


        delete.setOnAction(event -> {
            try {
                FileUtils.deleteDirectory(launcher.getVersionPath());
                HomeWindow homeWindow = new HomeWindow(null);
                homeWindow.setHome(MainStage);
                JsonObject object = SettingObject().getJsonObjects();
                object.remove("PreferredVersion");
                JSONUtils.ObjectToJsonFile(GetSettingFile(), object);
                logmaker.info("* " + launcher.getVersionNumber() + " Deleted");
            } catch (IOException e) {
                ErrorWindow.setErrorWin(e);
            }
        });
        completion.setOnAction(event -> ThreadUtils.StartThread(() -> {
            InstallGameVersion version = new InstallGameVersion(launcher, true);
            version.DownloadGame();
            logmaker.info("* " + launcher.getVersionNumber() + " downloaded");
        }));
    }

    private void setVersionSettingPane(ScrollPane scrollPane) {
        AnchorPane pane = new AnchorPane();
        double line = 35;
        Label tips = new Label("JDK地址:");
        tips.setLayoutX(layoutX);
        tips.setLayoutY(line);
        Label tips2 = new Label("版本:");
        tips2.setLayoutX(107);
        tips2.setLayoutY(line);
        double line2 = 55;
        TextField JavaPath = new TextField();
        JavaPath.setLayoutX(layoutX);
        JavaPath.setLayoutY(line2);
        JavaPath.setPrefSize(300, 23);
        JFXButton choose = new JFXButton("...");
        choose.setLayoutX(315);
        choose.setLayoutY(line2);


        Label tips3 = new Label("游戏运行内存:");
        tips3.setLayoutX(layoutX);
        tips3.setLayoutY(89);
        TextField Input = new TextField();
        Input.setLayoutX(layoutX);
        Input.setLayoutY(104);
        Input.setPrefSize(90, 23);

        double line3 = 138;
        Label tips4 = new Label("窗口宽度:");
        tips4.setLayoutX(layoutX);
        tips4.setLayoutY(line3);
        Label tips5 = new Label("窗口高度:");
        tips5.setLayoutX(165);
        tips5.setLayoutY(line3);

        double line4 = 156;
        TextField InputWidth = new TextField();
        InputWidth.setLayoutX(layoutX);
        InputWidth.setLayoutY(line4);
        InputWidth.setPrefSize(90, 23);
        TextField InputHeight = new TextField();
        InputHeight.setLayoutX(166);
        InputHeight.setLayoutY(line4);
        InputHeight.setPrefSize(90, 23);

        Label tips6 = new Label();
        tips6.setLayoutX(340);
        tips6.setLayoutY(340);
        tips6.setPrefSize(122, 15);

        JFXButton apply = new JFXButton("应用");
        apply.setPrefSize(150, 50);
        AnchorPane.setBottomAnchor(apply, 10.0);
        AnchorPane.setRightAnchor(apply, 30.0);

        size.ModifyWindwosSize(pane, tips, tips2, tips3, tips4, tips5, tips6, Input, JavaPath, InputHeight, InputWidth, choose);
        pane.getChildren().add(apply);
        Consoler.setStylesheets(pane);
        Consoler.setCss("BlackBorder", choose, apply);
        scrollPane.setContent(pane);
        JavaPath.setText(config.getJavaPath());
        InputWidth.setText(String.valueOf(config.getWindowWidth()));
        InputHeight.setText(String.valueOf(config.getWindowHeight()));
        Input.setText(String.valueOf(config.getRunningMemory()));
        tips2.setText("Java版本: " + JavaHomePath.getJavaVersion(config.getJavaPath()));
        choose.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择Java文件");
            fileChooser.setInitialDirectory(new File("C:\\Program Files"));
            File JavaExePath = fileChooser.showOpenDialog(MainStage);
            if (JavaExePath != null) {
                JavaPath.setText(JavaExePath.getAbsolutePath());
            }
        });
        apply.setOnAction(event -> {
            try {
                DefaultGameConfig.Config NewConfig = new DefaultGameConfig.Config();
                try {
                    if (!PlatformUtils.FileExistenceAndSize(JavaPath.getText()))
                        NewConfig.setJavaHome(JavaPath.getText());
                } catch (IOException e) {
                    throw new NumberFormatException();
                }
                NewConfig.setXmx(Integer.parseInt(Input.getText()));
                NewConfig.setHight(Integer.parseInt(InputHeight.getText()));
                NewConfig.setWidth(Integer.parseInt(InputWidth.getText()));
                DefaultGameConfig gameConfig = launcher.getGameConfig().getDefaultGameConfig();
                gameConfig.setConfig(NewConfig);
                logmaker.info(gameConfig);
                launcher.getGameConfig().PutConfigToFile(gameConfig);
                tips6.setText("设置成功");
                tips2.setText("Java版本: " + JavaHomePath.getJavaVersion(JavaPath.getText()));
            } catch (NumberFormatException e) {
                tips6.setTextFill(Color.RED);
                tips6.setText("请输入正确配置");
                logmaker.warn("* 配置无效", e);
            }
        });
    }

    private void setAutoDownload(ScrollPane scrollPane) {
        AnchorPane ModList = new AnchorPane();
        double i = 0;
        for (ModUtils.KindOfMod kind : ModUtils.KindOfMod.values()) {
            AnchorPane ModPane = new AnchorPane();
            AnchorPane.setTopAnchor(ModPane, 44 * i);
            ModPane.setPrefHeight(44);
            ModPane.setPrefWidth(510);
            setModPane(kind, ModPane);
            size.ModifyWindwosSize(ModList, ModPane);
            i++;
        }
        Consoler.setStylesheets(ModList);
        scrollPane.setContent(ModList);
    }

    private void setModPane(ModUtils.KindOfMod kind, AnchorPane ModPane) {
        ImageView ModIcon = new ImageView();
        switch (kind) {
            case FORGE ->
                    ModIcon.setImage(new Image(requireNonNull(VersionSettingWindows.class.getResourceAsStream("/forge.png"))));
            case FABRIC ->
                    ModIcon.setImage(new Image(requireNonNull(VersionSettingWindows.class.getResourceAsStream("/fabric.png"))));
            case QUILT ->
                    ModIcon.setImage(new Image(requireNonNull(VersionSettingWindows.class.getResourceAsStream("/quilt.png"))));
            case Original ->
                    ModIcon.setImage(new Image(requireNonNull(VersionSettingWindows.class.getResourceAsStream("/ico.jpg"))));
        }
        AnchorPane.setTopAnchor(ModIcon, 4.0);
        AnchorPane.setLeftAnchor(ModIcon, 10.0);
        AnchorPane.setBottomAnchor(ModIcon, 4.0);
        Label ModVersion = new Label();
        if (launcher.getKind() == kind) {
            DownloadInfo info = ModUtils.getVersionModInstall(launcher, kind);
            if (info != null) {
                ModVersion.setText(kind + " : " + info.getModVersion());
            } else {
                ModVersion.setText(kind + " : 不安装");
            }
        } else {
            ModVersion.setText(kind + " : 不安装");
        }
        AnchorPane.setBottomAnchor(ModVersion, 15.0);
        AnchorPane.setLeftAnchor(ModVersion, 60.0);
        AnchorPane.setTopAnchor(ModVersion, 15.0);
        JFXButton Download = new JFXButton("-->");
        AnchorPane.setTopAnchor(Download, 11.0);
        AnchorPane.setRightAnchor(Download, 20.0);
        AnchorPane.setBottomAnchor(Download, 11.0);
        ModPane.getChildren().addAll(ModIcon, ModVersion, Download);
    }
}
