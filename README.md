# Wdtc-demo😎

#### 一个简单到不能再简单的我的世界Java版启动器

---

## JDK:

### [zulu-20](https://www.azul.com/downloads/?version=java-20-sts&package=jdk-fx#zulu)

## IDE:

### IntelliJ IDEA

- Gradle

---

### 国内快速下载源:[BMCLAPI](https://bmclapidoc.bangbang93.com/)

---

### 构建方法:

- 等gradle自动下载好依赖后(个人技术能力有限,不太会自己造轮子,所以依赖比较多)
- 找到[WdtcMain](Wdtc/src/main/java/org/wdt/WdtcUI/WdtcMain.java)类文件里的main函数

```java
public static class WdtcMain {
    public static void main(String[] args) {
        //右键运行
        AppMain.main(args);
    }
}
```

---

## 许可证:

### GPL-2.0

---

### 其他:

#### JavaFx警报:
- 无伤大雅不用管它