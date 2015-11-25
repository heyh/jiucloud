package sy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class PDF2SWFUtil {
    /**
      * 利用SWFTools工具将pdf转换成swf，转换完后的swf文件与pdf同名
      * @param exePath 转换器安装路径
      * @param sourcePath PDF文件存放路径（包括文件名）
      * param outPath swf文件存放路径（不包括文件名）
       * @throws java.io.IOException
      */
    public static synchronized void pdf2swf(String exePath,String sourcePath, String outPath) throws IOException {

        //文件路径
         String filePath = sourcePath.substring(0, sourcePath.lastIndexOf("/"));
        //文件名，不带后缀
         String fileName = sourcePath.substring((filePath.length() + 1), sourcePath.lastIndexOf("."));
         // 目标路径不存在则建立目标路径   
         File dest = new File(outPath);  
         if (!dest.exists())  
             dest.mkdirs();  
   
         // 源文件不存在则返回   
         File source = new File(sourcePath);  
         if (!source.exists())  
             return;  
        Process pro = null;
        if (isWindowsSystem()) {
              //如果是windows系统
              //命令行命令
              String cmd = exePath + " \"" + sourcePath + "\" -o \"" + outPath + "/" + fileName + ".swf\"  -f -T 9";
              //Runtime执行后返回创建的进程对象
              pro = Runtime.getRuntime().exec(cmd);
        } else {
              //如果是linux系统,路径不能有空格，而且一定不能用双引号，否则无法创建进程
              String[] cmd = new String[3];
              cmd[0] = exePath;
              cmd[1] = sourcePath;
              cmd[2] = outPath + "/" + fileName + ".swf";
              //Runtime执行后返回创建的进程对象
              pro = Runtime.getRuntime().exec(cmd);

        }
        //非要读取一遍cmd的输出，要不不会flush生成文件（多线程）
        new DoOutput(pro.getInputStream()).start();
        new DoOutput(pro.getErrorStream()).start();
        try {
            //调用waitFor方法，是为了阻塞当前进程，直到cmd执行完
             pro.waitFor();
        } catch (InterruptedException e) {
           e.printStackTrace();
        }
    }
    /**
      * 判断是否是windows操作系统
      * @return
      */
    private static boolean isWindowsSystem() {
        String p = System.getProperty("os.name");
        return p.toLowerCase().indexOf("windows") >= 0 ? true : false;
    }
    /**
      * 多线程内部类
       * 读取转换时cmd进程的标准输出流和错误输出流，这样做是因为如果不读取流，进程将死锁
      */
    private static class DoOutput extends Thread {
        public InputStream is;
        //构造方法
         public DoOutput(InputStream is) {
            this.is = is;
        }
        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
            String str = null;
            try {
                //这里并没有对流的内容进行处理，只是读了一遍
                while ((str = br.readLine()) != null);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
      }
    }

    /**
      * 测试main方法
      * @param args
      */
    public static void main(String[] args) {
        //转换器安装路径
         String exePath = "c:/Program Files/SWFTools/pdf2swf.exe";
        try {
        	PDF2SWFUtil.pdf2swf(exePath,"Z:/Downloads/test.pdf","C:/gongju/apache-tomcat-6.0.44/webapps/jiucloud/upload/swfsource/16045/");
            //PDF2SWFUtil.pdf2swf(exePath,"Z:/Downloads/test.pdf","Z:/Downloads");
        } catch (IOException e) {
            System.err.println("转换出错！");
            e.printStackTrace();
        }
    }
}
