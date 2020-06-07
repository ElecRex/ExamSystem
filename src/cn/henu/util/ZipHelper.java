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
     * 将学生上传的答案按照学号文件夹分类压缩成zip
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
                File zipFile = new File(zipFilePath + "/" + filename + ".zip");
                if(zipFile.exists())
                    System.out.println("目录下已存在该文件：" + zipFile.getName());
                else{
                    File[] sourceFiles = sourceFile.listFiles();
                    if(null == sourceFiles || sourceFiles.length<1){
                        System.out.println("待压缩的文件下没有文件，不需要压缩！");
                    }else{
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024*10];
                        for(int i=0;i<sourceFiles.length;i++){

                            if(sourceFiles[i].isDirectory()){ //若为目录
                                File[] fl = sourceFiles[i].listFiles(); //该学号子目录下的试卷集合
                                System.out.println("------该学号子目录为："+sourceFiles[i].getName());
                                for(int j=0; j < fl.length; j++){
                                    //创建zip实体类，按学号将子目录添加进压缩包
                                    ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName() + "/"
                                                                    +fl[j].getName());
                                    zos.putNextEntry(zipEntry);
                                    //读取待压缩的文件并写进压缩包里
                                    fis = new FileInputStream(fl[j]);
                                }
                            } else { //若为文件
                                //创建zip实体类，并添加进压缩包
                                ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                                zos.putNextEntry(zipEntry);
                                //读取待压缩的文件并写进压缩包里
                                fis = new FileInputStream(sourceFiles[i]);
                            }
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
