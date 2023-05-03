package org.wdt.WdtcUI;

import com.alibaba.fastjson2.JSONObject;
import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.AboutSetting;
import org.wdt.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class SettingWin extends AboutSetting {
    private static final Logger logmaker = Logger.getLogger(SettingWin.class);

    private SettingWin() {
    }

    public static void setSettingWin(Stage MainStage) throws IOException {
        JSONObject SettingJson = StringUtil.FileToJSONObject(GetSettingFile());
        Pane pane = new Pane();
        JFXButton back = new JFXButton("返回");
        back.setOnAction(event -> HomeWin.setHome(MainStage));
        back.setStyle("-fx-border-color: #000000");
        MainStage.setTitle("Wdtc - Demo - Setting");
        RadioButton true_log = new RadioButton("显示");
        RadioButton false_log = new RadioButton("不显示");
        true_log.setLayoutX(77.0);
        true_log.setLayoutY(111.0);
        false_log.setLayoutY(111.0);
        false_log.setLayoutX(139.0);
        false_log.setOnAction(event -> {
            true_log.setSelected(false);
            logmaker.info("* 启动日志器关闭显示");
            SettingJson.put("log", false);
            StringUtil.PutJSONObject(GetSettingFile(), SettingJson);
        });
        true_log.setOnAction(event -> {
            false_log.setSelected(false);
            logmaker.info("* 启动日志器开启显示");
            SettingJson.put("log", true);
            StringUtil.PutJSONObject(GetSettingFile(), SettingJson);
        });
        Label cmd = new Label("启动时是否显示cmd窗口(如果按启动后长时间没反应可以设置显示,默认不显示):");
        cmd.setLayoutX(77.0);
        cmd.setLayoutY(89.0);
        Label BMCLAPI_Mess = new Label("是否启用BMCLAPI下载源(启用后下载速度也许会更快,默认不启用):");
        BMCLAPI_Mess.setLayoutX(77.0);
        BMCLAPI_Mess.setLayoutY(138.0);
        RadioButton true_bmcl = new RadioButton("启用");
        RadioButton false_bmcl = new RadioButton("不启用");
        true_bmcl.setLayoutX(76.0);
        true_bmcl.setLayoutY(159.0);
        false_bmcl.setLayoutX(139.0);
        false_bmcl.setLayoutY(159.0);
        true_bmcl.setOnAction(event -> {
            try {
                false_bmcl.setSelected(false);
                logmaker.info("* BMCLAPI下载加速已开启");
                SettingJson.put("bmcl", true);
                FileUtils.writeStringToFile(AboutSetting.GetSettingFile(), SettingJson.toString(), "UTF-8");
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        false_bmcl.setOnAction(event -> {
            true_bmcl.setSelected(false);
            logmaker.info("* BMCLAPI下载加速已关闭");
            SettingJson.put("bmcl", false);
            StringUtil.PutJSONObject(AboutSetting.GetSettingFile(), SettingJson);
        });
        Button export_log = new Button("导出日志");
        export_log.setLayoutX(76.0);
        export_log.setLayoutY(298.0);
        export_log.setPrefHeight(23.0);
        export_log.setPrefWidth(64.0);
        export_log.setOnAction(event -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("选择日志文件保存路径");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("日志文件", "*.log"));
                File logfile = fileChooser.showSaveDialog(MainStage);
                if (Objects.nonNull(logfile)) {
                    File srcFile = new File("ResourceFile/WdtcLog/Wdtc.log");
                    FileUtils.copyFile(srcFile, logfile);
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
        button.setOnAction(event -> {
            try {
                DirectoryChooser fileChooser = new DirectoryChooser();
                fileChooser.setTitle("选择游戏文件夹");
                fileChooser.setInitialDirectory(new File(GetDefaultGamePath()));
                File file = fileChooser.showDialog(MainStage);
                if (Objects.nonNull(file)) {
                    SettingJson.put("DefaultGamePath", file);
                    StringUtil.PutJSONObject(file, SettingJson);
                    GamePath.setText(file.getCanonicalPath());
                }
            } catch (IOException e) {
                ErrorWin.setErrorWin(e);
            }
        });
        Label tips3 = new Label("是否启用OpenGL软渲染器:");
        tips3.setLayoutX(76.0);
        tips3.setLayoutY(185.0);
        RadioButton TrueOpenGl = new RadioButton("启用");
        RadioButton FalseOpenGL = new RadioButton("不启用");
        TrueOpenGl.setLayoutX(78.0);
        TrueOpenGl.setLayoutY(209.0);
        TrueOpenGl.setOnAction(event -> {
            FalseOpenGL.setSelected(false);
            SettingJson.put("llvmpipe-loader", true);
            StringUtil.PutJSONObject(GetSettingFile(), SettingJson);
        });
        FalseOpenGL.setOnAction(event -> {
            TrueOpenGl.setSelected(false);
            SettingJson.put("llvmpipe-loader", false);
            StringUtil.PutJSONObject(GetSettingFile(), SettingJson);
        });
        FalseOpenGL.setLayoutX(139.0);
        FalseOpenGL.setLayoutY(209.0);
        pane.getChildren().addAll(back, true_bmcl, false_bmcl, true_log, false_log, cmd, BMCLAPI_Mess, export_log, GamePath, tips2, tips, button, tips3, TrueOpenGl, FalseOpenGL);
        Scene scene = new Scene(pane, 600.0, 400.0);
        MainStage.setScene(scene);
        false_bmcl.setSelected(!GetBmclSwitch());
        false_log.setSelected(!GetLogSwitch());
        FalseOpenGL.setSelected(!GetLlvmpipeSwitch());
        true_bmcl.setSelected(GetBmclSwitch());
        true_log.setSelected(GetLogSwitch());
        TrueOpenGl.setSelected(GetLlvmpipeSwitch());
    }
}
