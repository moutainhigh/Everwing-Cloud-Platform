package com.everwing.myexcel.definition.xml;/**
 * Created by wust on 2018/1/15.
 */

import com.everwing.myexcel.ConfigDefinitionBean;
import com.everwing.myexcel.MyEnum;
import com.everwing.myexcel.exception.MyExcelException;
import com.everwing.myexcel.xmlobject.complex.*;
import org.apache.commons.lang3.StringUtils;
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
 * Function:基于XML：复杂导出配置注册
 * Reason:
 * Date:2018/1/15
 * @author wusongti@lii.com.cn
 */
public class XMLExcelDefinitionReader4complexExport extends XMLExcelDefinitionReader {
    public XMLExcelDefinitionReader4complexExport(String xsdPath, String xmlPath) {
        super(xsdPath, xmlPath);
    }

    @Override
    public ConfigDefinitionBean registerConfigDefinitions() {
        ConfigDefinitionBean configDefinitionBean = ConfigDefinitionBeanFactory4complex.getInstance().getExportRegisterConfigDefinitions(super.xmlPath,this);
        return configDefinitionBean;
    }

    public void doRegisterConfigDefinitions(InputStream inputStream, com.everwing.myexcel.xmlobject.complex.XMLExcel4Export xmlExcel4Export) throws MyExcelException {
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

                    String attributeLabel = "";
                    if(e.hasAttribute(MyEnum.label.name())){
                        attributeLabel = e.getAttribute(MyEnum.label.name());
                    }

                    XMLSheet4Export xmlSheet = new XMLSheet4Export();
                    xmlSheet.setAttributeLabel(attributeLabel);
                    xmlSheet.setXmlComplexReport4Export(new XMLComplexReport4Export());

                    xmlExcel4Export.getXmlSheet4ExportList().add(xmlSheet);
                }else if(MyEnum.complexReport.name().equalsIgnoreCase(node.getNodeName())){// complexReport节点
                    XMLComplexReport4Export xmlComplexReport4Export = new XMLComplexReport4Export();
                    List<XMLRow4Export> xmlRow4Exports = new ArrayList<>();
                    xmlComplexReport4Export.setXmlRow4ExportList(xmlRow4Exports);

                    List<XMLSheet4Export> xmlSheet4ExportList = xmlExcel4Export.getXmlSheet4ExportList();
                    xmlSheet4ExportList.get(xmlSheet4ExportList.size() - 1).setXmlComplexReport4Export(xmlComplexReport4Export);
                }else if(MyEnum.row.name().equalsIgnoreCase(node.getNodeName())){// row节点
                    Element e = (Element)node;

                    String attributeIndex = "";
                    if(e.hasAttribute(MyEnum.index.name())){
                        attributeIndex = e.getAttribute(MyEnum.index.name());
                    }

                    XMLRow4Export xmlRow4Export = new XMLRow4Export();
                    List<XMLCell4Export> xmlCell4Exports = new ArrayList<>();
                    xmlRow4Export.setXmlCell4ExportList(xmlCell4Exports);
                    xmlRow4Export.setAttributeIndex(attributeIndex);

                    List<XMLSheet4Export> xmlSheet4ExportList = xmlExcel4Export.getXmlSheet4ExportList();
                    xmlSheet4ExportList.get(xmlSheet4ExportList.size() - 1).getXmlComplexReport4Export().getXmlRow4ExportList().add(xmlRow4Export);
                }else if(MyEnum.cell.name().equalsIgnoreCase(node.getNodeName())){// cell节点
                    Element e = (Element)node;
                    String cellText = e.getTextContent();
                    String attributeColumn = "";
                    String attributeRowspan = "";
                    String attributeColspan = "";

                    if(e.hasAttribute(MyEnum.column.name())){
                        attributeColumn = e.getAttribute(MyEnum.column.name());
                    }
                    if(e.hasAttribute(MyEnum.rowspan.name())){
                        attributeRowspan = e.getAttribute(MyEnum.rowspan.name());
                        if(StringUtils.isNotBlank(attributeRowspan)){
                            Integer.parseInt(attributeRowspan);
                        }
                    }
                    if(e.hasAttribute(MyEnum.colspan.name())){
                        attributeColspan = e.getAttribute(MyEnum.colspan.name());
                    }

                    XMLCell4Export xmlCell4Export = new XMLCell4Export();
                    xmlCell4Export.setAttributeColumn(attributeColumn);
                    xmlCell4Export.setRowspan(attributeRowspan);
                    xmlCell4Export.setColspan(attributeColspan);
                    xmlCell4Export.setCellText(cellText);

                    List<XMLSheet4Export> xmlSheet4ExportList = xmlExcel4Export.getXmlSheet4ExportList();
                    List<XMLRow4Export> xmlRow4ExportList = xmlSheet4ExportList.get(xmlSheet4ExportList.size() - 1).getXmlComplexReport4Export().getXmlRow4ExportList();

                    xmlRow4ExportList.get(xmlRow4ExportList.size() - 1).getXmlCell4ExportList().add(xmlCell4Export);
                }
                doParseXML4Export((Element) node,xmlExcel4Export);
            }
        }
    }
}
