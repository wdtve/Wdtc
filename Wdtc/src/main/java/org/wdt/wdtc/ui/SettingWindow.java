package org.wdt.wdtc.ui;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.wdt.utils.FileUtils;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.Starter;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


public class SettingWindow extends AboutSetting {
    private static final Logger logmaker = WdtcLogger.getLogger(SettingWindow.class);

    private SettingWindow() {
    }

    public static void setSettingWin(Stage MainStage) throws IOException {
        Setting setting = AboutSetting.getSetting();
        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> {
            HomeWindow win = new HomeWindow();
            win.setHome(MainStage);
            logmaker.info(getSetting());
        });
        back.getStyleClass().add("BlackBorder");
        MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion() + " - Setting");

        double line = 55.0;
        TextField GamePath = new TextField();
        GamePath.setText(FileUtils.getCanonicalPath(AboutSetting.getSetting().getDefaultGamePath()));
        GamePath.setLayoutX(coordinate.layoutX);
        GamePath.setLayoutY(line);
        GamePath.setPrefSize(297.0, 23.0);
        Button button = new Button("...");
        button.setLayoutX(325.0);
        button.setLayoutY(line);
        button.setOnMousePressed(event -> {
            if (event.isControlDown()) {
                PlatformUtils.StartPath(GetSettingFile());
                logmaker.info("* 设置文件" + GetSettingFile() + "已打开");
            } else {
                try {
                    DirectoryChooser fileChooser = new DirectoryChooser();
                    fileChooser.setTitle("选择游戏文件夹");
                    fileChooser.setInitialDirectory(getSetting().getDefaultGamePath());
                    File file = fileChooser.showDialog(MainStage);
                    if (Objects.nonNull(file)) {
                        setting.setDefaultGamePath(file);
                        putSettingToFile(setting);
                        GamePath.setText(file.getCanonicalPath());
                        logmaker.info("* 游戏文件夹已更改为:" + file);
                    }
                } catch (IOException e) {
                    ErrorWindow.setErrorWin(e);
                }
            }
        });

        double line1 = 35.0;
        Label tips = new Label("游戏文件夹位置:");
        tips.setLayoutX(coordinate.layoutX);
        tips.setLayoutY(line1);
        Label tips2 = new Label("如:选择C盘则游戏文件夹为\"C:\\minceaft\"");
        tips2.setLayoutX(107.0);
        tips2.setLayoutY(line1);
        tips2.getStyleClass().add("tips");

        Label cmd = new Label("启动时是否显示控制台窗口(如果按启动后长时间没反应可以设置显示,默认不显示):");
        cmd.setLayoutX(coordinate.layoutX);
        cmd.setLayoutY(89.0);
        RadioButton TrueLog = new RadioButton("显示");
        RadioButton FalseLog = new RadioButton("不显示");
        double line2 = 111.0;
        TrueLog.setLayoutX(coordinate.layoutX);
        TrueLog.setLayoutY(line2);
        FalseLog.setLayoutX(coordinate.layoutX2);
        FalseLog.setLayoutY(line2);
        FalseLog.setOnAction(event -> {
            TrueLog.setSelected(false);
            logmaker.info("* 启动日志器关闭显示");
            setting.setConsole(false);
            putSettingToFile(setting);
        });
        TrueLog.setOnAction(event -> {
            FalseLog.setSelected(false);
            logmaker.info("* 启动日志器开启显示");
            setting.setConsole(true);
            putSettingToFile(setting);
        });

        Label DownloadSourceTips = new Label("选择下载源(默认选择Official):");
        DownloadSourceTips.setLayoutX(coordinate.layoutX);
        DownloadSourceTips.setLayoutY(138.0);
        RadioButton OfficialDownloadSource = new RadioButton("Official");
        RadioButton BmclDownloadSource = new RadioButton("Bmcl");
        RadioButton McbbsDownloadSource = new RadioButton("Mcbbs");
        double line3 = 159.0;
        OfficialDownloadSource.setLayoutX(coordinate.layoutX);
        OfficialDownloadSource.setLayoutY(line3);
        BmclDownloadSource.setLayoutX(coordinate.layoutX2);
        BmclDownloadSource.setLayoutY(line3);
        McbbsDownloadSource.setLayoutX(281);
        McbbsDownloadSource.setLayoutY(line3);
        OfficialDownloadSource.setOnAction(event -> {
            BmclDownloadSource.setSelected(false);
            McbbsDownloadSource.setSelected(false);
            logmaker.info("* Switch to Official DownloadSource");
            setting.setDownloadSource(FileUrl.DownloadSourceList.OFFICIAL);
            putSettingToFile(setting);
        });
        BmclDownloadSource.setOnAction(event -> {
            OfficialDownloadSource.setSelected(false);
            McbbsDownloadSource.setSelected(false);
            logmaker.info("* Switch to Bmcl DownloadSource");
            setting.setDownloadSource(FileUrl.DownloadSourceList.BMCLAPI);
            putSettingToFile(setting);
        });
        McbbsDownloadSource.setOnAction(event -> {
            OfficialDownloadSource.setSelected(false);
            BmclDownloadSource.setSelected(false);
            logmaker.info("* Switch to Mcbbs DownloadSource");
            setting.setDownloadSource(FileUrl.DownloadSourceList.MCBBS);
            putSettingToFile(setting);
        });


        Label tips3 = new Label("是否启用OpenGL软渲染器:");
        tips3.setLayoutX(coordinate.layoutX);
        tips3.setLayoutY(185.0);
        RadioButton TrueOpenGl = new RadioButton("启用");
        RadioButton FalseOpenGL = new RadioButton("不启用");
        double line4 = 209.0;
        TrueOpenGl.setLayoutX(coordinate.layoutX);
        TrueOpenGl.setLayoutY(line4);
        FalseOpenGL.setLayoutX(coordinate.layoutX2);
        FalseOpenGL.setLayoutY(line4);
        TrueOpenGl.setOnAction(event -> {
            FalseOpenGL.setSelected(false);
            setting.setLlvmpipeLoader(true);
            logmaker.info("* OpenGL软渲染已开启");
            putSettingToFile(setting);
        });
        FalseOpenGL.setOnAction(event -> {
            TrueOpenGl.setSelected(false);
            setting.setLlvmpipeLoader(false);
            logmaker.info("* OpenGL软渲染已关闭");
            putSettingToFile(setting);
        });

        Label tips4 = new Label("将游戏设置成中文(默认开启):");
        tips4.setLayoutX(coordinate.layoutX);
        tips4.setLayoutY(235.0);
        RadioButton TrueZhcn = new RadioButton("启用");
        double line5 = 254.0;
        TrueZhcn.setLayoutX(coordinate.layoutX);
        TrueZhcn.setLayoutY(line5);
        RadioButton FalseZhcn = new RadioButton("不启用");
        FalseZhcn.setLayoutX(coordinate.layoutX2);
        FalseZhcn.setLayoutY(line5);
        FalseZhcn.setOnAction(event -> {
            setting.setChineseLanguage(false);
            TrueZhcn.setSelected(false);
            logmaker.info("* 取消将游戏设置为中文");
            putSettingToFile(setting);
        });
        TrueZhcn.setOnAction(event -> {
            setting.setChineseLanguage(true);
            FalseZhcn.setSelected(false);
            logmaker.info("* 将游戏设置为中文");
            putSettingToFile(setting);
        });

        JFXButton ExportLog = new JFXButton("导出日志");
        ExportLog.setLayoutY(420);
        AnchorPane.setLeftAnchor(ExportLog, 0.0);
        AnchorPane.setBottomAnchor(ExportLog, 0.0);
        ExportLog.setPrefSize(105, 30);
        ExportLog.setOnAction(event -> {
            try {
                DirectoryChooser fileChooser = new DirectoryChooser();
                fileChooser.setTitle("选择日志文件保存路径");
                fileChooser.setInitialDirectory(FilePath.getWdtcConfig());
                File logDirectory = fileChooser.showDialog(MainStage);
                if (Objects.nonNull(logDirectory)) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    File srcFile = new File(FilePath.getWdtcCache() + "/logs/Wdtc.log");
                    File logFile = new File(logDirectory.getAbsolutePath() + "/Wdtc-Demo-" + formatter.format(calendar.getTime()) + ".log");
                    FileUtils.copyFile(srcFile, logFile);
                    logmaker.info("* 日志已导出:" + logFile);
                }
            } catch (IOException e) {
                ErrorWindow.setErrorWin(e);
            }
        });

        JFXButton CleanCache = new JFXButton();
        CleanCache.setText("清除缓存:" + FileUtils.sizeOfDirectory(FilePath.getWdtcCache()) + "B");
        CleanCache.setPrefSize(105, 30);
        AnchorPane.setLeftAnchor(CleanCache, 0.0);
        AnchorPane.setBottomAnchor(CleanCache, 30.0);
        CleanCache.setOnMousePressed(event -> {
            if (event.isControlDown()) {
                PlatformUtils.StartPath(FilePath.getWdtcCache());
                logmaker.info("* 缓存文件夹已打开");
            } else {
                try {
                    FileUtils.cleanDirectory(FilePath.getWdtcCache());
                    logmaker.info("* Cache Folder Cleaned");
                    CleanCache.setText("清除缓存:" + FileUtils.sizeOfDirectory(FilePath.getWdtcCache()) + "B");
                } catch (IOException e) {
                    logmaker.error("* Clean Cache Folder Error,", e);
                }
            }
        });

        AnchorPane SonPane = new AnchorPane();
        Consoler.setTopGrid(SonPane);

        SonPane.setPrefSize(493, 336);
        WindwosSize size = new WindwosSize(MainStage);
        size.ModifyWindwosSize(SonPane, back, OfficialDownloadSource, BmclDownloadSource, McbbsDownloadSource, TrueLog,
                FalseLog, cmd, DownloadSourceTips, GamePath, tips2, tips, button, tips3, TrueOpenGl, FalseOpenGL, tips4, TrueZhcn, FalseZhcn);
        ScrollPane scrollPane = new ScrollPane(SonPane);
        AnchorPane.setLeftAnchor(scrollPane, 105.0);
        AnchorPane.setTopAnchor(scrollPane, 70.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);

        AnchorPane pane = new AnchorPane();
        Consoler.setCss("BackGroundWriteButton", ExportLog, CleanCache);
        Consoler.setStylesheets(pane);
        pane.getChildren().addAll(scrollPane, back, ExportLog, CleanCache);
        pane.setBackground(Consoler.getBackground());
        MainStage.setScene(new Scene(pane));
        FalseLog.setSelected(!setting.isConsole());
        FalseOpenGL.setSelected(!setting.isLlvmpipeLoader());
        FalseZhcn.setSelected(!setting.isChineseLanguage());
        TrueLog.setSelected(setting.isConsole());
        TrueOpenGl.setSelected(setting.isLlvmpipeLoader());
        TrueZhcn.setSelected(setting.isChineseLanguage());

        switch (setting.getDownloadSource()) {
            case MCBBS -> McbbsDownloadSource.setSelected(true);
            case BMCLAPI -> BmclDownloadSource.setSelected(true);
            case OFFICIAL -> OfficialDownloadSource.setSelected(true);
        }

    }

    private static class coordinate {
        public static final double layoutX = 20.0;
        public static final double layoutX2 = 138.0;
    }
}
