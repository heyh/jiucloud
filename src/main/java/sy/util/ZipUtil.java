package sy.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * Created by heyh on 2016/10/22.
 */
public class ZipUtil {

    private static String REMOTE_BASE_URL = "http://120.55.189.118:8080/project/";

    /**
     * 解压（带密码）
     * @param zipFile
     * @param dest
     * @param passwd
     * @throws ZipException
     * @throws IOException
     */
    public static void unzip(File zipFile, String dest, String passwd){
        ZipFile zFile = null;  // 首先创建ZipFile指向磁盘上的.zip文件
        try {
            zFile = new ZipFile(zipFile);
            zFile.setFileNameCharset("GBK");       // 设置文件名编码，在GBK系统中需要设置
            if (!zFile.isValidZipFile()) {   // 验证.zip文件是否合法，包括文件是否存在、是否为zip文件、是否被损坏等
                throw new ZipException("压缩文件不合法,可能被损坏.");
            }
            File destDir = new File(dest);     // 解压目录
            if (destDir.isDirectory() && !destDir.exists()) {
                destDir.mkdir();
            }
            if (zFile.isEncrypted()) {
                zFile.setPassword(passwd.toCharArray());  // 设置密码
            }
            zFile.extractAll(dest);      // 将文件抽出到解压目录(解压)
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载远程服务器文件
     * @param filePath
     * @param fileName
     * @return
     */
    public static String downloadRemoteFile(String remoteBaseUrl, String baseDest, String filePath, String fileName){
        String _filePath = "";
        URL url= null;
        try {
            url = new URL(remoteBaseUrl + filePath + "/" + fileName);
            InputStream is=url.openStream();
            createDir(baseDest + filePath);
            _filePath = baseDest + filePath + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
            OutputStream os=new FileOutputStream(_filePath);
            byte bf[]=new byte[1024];
            int length=0;
            while((length=is.read(bf, 0, 1024))!=-1){
                os.write(bf, 0, length);
            }
            is.close();
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _filePath;
    }

    /**
     * 创建目录
     * @param dest
     * @return
     */
    public static boolean createDir(String dest) {
        File dir = new File(dest);
        if (dir.exists()) {
            return false;
        }
        if (!dest.endsWith(File.separator)) {
            dest = dest + File.separator;
        }
        // 创建单个目录
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
//        String remoteFilePath = "6091/945701392531718344.jsw";
//        try {
//            downloadRemoteFile(remoteFilePath);
//            File zipFile = new File("6091/945701392531718344.zip");
//            unzip(zipFile, "6091", "njrxy_+7804");
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }
}
