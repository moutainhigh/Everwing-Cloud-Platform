package com.everwing.myexcel.xmlobject.common;

/**
 * 工作薄对象
 */
public class XMLSheet4Import{
    // index属性
    private Integer attributeIndex;

    // list元素集合
    private XMLList4Import xmlList4Import;


    public Integer getAttributeIndex() {
        return attributeIndex;
    }

    public void setAttributeIndex(Integer attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    public XMLList4Import getXmlList4Import() {
        return xmlList4Import;
    }

    public void setXmlList4Import(XMLList4Import xmlList4Import) {
        this.xmlList4Import = xmlList4Import;
    }

    /**
     * 非空判断
     * @return
     */
    public boolean isEmpty(){
        if(attributeIndex == null
                && xmlList4Import == null){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "XMLSheet4Import{" +
                "attributeIndex=" + attributeIndex +
                ", elementLists=" + xmlList4Import +
                '}';
    }
}