package com.motorcli.springboot.common.utils;

import com.motorcli.springboot.common.exceptions.FileHandlerException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 文件工具
 */
@Slf4j
public class FileUtils {

    /**
     * 获取扩展名
     * @param file 文件
     * @return 扩转名 （带点）
     */
    public static String getExtension(File file) {
        String extension = "."
                + file.getName().substring(file.getName().lastIndexOf(".") + 1);
        return extension.toLowerCase();
    }

    /**
     * 获取扩展名
     * @param fileName 文件名
     * @return 扩转名 （带点）
     */
    public static String getExtension(String fileName) {
        String extension = "."
                + fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.toLowerCase();
    }

    public static String getFileName(File file) {
        return getFileName(file.getName());
    }

    /**
     * 获取文件名， 不包含扩展名
     * @param fileName 文件名
     * @return 文件名称
     */
    public static String getFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 新建文件
     *
     * @param path 文件路径
     * @param name 文件名称
     * @return 文件对象
     */
    public static File newFile(String path, String name) throws FileHandlerException {
        String destFileName = path + name;
        File file = new File(destFileName);
        if(file.exists()) {
            return file;
        }
        if (destFileName.endsWith(File.separator)) {
            throw new FileHandlerException("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
        }
        //判断目标文件所在的目录是否存在
        if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            if(!file.getParentFile().mkdirs()) {
                throw new FileHandlerException("创建目标文件所在目录失败！path : [" + file.getParentFile().getAbsolutePath() + "]");
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {
                return file;
            } else {
                throw new FileHandlerException("创建单个文件" + destFileName + "失败");
            }
        } catch (IOException e) {
            throw new FileHandlerException("文件操作时发生异常", e);
        }
    }

    /**
     * 新建文件
     *
     * @param path 文件绝对路径
     * @param name 文件名
     * @param b 文件字节流
     * @return 文件
     */
    public static File newFile(String path, String name, byte[] b) throws FileHandlerException {
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(path);
            if (!ret.exists()) {
                ret.mkdirs();// 目录不存在的情况下，创建目录。
            }
            if (FileUtils.findFile(path, name) != null) {
                FileUtils.deleteFile(path, name);
            }

            ret = new File(path + File.separator + name);

            FileOutputStream fileOutputStream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fileOutputStream);
            stream.write(b);
        } catch (Exception e) {
            throw new FileHandlerException("文件操作时发生异常", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new FileHandlerException("关闭文件时发生异常", e);
                }
            }
        }
        return ret;
    }

    /**
     * 查找文件
     *
     * @param path 文件绝对路径
     * @param name 文件名
     * @return 文件对象
     */
    public static File findFile(String path, String name) {
        boolean flag = false;
        File file = new File(path + File.separator + name);
        if (file.exists()) {
            flag = true;
        }
        return flag ? file : null;
    }

    /**
     * 查找文件
     *
     * @param fileName 文件绝对路径
     * @return 文件对象
     */
    public static File findFile(String fileName) {
        boolean flag = false;
        File file = new File(fileName);
        if (file.exists()) {
            flag = true;
        }
        return flag ? file : null;
    }

    /**
     * 删除文件
     *
     * @param path 文件绝对路径
     * @param name 文件名
     * @return <code>true</code> 成功
     *         <br>
     *         <code>false</code> 失败
     */
    public static boolean deleteFile(String path, String name) {
        boolean flag = false;
        File file = new File(path + File.separator + name);
        if (file.exists()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 删除文件
     * @param file 文件对象
     * @return <code>true</code> 成功
     *         <br>
     *         <code>false</code> 失败
     */
    public static boolean deleteFile(File file) {
        return file.delete();
    }

    /**
     * 删除文件夹及其子目录
     *
     * @param file 文件夹对象
     * @return <code>true</code> 成功
     *         <br>
     *         <code>false</code> 失败
     */
    public static boolean deleteDir(File file) {
        File files[] = file.listFiles();
        if (files != null) {
            for (File tempFile : files) {
                deleteDir(tempFile);
            }
        }
        return file.delete();
    }

    /**
     * 删除文件夹及其子目录
     *
     * @param path 文件夹路径
     * @return <code>true</code> 成功
     *         <br>
     *         <code>false</code> 失败
     */
    public static boolean deleteDir(String path) {
        File file = findFile(path);
        return deleteDir(file);
    }

    /**
     * 拷贝文件
     *
     * @param src 源文件
     * @param dis 目标文件
     */
    public static void copy(File src, File dis) throws FileHandlerException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src));
            out = new BufferedOutputStream(new FileOutputStream(dis),
                    in.available());
            byte[] buffer = new byte[in.available()];
            while (in.read(buffer) > 0) {
                out.write(buffer);
            }
            in.close();
            out.close();
        } catch (IOException ex) {
            throw new FileHandlerException("复制文件失败", ex);
        }
    }

    /**
     * 拷贝文件
     *
     * @param input 输入流
     * @param dis 目标文件
     */
    public static void copy(InputStream input, File dis) throws FileHandlerException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(input);
            out = new BufferedOutputStream(new FileOutputStream(dis),
                    in.available());
            byte[] buffer = new byte[in.available()];
            while (in.read(buffer) > 0) {
                out.write(buffer);
            }
            in.close();
            out.close();
        } catch (IOException ex) {
            throw new FileHandlerException("复制文件失败", ex);
        }
    }

    /**
     * 文件名转码
     * 由 ISO-8859-1 转到 UTF-8
     * @param chi 文件名
     * @return 转码后的字符串
     */
    public static String trans(String chi) {
        String result = null;
        byte temp[];
        try {
            temp = chi.getBytes("ISO-8859-1");
            result = new String(temp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("转码错误", e);
        }
        return result;
    }

    /**
     * 文件名转码
     * 由 GB2312 转到 ISO-8859-1
     * @param src 文件名
     * @return 转码后的字符串
     */
    public static String change(String src) {
        if(src == null) {
            return null;
        }
        try {
            return new String(src.getBytes("GB2312"), "iso8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error("转码错误", e);
        }
        return null;
    }
}
