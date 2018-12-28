package com.everwing.coreservice.wy.core.utils;

import com.everwing.coreservice.common.utils.CommonUtils;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ZipUtils {

	private static final Logger logger = LogManager.getLogger(ZipUtils.class);

	private static final ZipUtils zipUtil = new ZipUtils();
	
	private ZipUtils(){}
	
	public static ZipUtils getInstance(){
		return zipUtil;
	}
	
	public void zipFiles(String inputPath , String outputPath , String fileName){
		
		File inputDir = new File(inputPath);
		
		if(!inputDir.exists()){
			logger.error("file2Zip : 未找到可读文件夹. 读取路径: {}. ",inputPath);
			return;
		}
		
		File outputDir = new File(outputPath);
		if(!outputDir.exists()){
			outputDir.mkdirs();
		}
		
		File[] pdfFiles = inputDir.listFiles();
		if(CommonUtils.isEmpty(pdfFiles)){
			logger.warn("file2Zip : 当前文件下未找到可进行转换的文件. 读取路径: {}. ",inputPath);
			return;
		}
		
		ZipOutputStream zos = null;
		BufferedInputStream bis = null;
		
		Document doc = null;
		PdfCopy copy = null;
		String pdfFileName = fileName.replaceAll("zip", "pdf");
		File bigPdfFile = new File(outputPath + File.separator + pdfFileName);
			
		try {	
			zos = new ZipOutputStream(new FileOutputStream(outputPath + File.separator + fileName));
				
			ZipEntry entry = new ZipEntry(bigPdfFile.getName());
			zos.putNextEntry(entry);
			bis = new BufferedInputStream(new FileInputStream(bigPdfFile));
			byte[] b = new byte[bis.available()];
			bis.read(b);
			zos.write(b);
			bis.close();
			b = null;
			bis = null;
			
			if(bigPdfFile != null && bigPdfFile.exists()){
				FileUtils.forceDelete(bigPdfFile);
			}
			inputDir.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != bis){
				try {
					bis.close();
				} catch (IOException e) {
					logger.error("file2Zip : pdf读取流关闭异常 . 异常: {}. ", e.getMessage());
				} finally{
					bis = null;
				}
			}
			if(null != zos){
				try {
					zos.flush();
					zos.close();
				} catch (IOException e) {
					logger.error("file2Zip : pdf写入到zip流关闭异常 . 异常: {}. ", e.getMessage());
				} finally{
					zos = null;
				}
			}
		}
	}
}
