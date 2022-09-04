package code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ���������ļ���ָ���ļ���ʽ��ȡָ��·���µ������ļ�
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
     * ��ȡ�����ļ���Ϣ
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
     * ��ȡ����Ҫ��������ļ�
     * @param saveFile Ҳ����Ҫ��������ļ�
     * @param fileSaveDir �ļ�����λ��
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
                                // ���ļ�����·����Ϊ�ļ���
                                String saveName = i++ + "_" + file.getPath().replace('\\', '_').replace(':', '&');
                                FileOutputStream fos = new FileOutputStream(fileSaveDir.getAbsoluteFile() + "\\" + saveName);
                                byte[] bytes = new byte[1024];
                                int len;
                                while ((len = fis.read(bytes)) != -1) {
                                    fos.write(bytes, 0, len);
                                }
                                fis.close();
                                fos.close();
                                System.out.println(saveName + " ����ɹ�");
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
