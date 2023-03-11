# Wdtc-demo😎

#### 一个简单到不能再简单的我的世界Java版启动器

---

## JDK:

### corretto-17

## IDE:

### IntelliJ IDEA

- Gradle

---

### 国内快速下载源:[BMCLAPI](https://bmclapidoc.bangbang93.com/)

---

### 构建方法:
- 等gradle自动下载好依赖后
- 找到[AppMain](Wdtc/src/main/java/org/WdtcUI/AppMain.java)类文件里的main函数

```
public static void main(String[] args) {
    //右键运行
    Application.launch();
}
```

---

## 许可证:

### GPL-2.0

---

### 其他:

#### JavaFx报错

- 要是编译时报错:"错误: 缺少 JavaFX 运行时组件, 需要使用该组件来运行此应用程序"
- 可以自行百度,也可以添加module-info.java文件,这个自己看着写
- 解决方法之一:下载好javafx-sdk-17.0.6后放在项目根目录里然后在IDE中运行配置里添加

```
//javafx-sdk-17.0.6下载地址:https://download2.gluonhq.com/openjfx/17.0.6/openjfx-17.0.6_windows-x64_bin-sdk.zip

--module-path "javafx-sdk-17.0.6/lib" --add-modules javafx.controls,javafx.fxml
```