package com.everwing.myexcel.definition.xml;/**
 * Created by wust on 2017/5/8.
 */

import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.exception.MyExcelException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 *
 * Function:基于XML的配置注册抽象类
 * Reason:
 * Date:2017/5/8
 * @author wusongti@lii.com.cn
 */
public abstract class XMLExcelDefinitionReader implements ExcelDefinitionReader {
    static Logger logger = LogManager.getLogger(XMLExcelDefinitionReader.class);

    protected String xsdPath;

    protected String xmlPath;


    public XMLExcelDefinitionReader(String xsdPath, String xmlPath){
        this.xsdPath = xsdPath;
        this.xmlPath = xmlPath;
        validateXML();
    }



    /**
     * 验证XML是否按照规范来编写
     * @throws Exception
     */
    protected void validateXML() throws MyExcelException {
        try{
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            URL xsdURL = ResourceUtils.getURL(xsdPath);
            Schema schema = schemaFactory.newSchema(xsdURL);
            Validator validator = schema.newValidator();

            File sourceFile = ResourceUtils.getFile("classpath:" + xmlPath);
            Source source = new StreamSource(sourceFile);
            validator.validate(source);
        } catch (FileNotFoundException e) {
            logger.error(e);
            throw new MyExcelException("找不到[" + xsdPath + "]或[" + xmlPath + "]文件",e);
        } catch (SAXException e) {
            logger.error(e);
            throw new MyExcelException("XML[" + xmlPath + "]不按照XSD[" + xsdPath + "]规范来编写",e);
        } catch (IOException e) {
            logger.error(e);
            throw new MyExcelException("XML[" + xmlPath + "]不按照XSD[" + xsdPath + "]规范来编写",e);
        }
    }
}
