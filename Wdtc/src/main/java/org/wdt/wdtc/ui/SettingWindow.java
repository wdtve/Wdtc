package org.wdt.wdtc.ui;

import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.Starter;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


public class SettingWindow extends AboutSetting {
    private static final Logger logmaker = getWdtcLogger.getLogger(SettingWindow.class);

    private SettingWindow() {
    }

    public static void setSettingWin(Stage MainStage) throws IOException {
        Setting setting = AboutSetting.getSetting();
        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> {
            HomeWindow win = new HomeWindow();
            win.setHome(MainStage);
        });
        MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion() + " - Setting");

        double line = 55.0;
        TextField GamePath = new TextField();
        GamePath.setText(AboutSetting.getSetting().getDefaultGamePath());
        GamePath.setLayoutX(coordinate.layoutX);
        GamePath.setLayoutY(line);
        GamePath.setPrefSize(297.0, 23.0);
        Button button = new Button("...");
        button.setLayoutX(325.0);
        button.setLayoutY(line);
        button.setOnMousePressed(event -> {
            if (event.isControlDown()) {
                try {
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", GetSettingFile().getCanonicalPath()});
                    logmaker.info("* 设置文件" + GetSettingFile() + "已打开");
                } catch (IOException e) {
                    ErrorWin.setErrorWin(e);
                }
            } else {
                try {
                    DirectoryChooser fileChooser = new DirectoryChooser();
                    fileChooser.setTitle("选择游戏文件夹");
                    fileChooser.setInitialDirectory(new File(getSetting().getDefaultGamePath()));
                    File file = fileChooser.showDialog(MainStage);
                    if (Objects.nonNull(file)) {
                        setting.setDefaultGamePath(file.getCanonicalPath());
                        putSettingToFile(setting);
                        GamePath.setText(file.getCanonicalPath());
                        logmaker.info("* 游戏文件夹已更改为:" + file);
                    }
                } catch (IOException e) {
                    ErrorWin.setErrorWin(e);
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

        Label BMCLAPIMessage = new Label("是否启用BMCLAPI下载源(启用后下载速度也许会更快,默认不启用):");
        BMCLAPIMessage.setLayoutX(coordinate.layoutX);
        BMCLAPIMessage.setLayoutY(138.0);
        RadioButton TrueBmcl = new RadioButton("启用");
        RadioButton FalseBmcl = new RadioButton("不启用");
        double line3 = 159.0;
        TrueBmcl.setLayoutX(coordinate.layoutX);
        TrueBmcl.setLayoutY(line3);
        FalseBmcl.setLayoutX(coordinate.layoutX2);
        FalseBmcl.setLayoutY(line3);
        TrueBmcl.setOnAction(event -> {
            FalseBmcl.setSelected(false);
            logmaker.info("* BMCLAPI下载加速已开启");
            setting.setBmcl(true);
            putSettingToFile(setting);
        });
        FalseBmcl.setOnAction(event -> {
            TrueBmcl.setSelected(false);
            logmaker.info("* BMCLAPI下载加速已关闭");
            setting.setBmcl(false);
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
                    File srcFile = new File(System.getProperty("user.home") + "/.wdtc/logs/Wdtc.log");
                    File logFile = new File(logDirectory.getAbsolutePath() + "/Wdtc-Demo-" + formatter.format(calendar.getTime()) + ".log");
                    FileUtils.copyFile(srcFile, logFile);
                    logmaker.info("* 日志已导出:" + logFile);
                }
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });

        JFXButton CleanCache = new JFXButton();
        CleanCache.setText("清除缓存:" + FileUtils.sizeOfDirectory(FilePath.getWdtcCache()) + "B");
        CleanCache.setPrefSize(105, 30);
        AnchorPane.setLeftAnchor(CleanCache, 0.0);
        AnchorPane.setBottomAnchor(CleanCache, 30.0);
        CleanCache.setOnMousePressed(event -> {
            if (event.isControlDown()) {
                try {
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", FilePath.getWdtcCache().getCanonicalPath()});
                    logmaker.info("* 缓存文件夹已打开");
                } catch (IOException e) {
                    logmaker.error("* OpenFolder Error,", e);
                }
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
        size.ModifyWindwosSize(SonPane, back, TrueBmcl, FalseBmcl, TrueLog, FalseLog, cmd, BMCLAPIMessage, GamePath,
                tips2, tips, button, tips3, TrueOpenGl, FalseOpenGL, tips4, TrueZhcn, FalseZhcn);
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
        FalseBmcl.setSelected(!setting.isBmcl());
        FalseLog.setSelected(!setting.isConsole());
        FalseOpenGL.setSelected(!setting.isLlvmpipeLoader());
        FalseZhcn.setSelected(!setting.isChineseLanguage());
        TrueBmcl.setSelected(setting.isBmcl());
        TrueLog.setSelected(setting.isConsole());
        TrueOpenGl.setSelected(setting.isLlvmpipeLoader());
        TrueZhcn.setSelected(setting.isChineseLanguage());
    }

    private static void putKey(JsonObject object) {
        try {
            FileUtils.writeStringToFile(GetSettingFile(), JSONObject.toJSONString(object), "UTF-8");
        } catch (IOException e) {
            logmaker.error("* Put File Error,", e);
        }
    }

    private static class coordinate {
        public static final double layoutX = 20.0;
        public static final double layoutX2 = 138.0;
    }
}
