package com.everwing.coreservice.common.xml.areas;/**
 * Created by wust on 2018/6/27.
 */

import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.system.areas.TSysAreasList;
import com.everwing.coreservice.common.xml.XMLDefaultResolver;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:区域XML数据解析器
 * Reason:
 * Date:2018/6/27
 * @author wusongti@lii.com.cn
 */
public class XMLAreasResolver extends XMLDefaultResolver {
    static Logger logger = LogManager.getLogger(XMLAreasResolver.class);

    /**
     * 元素名称
     */
    private static final String ELEMENT_RECORDS = "records";
    private static final String ELEMENT_RECORD = "record";
    private static final String ELEMENT_ATTRIBUTE_ID = "id";
    private static final String ELEMENT_ATTRIBUTE_AREA_NAME = "area_name";
    private static final String ELEMENT_ATTRIBUTE_PID = "pid";
    private static final String ELEMENT_ATTRIBUTE_SHORT_NAME = "short_name";
    private static final String ELEMENT_ATTRIBUTE_LEVEL = "level";
    private static final String ELEMENT_ATTRIBUTE_SORT = "sort";


    private Map<String,String> uniqueKeyChecking = new HashMap<>();
    private List<TSysAreasList> tSysAreasLists = new ArrayList<TSysAreasList>();

    private static  final String[] xmlNameList = {"t_sys_areas_level1.xml","t_sys_areas_level2.xml","t_sys_areas_level3.xml","t_sys_areas_level4.xml"};

    @Override
    public void parseXML() throws ECPBusinessException {
        String mainXSDPath = "initData" + File.separator + "areas" + File.separator + "t_sys_areas.xsd";
        for (String s : xmlNameList) {
            String mainXMLPath = "initData" + File.separator + "areas" + File.separator + s;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            //validateXML(mainXSDPath, mainXMLPath);

            try{
                DocumentBuilder db = dbf.newDocumentBuilder();
                org.w3c.dom.Document doc = db.parse(ResourceUtils.getFile("classpath:" + mainXMLPath));
                org.w3c.dom.Element element = doc.getDocumentElement();
                doParseXML(element);
            }catch (Exception e){
                throw new ECPBusinessException(e);
            }
        }
    }

    @Override
    public Map<String, List> getResult() {
        this.parseXML();
        Map<String, List> map = new HashMap<>(1);
        map.put("tSysAreasLists",tSysAreasLists);
        return map;
    }



    private void doParseXML(org.w3c.dom.Element element) throws Exception {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            short nodeType = node.getNodeType();
            if (nodeType == Node.ELEMENT_NODE) {
                if (ELEMENT_RECORD.equalsIgnoreCase(node.getNodeName())) {
                    String id = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_ID);
                    String areaName = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_AREA_NAME);
                    String pid = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_PID);
                    String shortName = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_SHORT_NAME);
                    String level = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_LEVEL);
                    String sort = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_SORT);


                    String uniqueKey = "AREAS_ID" + id;
                    if(uniqueKeyChecking.containsKey(uniqueKey)){
                        throw new ECPBusinessException("解析t_sys_areas.xml失败，AREAS_ID不允许重复["+id+"]");
                    }
                    uniqueKeyChecking.put(uniqueKey,null);

                    TSysAreasList areas = new TSysAreasList();
                    areas.setId(id);
                    areas.setAreaName(areaName);
                    areas.setPid(pid);
                    areas.setShortName(shortName);
                    areas.setLevel(level);
                    if(StringUtils.isNotBlank(CommonUtils.null2String(sort))){
                        areas.setSort(Integer.parseInt(sort));
                    }
                    this.tSysAreasLists.add(areas);
                }
                doParseXML((org.w3c.dom.Element) node);
            }
        }
    }
}
