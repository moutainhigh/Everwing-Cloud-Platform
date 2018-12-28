package com.everwing.coreservice.common.platform.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * byte[] 转multipartFile
 *
 * @author DELL shiny
 * @create 2017/8/7
 */
public class Base64DecodedMultipartFile implements MultipartFile {

    private final byte[] imgContent;

    public Base64DecodedMultipartFile(byte[] imgContent){
        this.imgContent=imgContent;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        new FileOutputStream(file).write(imgContent);
    }
}
