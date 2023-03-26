import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件夹打压缩包
 *
 * @author ZYGisComputer
 */
public final class ZipObjects {
    public static void main(String[] args) throws Exception {
        compressedFile(".minecraft/assets/objects", "WdtcCore/ResourceFile/Download/objects.jar");
    }

    /**
     * @ClassName ZipOutputStreamDemo
     * @Author houyuanbo
     * @Date 2021/9/28 15:14
     * @Description TODO
     * @Version
     **/

    /**
     * 将指定文件/文件夹压缩成zip、rar压缩文件
     */


    /**
     * @param resourcesPath 资源文件夹
     * @param targetPath    目的压缩文件保存路径
     * @return void
     * @throws Exception
     * @desc 将源文件/文件夹生成指定格式的压缩文件,格式zip
     */
    public static void compressedFile(String resourcesPath, String targetPath) throws Exception {
        File resourcesFile = new File(resourcesPath);
        File targetFile = new File(targetPath);

        if (!targetFile.exists()) {
            if (targetFile.isDirectory()) {
                System.out.println(true);
                targetFile.mkdirs();
            }
        }

        FileOutputStream outputStream = new FileOutputStream(targetFile);
        CheckedOutputStream cos = new CheckedOutputStream(outputStream, new CRC32());
        ZipOutputStream out = new ZipOutputStream(cos);
        createCompressedFile(out, resourcesFile, "");
        out.close();

    }

    /**
     * @param out  输出流
     * @param file 目标文件
     * @return void
     * @throws Exception
     * @desc 生成压缩文件。
     * 如果是文件夹，则使用递归，进行文件遍历、压缩
     * 如果是文件，直接压缩
     */
    public static void createCompressedFile(ZipOutputStream out, File file, String dir) throws Exception {
        //如果当前的是文件夹，则进行进一步处理
        if (file.isDirectory()) {
            //得到文件列表信息
            File[] files = file.listFiles();
            //将文件夹添加到下一级打包目录
            out.putNextEntry(new ZipEntry(dir + "/"));

            dir = dir.length() == 0 ? "" : dir + "/";

            //循环将文件夹中的文件打包
            for (int i = 0; i < files.length; i++) {
                createCompressedFile(out, files[i], dir + files[i].getName());
            }
        } else {   //当前的是文件，打包处理
            //文件输入流
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(dir);
            out.putNextEntry(entry);
            // out.putNextEntry(new ZipEntry(dir));
            //进行写操作
            int j = 0;
            byte[] buffer = new byte[1024];
            while ((j = bis.read(buffer)) > 0) {
                out.write(buffer, 0, j);
            }
            //关闭输入流
            bis.close();
        }
    }
}
