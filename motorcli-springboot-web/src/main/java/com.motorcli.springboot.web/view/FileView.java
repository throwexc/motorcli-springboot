package com.motorcli.springboot.web.view;

import com.motorcli.springboot.common.utils.FileUtils;
import org.springframework.web.servlet.View;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

public class FileView implements View {

    private int bufferSize = 2048;

    private File file;

    private String fileName;

    private InputStream inputStream;

    private String contentType;

    public FileView(File file) {
        this.file = file;
        this.contentType = new MimetypesFileTypeMap().getContentType(this.file);
    }

    public FileView(InputStream inputStream, String fileName) {
        this.fileName = fileName;
        this.inputStream = inputStream;
        this.contentType = new MimetypesFileTypeMap().getContentType(this.fileName);
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void render(Map<String, ?> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ServletOutputStream out = httpServletResponse.getOutputStream();

        InputStream is;

        if(this.file != null) {
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + FileUtils.change(this.file.getName()));
            httpServletResponse.setContentType(getContentType());
            is = new FileInputStream(this.file);
        } else {
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + FileUtils.change(this.fileName));
            httpServletResponse.setContentType(getContentType());
            is = this.inputStream;
        }


        copyStream(is,out,true);
        is.close();
        out.close();
    }

    /**
     * 复制流 到 前端浏览器
     * @param source 源文件输入流
     * @param dest 输出流
     * @param flush
     * @return
     */
    private final long copyStream(InputStream source, OutputStream dest, boolean flush){
        int bytes;
        long total=0l;
        byte [] buffer=new byte[bufferSize];
        try {
            while((bytes=source.read(buffer))!=-1){
                if(bytes==0){
                    bytes=source.read();
                    if(bytes<0)
                        break;
                    dest.write(bytes);
                    if(flush)
                        dest.flush();
                    total+=bytes;
                }
                dest.write(buffer,0,bytes);
                total+=bytes;
            }

            if(flush) {
                dest.flush();
            }

        } catch (IOException e) {
            throw new RuntimeException("IOException caught while copying.",e);
        }
        return total;
    }
}
