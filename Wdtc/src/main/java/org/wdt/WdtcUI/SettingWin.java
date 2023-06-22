package org.wdt.WdtcUI;

import com.alibaba.fastjson2.JSONObject;
import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.AboutSetting;
import org.wdt.FilePath;
import org.wdt.platform.PlatformUtils;
import org.wdt.platform.Starter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


public class SettingWin extends AboutSetting {
    private static final Logger logmaker = Logger.getLogger(SettingWin.class);

    private SettingWin() {
    }

    public static void setSettingWin(Stage MainStage) throws IOException {
        JSONObject SettingJson = SettingObject();
        Pane SonPane = new Pane();
        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> HomeWin.setHome(MainStage));
        MainStage.setTitle("Wdtc - " + Starter.getLauncherVersion() + " - Setting");
        RadioButton TrueLog = new RadioButton("显示");
        RadioButton FalseLog = new RadioButton("不显示");
        TrueLog.setLayoutX(77.0);
        TrueLog.setLayoutY(111.0);
        FalseLog.setLayoutY(111.0);
        FalseLog.setLayoutX(139.0);
        FalseLog.setOnAction(event -> {
            TrueLog.setSelected(false);
            logmaker.info("* 启动日志器关闭显示");
            PlatformUtils.PutKeyToFile(GetSettingFile(), SettingJson, "log", false);
        });
        TrueLog.setOnAction(event -> {
            FalseLog.setSelected(false);
            logmaker.info("* 启动日志器开启显示");
            PlatformUtils.PutKeyToFile(GetSettingFile(), SettingJson, "log", true);
        });
        Label cmd = new Label("启动时是否显示cmd窗口(如果按启动后长时间没反应可以设置显示,默认不显示):");
        cmd.setLayoutX(77.0);
        cmd.setLayoutY(89.0);
        Label BMCLAPIMessage = new Label("是否启用BMCLAPI下载源(启用后下载速度也许会更快,默认不启用):");
        BMCLAPIMessage.setLayoutX(77.0);
        BMCLAPIMessage.setLayoutY(138.0);
        RadioButton TrueBmcl = new RadioButton("启用");
        RadioButton FalseBmcl = new RadioButton("不启用");
        TrueBmcl.setLayoutX(76.0);
        TrueBmcl.setLayoutY(159.0);
        FalseBmcl.setLayoutX(139.0);
        FalseBmcl.setLayoutY(159.0);
        TrueBmcl.setOnAction(event -> {
            FalseBmcl.setSelected(false);
            logmaker.info("* BMCLAPI下载加速已开启");
            PlatformUtils.PutKeyToFile(GetSettingFile(), SettingJson, "bmcl", true);
        });
        FalseBmcl.setOnAction(event -> {
            TrueBmcl.setSelected(false);
            logmaker.info("* BMCLAPI下载加速已关闭");
            PlatformUtils.PutKeyToFile(GetSettingFile(), SettingJson, "bmcl", false);
        });
        Button ExportLog = new Button("导出日志");
        ExportLog.setLayoutX(76.0);
        ExportLog.setLayoutY(298.0);
        ExportLog.setPrefHeight(23.0);
        ExportLog.setPrefWidth(64.0);
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
        TextField GamePath = new TextField();
        GamePath.setText(AboutSetting.GetDefaultGamePath());
        GamePath.setLayoutX(77.0);
        GamePath.setLayoutY(55.0);
        GamePath.setPrefHeight(23.0);
        GamePath.setPrefWidth(234.0);
        Label tips = new Label("游戏文件夹位置:");
        tips.setLayoutX(78.0);
        tips.setLayoutY(35.0);
        Label tips2 = new Label("如:选择C盘则游戏文件夹为\"C:\\minceaft\"");
        tips2.setTextFill(Color.GRAY);
        tips2.setLayoutX(168.0);
        tips2.setLayoutY(35.0);
        Button button = new Button("...");
        button.setLayoutX(315.0);
        button.setLayoutY(55.0);
        button.setOnMousePressed(event -> {
            if (event.isControlDown()) {
                try {
                    Runtime.getRuntime().exec("cmd.exe /c " + GetSettingFile());
                    logmaker.info("* 设置文件" + GetSettingFile() + "已打开");
                } catch (IOException e) {
                    ErrorWin.setErrorWin(e);
                }
            } else {
                try {
                    DirectoryChooser fileChooser = new DirectoryChooser();
                    fileChooser.setTitle("选择游戏文件夹");
                    fileChooser.setInitialDirectory(new File(GetDefaultGamePath()));
                    File file = fileChooser.showDialog(MainStage);
                    if (Objects.nonNull(file)) {
                        PlatformUtils.PutKeyToFile(GetSettingFile(), SettingJson, "DefaultGamePath", file);
                        GamePath.setText(file.getCanonicalPath());
                        logmaker.info("* 游戏文件夹已更改为:" + file);
                    }
                } catch (IOException e) {
                    ErrorWin.setErrorWin(e);
                }
            }
        });
        Label tips3 = new Label("是否启用OpenGL软渲染器:");
        tips3.setLayoutX(76.0);
        tips3.setLayoutY(185.0);
        RadioButton TrueOpenGl = new RadioButton("启用");
        RadioButton FalseOpenGL = new RadioButton("不启用");
        TrueOpenGl.setLayoutX(77.0);
        TrueOpenGl.setLayoutY(209.0);
        FalseOpenGL.setLayoutX(139.0);
        FalseOpenGL.setLayoutY(209.0);
        TrueOpenGl.setOnAction(event -> {
            FalseOpenGL.setSelected(false);
            PlatformUtils.PutKeyToFile(GetSettingFile(), SettingJson, "llvmpipe-loader", true);
            logmaker.info("* OpenGL软渲染已开启");
        });
        FalseOpenGL.setOnAction(event -> {
            TrueOpenGl.setSelected(false);
            PlatformUtils.PutKeyToFile(GetSettingFile(), SettingJson, "llvmpipe-loader", false);
            logmaker.info("* OpenGL软渲染已关闭");
        });
        Label tips4 = new Label("将游戏设置成中文(默认开启):");
        tips4.setLayoutX(76.0);
        tips4.setLayoutY(235.0);
        RadioButton TrueZhcn = new RadioButton("启用");
        TrueZhcn.setLayoutX(77.0);
        TrueZhcn.setLayoutY(254.0);
        RadioButton FalseZhcn = new RadioButton("不启用");
        FalseZhcn.setLayoutX(139.0);
        FalseZhcn.setLayoutY(254.0);
        FalseOpenGL.setOnAction(event -> {
            PlatformUtils.PutKeyToFile(GetSettingFile(), SettingJson, "ZH-CN", false);
            TrueZhcn.setSelected(false);
            logmaker.info("* 取消将游戏设置为中文");
        });
        TrueZhcn.setOnAction(event -> {
            PlatformUtils.PutKeyToFile(GetSettingFile(), SettingJson, "ZH-CN", true);
            FalseZhcn.setSelected(false);
            logmaker.info("* 将游戏设置为中文");
        });
        SonPane.getChildren().addAll(back, TrueBmcl, FalseBmcl, TrueLog, FalseLog, cmd, BMCLAPIMessage, ExportLog,
                GamePath, tips2, tips, button, tips3, TrueOpenGl, FalseOpenGL, tips4, TrueZhcn, FalseZhcn);
        SonPane.setPrefHeight(448.0);
        SonPane.setPrefWidth(598.0);
        SonPane.setBackground(Consoler.getBackground());
        ScrollPane scrollPane = new ScrollPane(SonPane);
        Scene scene = new Scene(scrollPane, 600.0, 450.0);
        MainStage.setScene(scene);
        FalseBmcl.setSelected(!GetBmclSwitch());
        FalseLog.setSelected(!GetLogSwitch());
        FalseOpenGL.setSelected(!GetLlvmpipeSwitch());
        FalseZhcn.setSelected(!GetZHCNSwitch());
        TrueBmcl.setSelected(GetBmclSwitch());
        TrueLog.setSelected(GetLogSwitch());
        TrueOpenGl.setSelected(GetLlvmpipeSwitch());
        TrueZhcn.setSelected(GetZHCNSwitch());
    }
}
