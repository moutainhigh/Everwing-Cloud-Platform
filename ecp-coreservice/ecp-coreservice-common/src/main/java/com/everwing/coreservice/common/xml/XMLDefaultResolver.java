package com.everwing.coreservice.common.xml;/**
 * Created by wust on 2018/6/27.
 */

import com.everwing.coreservice.common.exception.ECPBusinessException;
import org.springframework.util.ResourceUtils;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

/**
 *
 * Function:
 * Reason:
 * Date:2018/6/27
 * @author wusongti@lii.com.cn
 */
public abstract class XMLDefaultResolver implements XMLAbstractResolver{
    @Override
    public void validateXML(String xsdPath, String xmlPath) throws ECPBusinessException {
        try{
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

            File schemaFile = ResourceUtils.getFile("classpath:" + xsdPath);

            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();

            File sourceFile = ResourceUtils.getFile("classpath:" + xmlPath);
            Source source = new StreamSource(sourceFile);
            validator.validate(source);
        } catch (Exception e) {
            throw new ECPBusinessException("["+xmlPath+"]验证失败。"+e.toString());
        }
    }
}
