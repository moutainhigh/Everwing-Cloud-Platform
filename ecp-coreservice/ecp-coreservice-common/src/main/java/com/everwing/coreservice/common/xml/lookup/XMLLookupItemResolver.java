package com.everwing.coreservice.common.xml.lookup;/**
 * Created by wust on 2018/6/27.
 */

import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookupItemList;
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
 * Function:
 * Reason:
 * Date:2018/6/27
 * @author wusongti@lii.com.cn
 */
public class XMLLookupItemResolver extends XMLDefaultResolver {
    static Logger logger = LogManager.getLogger(XMLLookupItemResolver.class);

    /**
     * 元素名称
     */
    private static final String ELEMENT_RECORD = "record";
    private static final String ELEMENT_LOOKUP_ITEM_ID = "lookup_item_id";
    private static final String ELEMENT_LOOKUP_ID = "lookup_id";
    private static final String ELEMENT_CODE = "code";
    private static final String ELEMENT_NAME = "name";
    private static final String ELEMENT_PARENT_CODE = "parent_code";
    private static final String ELEMENT_DESCRIPTION = "description";
    private static final String ELEMENT_ITEM_ORDER = "item_order";


    private final Map<String, String> uniqueKeyIdChecking = new HashMap<>();
    private final Map<String, String> uniqueKeyCodeChecking = new HashMap<>();
    private final List<TSysLookupItemList> tSysLookupItemLists = new ArrayList<TSysLookupItemList>();


    @Override
    public void parseXML() throws ECPBusinessException {
        String mainXMLPath = "initData" + File.separator + "lookup" + File.separator + "t_sys_lookup_item.xml";
        String mainXSDPath = "initData" + File.separator + "lookup" + File.separator + "t_sys_lookup_item.xsd";
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

    @Override
    public Map<String, List> getResult() {
        this.parseXML();
        Map<String, List> map = new HashMap<>(1);
        map.put("tSysLookupItemLists",tSysLookupItemLists);
        return map;
    }


    private void doParseXML(org.w3c.dom.Element element) throws Exception {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            short nodeType = node.getNodeType();
            if (nodeType == Node.ELEMENT_NODE) {
                if (ELEMENT_RECORD.equalsIgnoreCase(node.getNodeName())) {
                    String lookupItemId = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_LOOKUP_ITEM_ID);
                    String lookupId = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_LOOKUP_ID);
                    String code = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_CODE);
                    String name = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_NAME);
                    String parentCode = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_PARENT_CODE);
                    String description = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_DESCRIPTION);
                    String itemOrder = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ITEM_ORDER);


                    String uniqueKeyId = "LOOKUP_ITEM_ID" + lookupItemId;
                    if (uniqueKeyIdChecking.containsKey(uniqueKeyId)) {
                        throw new ECPBusinessException("解析t_sys_lookup_item.xml失败，LOOKUP_ITEM_ID元素值不允许重复[" + lookupItemId + "]");
                    }
                    uniqueKeyIdChecking.put(uniqueKeyId, null);

                    String uniqueKeyCode = lookupId + "." + code;
                    if (uniqueKeyCodeChecking.containsKey(uniqueKeyCode)) {
                        throw new ECPBusinessException("解析解析t_sys_lookup_item.xml失败，同一个lookupId下面不能有相同的CODE[" + lookupId + "." +code + "]");
                    }
                    uniqueKeyCodeChecking.put(uniqueKeyCode, null);


                    TSysLookupItemList lookup = new TSysLookupItemList();
                    lookup.setLookupItemId(lookupItemId);
                    lookup.setLookupId(lookupId);
                    lookup.setCode(code);
                    lookup.setName(name);
                    lookup.setParentCode(parentCode);
                    lookup.setDescription(description);


                    if (StringUtils.isNotBlank(CommonUtils.null2String(itemOrder))) {
                        lookup.setItemOrder(Integer.parseInt(itemOrder));
                    }else{
                        lookup.setItemOrder(0);
                    }
                    this.tSysLookupItemLists.add(lookup);
                }
                doParseXML((org.w3c.dom.Element) node);
            }
        }
    }
}
