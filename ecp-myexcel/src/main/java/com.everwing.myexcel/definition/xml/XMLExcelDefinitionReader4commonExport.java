package com.everwing.myexcel.definition.xml;/**
 * Created by wust on 2017/8/7.
 */

import com.everwing.myexcel.ConfigDefinitionBean;
import com.everwing.myexcel.MyEnum;
import com.everwing.myexcel.exception.MyExcelException;
import com.everwing.myexcel.xmlobject.common.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Function:基于XML：普通导出配置注册
 * Reason:
 * Date:2017/8/7
 * @author wusongti@lii.com.cn
 */
public class XMLExcelDefinitionReader4commonExport extends XMLExcelDefinitionReader {

    public XMLExcelDefinitionReader4commonExport(String xsdPath, String xmlPath) {
        super(xsdPath, xmlPath);
    }

    @Override
    public ConfigDefinitionBean registerConfigDefinitions() {
        ConfigDefinitionBean configDefinitionBean = ConfigDefinitionBeanFactory4common.getInstance().getExportRegisterConfigDefinitions(super.xmlPath,this);
        return configDefinitionBean;
    }

    public void doRegisterConfigDefinitions(InputStream inputStream,XMLExcel4Export xmlExcel4Export) throws MyExcelException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputStream);
            inputStream.close();

            Element root = doc.getDocumentElement();
            doParseXML4Export(root,xmlExcel4Export);
        } catch (ParserConfigurationException e) {
            throw new MyExcelException(e);
        } catch (SAXException e) {
            throw new MyExcelException(e);
        } catch (IOException e) {
            throw new MyExcelException(e);
        }
    }


    private void doParseXML4Export(Element element,XMLExcel4Export xmlExcel4Export) {
        if(MyEnum.excel.name().equalsIgnoreCase(element.getNodeName())) {// excel
            String attributeId = element.getAttribute(MyEnum.id.name());

            xmlExcel4Export.setXmlSheet4ExportList(new ArrayList<XMLSheet4Export>());

            xmlExcel4Export.setAttributeId(attributeId);
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            short nodeType = node.getNodeType();
            if (nodeType == Node.ELEMENT_NODE) {
                if(MyEnum.sheet.name().equalsIgnoreCase(node.getNodeName())){// sheet节点
                    Element e = (Element)node;

                    String attributeValue = "";
                    if(e.hasAttribute(MyEnum.label.name())){
                        attributeValue = e.getAttribute(MyEnum.label.name());
                    }

                    XMLSheet4Export xmlSheet = new XMLSheet4Export();

                    xmlSheet.setAttributeLabel(attributeValue);

                    xmlSheet.setXmlList4Export(new XMLList4Export());

                    xmlExcel4Export.getXmlSheet4ExportList().add(xmlSheet);
                }else if(MyEnum.list.name().equalsIgnoreCase(node.getNodeName())){// list节点
                    String attributeValue = "";

                    Element e = (Element)node;
                    if(e.hasAttribute(MyEnum.label.name())){
                        attributeValue = e.getAttribute(MyEnum.label.name());
                    }

                    XMLList4Export xmlList = new XMLList4Export();

                    xmlList.setAttributeLabel(attributeValue);

                    xmlList.setElementSql(new XMLSQL4Export());

                    xmlList.setElementFields(new ArrayList<XMLField4Export>());

                    List<XMLSheet4Export> xmlSheet4ExportList = xmlExcel4Export.getXmlSheet4ExportList();
                    xmlSheet4ExportList.get(xmlSheet4ExportList.size() - 1).setXmlList4Export(xmlList);
                }else if(MyEnum.sql.name().equalsIgnoreCase(node.getNodeName())){// sql节点
                    Element e = (Element)node;

                    String attributeId = "";
                    if(e.hasAttribute(MyEnum.id.name())){
                        attributeId = e.getAttribute(MyEnum.id.name());
                    }

                    String sqlText = e.getTextContent();

                    XMLSQL4Export xmlsql4Export = new XMLSQL4Export();
                    xmlsql4Export.setAttributeId(attributeId);
                    xmlsql4Export.setSqlText(sqlText);

                    List<XMLSheet4Export> xmlSheet4ExportList = xmlExcel4Export.getXmlSheet4ExportList();
                    xmlSheet4ExportList.get(xmlSheet4ExportList.size() - 1).getXmlList4Export().setElementSql(xmlsql4Export);

                }else if(MyEnum.field.name().equalsIgnoreCase(node.getNodeName())){// field节点

                    Element e = (Element)node;

                    String attributeLabel = "";
                    String attributeColumn = "";
                    String attributeType = "";
                    String attributeFormat = "";
                    String attributeLookupCode = "";
                    String attributeParentCode = "";

                    if(e.hasAttribute(MyEnum.label.name())){
                        attributeLabel = e.getAttribute(MyEnum.label.name());
                    }
                    if(e.hasAttribute(MyEnum.column.name())){
                        attributeColumn = e.getAttribute(MyEnum.column.name());
                    }
                    if(e.hasAttribute(MyEnum.type.name())){
                        attributeType = e.getAttribute(MyEnum.type.name());
                    }
                    if(e.hasAttribute(MyEnum.format.name())){
                        attributeFormat = e.getAttribute(MyEnum.format.name());
                    }
                    if(e.hasAttribute(MyEnum.lookupCode.name())){
                        attributeLookupCode = e.getAttribute(MyEnum.lookupCode.name());
                    }
                    if(e.hasAttribute(MyEnum.parentCode.name())){
                        attributeParentCode = e.getAttribute(MyEnum.parentCode.name());
                    }

                    XMLField4Export xmlField = new XMLField4Export();

                    xmlField.setAttributeLabel(attributeLabel);

                    xmlField.setAttributeColumn(attributeColumn);

                    xmlField.setAttributeType(attributeType);

                    xmlField.setAttributeFormat(attributeFormat);

                    xmlField.setAttributeLookupCode(attributeLookupCode);

                    xmlField.setAttributeParentCode(attributeParentCode);

                    List<XMLSheet4Export> xmlSheet4ExportList = xmlExcel4Export.getXmlSheet4ExportList();
                    xmlSheet4ExportList.get(xmlSheet4ExportList.size() - 1).getXmlList4Export().getElementFields().add(xmlField);
                }

                doParseXML4Export((Element) node,xmlExcel4Export);
            }
        }
    }
}
