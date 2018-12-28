package com.everwing.myexcel.xmlobject.common;

import java.util.List;

/**
 * 列表对象
 */
public class XMLList4Export{
    // label属性
    private String attributeLabel;

    // sql元素
    private XMLSQL4Export elementSql;

    // fields元素集合
    private List<XMLField4Export> elementFields;

    public String getAttributeLabel() {
        return attributeLabel;
    }

    public void setAttributeLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    public XMLSQL4Export getElementSql() {
        return elementSql;
    }

    public void setElementSql(XMLSQL4Export elementSql) {
        this.elementSql = elementSql;
    }

    public List<XMLField4Export> getElementFields() {
        return elementFields;
    }

    public void setElementFields(List<XMLField4Export> elementFields) {
        this.elementFields = elementFields;
    }

    @Override
    public String toString() {
        return "XMLList4Export{" +
                "attributeLabel='" + attributeLabel + '\'' +
                ", elementSql=" + elementSql +
                ", elementFields=" + elementFields +
                '}';
    }
}