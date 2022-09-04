package code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 根据配置文件中指定文件格式获取指定路径下的所有文件
 * @author fightingHawk Email: fighting.hawk@foxmail.com
 * @version 1.0.0
 */
public class GetFile {
    private int i = 1;
    private String saveDir;
    private String startDir;
    private String[] fileFormats;

    public GetFile() {
        getConfigFile();
        File fileStartDir = new File(this.startDir);
        File fileSaveDir = new File(this.saveDir);
        File[] files = fileStartDir.listFiles();
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getAllFile(file, fileSaveDir);
                }
            }
        }
    }

    /**
     * 获取配置文件信息
     */
    private void getConfigFile() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
            this.saveDir = (String) properties.get("saveDir");
            this.startDir = (String) properties.get("startDir");
            String saveFileFormat = (String) properties.get("saveFileFormat");
            this.fileFormats = saveFileFormat.split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取满足要求的所有文件
     * @param saveFile 也许需要被保存的文件
     * @param fileSaveDir 文件保存位置
     */
    private void getAllFile(File saveFile, File fileSaveDir) {
        File[] saveFiles = saveFile.listFiles();
        if (saveFiles != null) {
            for (File file : saveFiles) {
                if (file.isDirectory()) {
                    getAllFile(file, fileSaveDir);
                } else {
                    for (String fileFormat : this.fileFormats) {
                        if (file.getName().endsWith(fileFormat)) {
                            try {
                                FileInputStream fis = new FileInputStream(file);
                                // 以文件完整路径作为文件名
                                String saveName = i++ + "_" + file.getPath().replace('\\', '_').replace(':', '&');
                                FileOutputStream fos = new FileOutputStream(fileSaveDir.getAbsoluteFile() + "\\" + saveName);
                                byte[] bytes = new byte[1024];
                                int len;
                                while ((len = fis.read(bytes)) != -1) {
                                    fos.write(bytes, 0, len);
                                }
                                fis.close();
                                fos.close();
                                System.out.println(saveName + " 保存成功");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new GetFile();
    }
}
