package com.everwing.myexcel.xmlobject.common;

/**
 * Created by wust on 2017/5/18.
 */

import com.everwing.myexcel.ConfigDefinitionBean;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/18
 * @author wusongti@lii.com.cn
 */
public class XMLExcel4Export extends ConfigDefinitionBean {
    private static final long serialVersionUID = -6949258867682519638L;
    private String attributeId;

    private List<XMLSheet4Export> xmlSheet4ExportList;

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public List<XMLSheet4Export> getXmlSheet4ExportList() {
        return xmlSheet4ExportList;
    }

    public void setXmlSheet4ExportList(List<XMLSheet4Export> xmlSheet4ExportList) {
        this.xmlSheet4ExportList = xmlSheet4ExportList;
    }

    public boolean isEmpty(){
        if(attributeId == null
                && CollectionUtils.isEmpty(xmlSheet4ExportList)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "XMLExcel4Export{" +
                "attributeId='" + attributeId + '\'' +
                ", xmlSheet4ExportList=" + xmlSheet4ExportList +
                '}';
    }
}
