package com.everwing.coreservice.wy.core.utils;

import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.utils.SysConfig;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

/**
 * @TODO PDFUtils
 * @author DELL
 *
 */
@Component
public class PDFUtils {

	private static final Logger logger = LogManager.getLogger(PDFUtils.class);
	
	private static final PDFUtils utils = new PDFUtils();
	
	private PDFUtils(){}
	
	private static SysConfig sysConfig;
	
	public static PDFUtils getInstance(){
		if(null == sysConfig){
			sysConfig = (SysConfig)SpringContextHolder.getBean("sysConfig");
		}
		return utils;
	}
	
	
	public void html2PDF(String path){
		
		if(null == path){
			logger.error("html2PDF : 传入数据流为空.");
			return;
		}
		FileOutputStream fos = null;
        try {
        	fos = new FileOutputStream(path + ".pdf");
        	ITextRenderer renderer = new ITextRenderer();   
        	
        	File file = new File(path + ".html");
        	String url = file.toURI().toURL().toString();
        	renderer.setDocument(url);     
        	
        	// 解决中文支持问题       
        	ITextFontResolver fontResolver = renderer.getFontResolver();  
			fontResolver.addFont(sysConfig.getFontPath() + "SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			
			//解决图片的相对路径问题  
			renderer.getSharedContext().setBaseURL(sysConfig.getBillImgPath());  
			renderer.layout();      
			renderer.createPDF(fos); 
			fos.flush();
			fos.close();
			fos = null;
		} catch (DocumentException | IOException e) {
			logger.error("html2PDF 获取字体/IO出现异常 . 异常数据: {}", e.getMessage());
		} catch (Exception e1){
			logger.error("html2PDF 出现异常 . 异常数据: {}", e1.getMessage());
		}finally{
			if(null != fos){
				try {
					fos.flush();  
					fos.close();
				} catch (IOException e) {
					logger.error("html2PDF : 文件输出流关闭异常. ");
					e.printStackTrace();
				} finally{
					fos = null;
				}
			}
		}
	}

	/**
	 * @TODO pdf合并成大的pdf
	 * @param inputPath
	 * @param newPath
	 * @param fileName
	 */
	public void combinePdfs(String inputPath, String newPath, String fileName) {
		File inputDir = new File(inputPath);
		
		if(!inputDir.exists()){
			logger.error("file2Zip : 未找到可读文件夹. 读取路径: {}. ",inputPath);
			return;
		}
		
		File outputDir = new File(newPath);
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
//		String pdfFileName = fileName.replaceAll("zip", "pdf");
		File bigPdfFile = new File(newPath + File.separator + fileName);
		try {
			//准备合并成一个pdf 然后再进行单个pdf打包
			for(int i = 0 ; i < pdfFiles.length ; i++){
				File pdfFile = pdfFiles[i];
				if(pdfFile.getName().endsWith(".html")){
					pdfFile.delete();
					continue;
				}
				
				PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
				int n = reader.getNumberOfPages();
				if(null == doc){
					doc = new Document(new PdfReader(reader).getPageSize(1));
					copy = new PdfCopy(doc, new FileOutputStream(bigPdfFile));
					doc.open();
				}
				
				for (int j = 1; j <= n; j++) {
					doc.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
				
				if(pdfFile != null && pdfFile.exists()){
					FileUtils.forceDelete(pdfFile);
				}
			}
			
			if(inputDir != null && inputDir.exists()){
				FileUtils.forceDelete(inputDir);
			}
			
		} catch (IOException | DocumentException e1) {
			e1.printStackTrace();
		} finally{
			if(null != doc){
				doc.close();
			}
		}
	}
}
