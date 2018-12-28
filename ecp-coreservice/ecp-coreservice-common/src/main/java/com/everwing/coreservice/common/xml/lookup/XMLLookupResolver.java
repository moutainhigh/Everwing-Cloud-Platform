package com.everwing.coreservice.common.xml.lookup;/**
 * Created by wust on 2018/6/27.
 */

import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookupList;
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
 * Function:
 * Reason:
 * Date:2018/6/27
 *
 * @author wusongti@lii.com.cn
 */
public class XMLLookupResolver extends XMLDefaultResolver {
    static Logger logger = LogManager.getLogger(XMLLookupResolver.class);

    /**
     * 元素名称
     */
    private static final String ELEMENT_RECORD = "record";
    private static final String ELEMENT_LOOKUP_ID = "lookup_id";
    private static final String ELEMENT_CODE = "code";
    private static final String ELEMENT_NAME = "name";
    private static final String ELEMENT_DESCRIPTION = "description";
    private static final String ELEMENT_ITEM_ORDER = "item_order";


    private final Map<String, String> uniqueKeyIdChecking = new HashMap<>();
    private final Map<String, String> uniqueKeyCodeChecking = new HashMap<>();
    private final List<TSysLookupList> tSysLookupLists = new ArrayList<TSysLookupList>();

    @Override
    public void parseXML() throws ECPBusinessException {
        String mainXMLPath = "initData" + File.separator + "lookup" + File.separator + "t_sys_lookup.xml";
        String mainXSDPath = "initData" + File.separator + "lookup" + File.separator + "t_sys_lookup.xsd";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //validateXML(mainXSDPath, mainXMLPath);

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(ResourceUtils.getFile("classpath:" + mainXMLPath));
            org.w3c.dom.Element element = doc.getDocumentElement();
            doParseXML(element);
        } catch (Exception e) {
            throw new ECPBusinessException(e);
        }
    }

    @Override
    public Map<String, List> getResult() {
        this.parseXML();
        Map<String, List> map = new HashMap<>(1);
        map.put("tSysLookupLists", tSysLookupLists);
        return map;
    }


    private void doParseXML(org.w3c.dom.Element element) throws Exception {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            short nodeType = node.getNodeType();
            if (nodeType == Node.ELEMENT_NODE) {
                if (ELEMENT_RECORD.equalsIgnoreCase(node.getNodeName())) {
                    String lookupId = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_LOOKUP_ID);
                    String code = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_CODE);
                    String name = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_NAME);
                    String description = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_DESCRIPTION);
                    String itemOrder = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ITEM_ORDER);


                    String uniqueKeyId = "LOOKUP_ID" + lookupId;
                    if (uniqueKeyIdChecking.containsKey(uniqueKeyId)) {
                        throw new ECPBusinessException("解析t_sys_lookup.xml失败，LOOKUP_ID元素值不允许重复[" + lookupId + "]");
                    }
                    uniqueKeyIdChecking.put(uniqueKeyId, null);

                    TSysLookupList lookup = new TSysLookupList();
                    lookup.setLookupId(lookupId);
                    lookup.setCode(code);
                    lookup.setName(name);
                    lookup.setDescription(description);
                    if (StringUtils.isNotBlank(CommonUtils.null2String(itemOrder))) {
                        lookup.setItemOrder(Integer.parseInt(itemOrder));
                    }else{
                        lookup.setItemOrder(0);
                    }
                    this.tSysLookupLists.add(lookup);
                }
                doParseXML((org.w3c.dom.Element) node);
            }
        }
    }
}
