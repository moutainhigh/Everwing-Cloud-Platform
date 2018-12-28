package com.everwing.myexcel.xmlobject.common;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 列表对象
 */
public class XMLList4Import{
    // index属性
    private Integer attributeIndex;

    // 表示列表数据的开始行数
    private Integer attributeStartRow;

    // fields元素集合
    private List<XMLField4Import> elementFields;

    // 类路径
    private String attributeClass;


    public Integer getAttributeIndex() {
        return attributeIndex;
    }

    public void setAttributeIndex(Integer attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    public Integer getAttributeStartRow() {
        return attributeStartRow;
    }

    public void setAttributeStartRow(Integer attributeStartRow) {
        this.attributeStartRow = attributeStartRow;
    }

    public List<XMLField4Import> getElementFields() {
        return elementFields;
    }

    public void setElementFields(List<XMLField4Import> elementFields) {
        this.elementFields = elementFields;
    }

    public String getAttributeClass() {
        return attributeClass;
    }

    public void setAttributeClass(String attributeClass) {
        this.attributeClass = attributeClass;
    }

    public boolean isEmpty(){
        if(attributeIndex == null
                && attributeStartRow == null
                && attributeClass == null
                && CollectionUtils.isEmpty(elementFields)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "XMLList4Import{" +
                "attributeIndex=" + attributeIndex +
                ", attributeStartRow=" + attributeStartRow +
                ", elementFields=" + elementFields +
                ", classPath='" + attributeClass + '\'' +
                '}';
    }
}