package com.everwing.server.wy.utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * jasperReport工具类
 *
 * @author DELL shiny
 * @create 2018/4/23
 */
public class ReportUtils {

    private static final Logger logger= LogManager.getLogger(ReportUtils.class);

    private ReportUtils(){}


    public static void generatePDF(String jasperFilePath,String pdfFilePath,Collection fileData){
        try {
            logger.info("generatePDF 参数:jfp{},pfp:{}",jasperFilePath,pdfFilePath);
            JasperReport jasperReport=null;
            JasperPrint print=null;
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(fileData);
            if(jasperFilePath.endsWith(".jrxml")){
                jasperReport=JasperCompileManager.compileReport(jasperFilePath);
                print=JasperFillManager.fillReport(jasperReport,setParameter(jasperFilePath),beanColDataSource);
            }else {
                InputStream inputStream=new FileInputStream(jasperFilePath);
                print=JasperFillManager.fillReport(inputStream,setParameter(jasperFilePath),beanColDataSource);
            }
            if (pdfFilePath != null) {
                File file=new File(pdfFilePath);
                if(!file.getParentFile().exists()) {
                    if(!file.getParentFile().mkdirs()) {
                        logger.error("创建目标文件所在目录失败！");
                    }
                }
                if(!file.exists()){
                    file.createNewFile();
                }
                JasperExportManager.exportReportToPdfFile(print, pdfFilePath);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("jasper 导出PDF 异常:{}",e.getMessage());
            return;
        }
    }

    public static byte[] generatePDF(String jasperFilePath,Collection data){
        try {
            JasperReport jasperReport=null;
            JasperPrint print=null;
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(data);
            if(jasperFilePath.endsWith(".jrxml")){
                jasperReport=JasperCompileManager.compileReport(jasperFilePath);
                print=JasperFillManager.fillReport(jasperReport,setParameter(jasperFilePath),beanColDataSource);
            }else {
                InputStream inputStream=new FileInputStream(jasperFilePath);
                print=JasperFillManager.fillReport(inputStream,setParameter(jasperFilePath),beanColDataSource);
            }
            return JasperExportManager.exportReportToPdf(print);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("jasper 导出PDF 异常:{}",e.getMessage());
            return null;
        }
    }

    private static Map<String,Object> setParameter(String jasperFilePath){
        Map<String,Object> parameters=new HashMap<>(3);
        String imagePath=jasperFilePath.substring(0,jasperFilePath.lastIndexOf(File.separator))+File.separator+"img"+File.separator;
        String qrCode="erweim.png";
        String logo="logo.jpg";
        parameters.put("image_path",imagePath);
        parameters.put("qrCode",qrCode);
        parameters.put("logo",logo);
        return parameters;
    }
}
