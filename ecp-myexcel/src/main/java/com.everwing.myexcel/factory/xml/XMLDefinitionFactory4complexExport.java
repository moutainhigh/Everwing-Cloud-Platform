package com.everwing.myexcel.factory.xml;/**
 * Created by wust on 2018/1/15.
 */

import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.definition.xml.XMLExcelDefinitionReader4complexExport;

/**
 *
 * Function:导出：基于XML的抽象工厂实现
 * Reason:该产品负责解析复杂报表导出的xml配置
 * Date:2018/1/15
 * @author wusongti@lii.com.cn
 */
public class XMLDefinitionFactory4complexExport extends XMLDefinitionFactory {
    protected static final String EXPORT_XSD = "classpath:complex_export.xsd";
    private String fullXmlName;
    public XMLDefinitionFactory4complexExport(String fullXmlName) {
        this.fullXmlName = fullXmlName;
    }

    @Override
    public ExcelDefinitionReader createExcelDefinitionReader() {
        ExcelDefinitionReader definitionReader = new XMLExcelDefinitionReader4complexExport(EXPORT_XSD,fullXmlName);
        return definitionReader;
    }
}
