package cn.henu.util;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZipHelper {
    /**
     * 将学生上传的答案压缩成zip
     * @param sourceFilePath
     * @param zipFilePath
     * @param filename
     * @return
     */
    public static boolean toZip(String sourceFilePath, String zipFilePath, String filename){

        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if(sourceFile.exists() == false){
            System.out.println("待压缩的目录不存在！");
        }else{
            try {
                File zipFile = new File(zipFilePath+"/" + filename + ".zip");
                if(zipFile.exists())
                    System.out.println("目录下已存在该文件！");
                else{
                    File[] sourceFiles = sourceFile.listFiles();
                    if(null == sourceFiles || sourceFiles.length<1){
                        System.out.println("待压缩的文件下没有文件，不需要压缩！");
                    }else{
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024*10];
                        for(int i=0;i<sourceFiles.length;i++){
                            //创建zip实体类，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            //读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis,1024*10);
                            int read = 0;
                            while((read = bis.read(bufs,0,1024*10))!= -1)
                                zos.write(bufs, 0, read);
                        }
                        flag = true;
                    }

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }finally {
                try {
                    if(bis != null) bis.close();
                    if(zos != null) zos.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                    throw new RuntimeException(e2);
                }
            }
        }

        return flag;
    }
}
