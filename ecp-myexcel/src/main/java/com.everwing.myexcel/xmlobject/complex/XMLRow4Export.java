package com.everwing.myexcel.xmlobject.complex;/**
 * Created by wust on 2018/1/15.
 */

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/15
 * @author wusongti@lii.com.cn
 */
public class XMLRow4Export {
    private String attributeIndex;
    private List<XMLCell4Export> xmlCell4ExportList;


    public String getAttributeIndex() {
        return attributeIndex;
    }

    public void setAttributeIndex(String attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    public List<XMLCell4Export> getXmlCell4ExportList() {
        return xmlCell4ExportList;
    }

    public void setXmlCell4ExportList(List<XMLCell4Export> xmlCell4ExportList) {
        this.xmlCell4ExportList = xmlCell4ExportList;
    }
}
