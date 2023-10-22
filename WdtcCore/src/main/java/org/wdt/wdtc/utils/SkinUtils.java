package org.wdt.wdtc.utils;

import lombok.Setter;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.FilenameUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.manger.FileManger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SkinUtils {
    private File SkinFile;
    private String UserName;
    @Setter
    private InputStream UserSkinInput;

    public SkinUtils(File skinFile) {
        SkinFile = skinFile;
    }

    public SkinUtils(String userName) {
        UserName = userName;
    }

    public File writeSkinHead() throws IOException {
        BufferedImage image = ImageIO.read(UserSkinInput != null ? UserSkinInput : FileUtils.newInputStream(SkinFile));
        String extension = FilenameUtils.getExtension(SkinFile.getName());
        BufferedImage image1 = image.getSubimage(8, 8, 8, 8);
        File file = new File(FileManger.getUserAsste(), StringUtils.cleanStrInString(SkinFile.getName(),
                "." + extension) + "-head." + extension);
        FileUtils.touch(file);
        ImageIO.write(image1, extension, FileUtils.newOutputStream(file));
        return file;
    }

    public File getSkinFile() {
        return new File(FileManger.getUserAsste(), UserName + ".png");
    }

    public void copySkinFile() throws IOException {
        IOUtils.copy(UserSkinInput, FileUtils.newOutputStream(getSkinFile()));
    }
}
