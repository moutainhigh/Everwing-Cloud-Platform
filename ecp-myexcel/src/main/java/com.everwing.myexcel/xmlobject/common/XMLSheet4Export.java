package com.everwing.myexcel.xmlobject.common;

/**
 * 工作薄对象
 */
public class XMLSheet4Export{
    // label属性
    private String attributeLabel;

    // list元素集合
    private XMLList4Export xmlList4Export;

    public String getAttributeLabel() {
        return attributeLabel;
    }

    public void setAttributeLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    public XMLList4Export getXmlList4Export() {
        return xmlList4Export;
    }

    public void setXmlList4Export(XMLList4Export xmlList4Export) {
        this.xmlList4Export = xmlList4Export;
    }

    @Override
    public String toString() {
        return "XMLSheet4Export{" +
                "attributeLabel='" + attributeLabel + '\'' +
                ", xmlList4Export=" + xmlList4Export +
                '}';
    }
}