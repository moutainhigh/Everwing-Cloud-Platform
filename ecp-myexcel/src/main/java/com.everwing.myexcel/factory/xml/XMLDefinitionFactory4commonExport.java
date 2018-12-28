package com.everwing.myexcel.factory.xml;/**
 * Created by wust on 2017/8/7.
 */

import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.definition.xml.XMLExcelDefinitionReader4commonExport;

/**
 *
 * Function:导出：基于XML的抽象工厂实现
 * Reason:该产品负责解析导出的xml配置
 * Date:2017/8/7
 * @author wusongti@lii.com.cn
 */
public class XMLDefinitionFactory4commonExport extends XMLDefinitionFactory {

    protected static final String EXPORT_XSD = "classpath:export.xsd";
    private String fullXmlName;
    public XMLDefinitionFactory4commonExport(String fullXmlName) {
        this.fullXmlName = fullXmlName;
    }

    @Override
    public ExcelDefinitionReader createExcelDefinitionReader() {
        ExcelDefinitionReader definitionReader = new XMLExcelDefinitionReader4commonExport(EXPORT_XSD,fullXmlName);
        return definitionReader;
    }
}
