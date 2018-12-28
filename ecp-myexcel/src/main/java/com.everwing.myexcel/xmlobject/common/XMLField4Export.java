package com.everwing.myexcel.xmlobject.common;

/**
 * 字段元素
 */
public class XMLField4Export{
    // column属性
    private String attributeColumn;

    // property属性
    private String attributeLabel;

    // attributeType属性
    private String attributeType;

    // attributeFormat属性
    private String attributeFormat;

    // lookupCode属性
    private String attributeLookupCode;

    // parentCode属性
    private String attributeParentCode;


    public String getAttributeColumn() {
        return attributeColumn;
    }

    public void setAttributeColumn(String attributeColumn) {
        this.attributeColumn = attributeColumn;
    }

    public String getAttributeLabel() {
        return attributeLabel;
    }

    public void setAttributeLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getAttributeFormat() {
        return attributeFormat;
    }

    public void setAttributeFormat(String attributeFormat) {
        this.attributeFormat = attributeFormat;
    }

    public String getAttributeLookupCode() {
        return attributeLookupCode;
    }

    public void setAttributeLookupCode(String attributeLookupCode) {
        this.attributeLookupCode = attributeLookupCode;
    }

    public String getAttributeParentCode() {
        return attributeParentCode;
    }

    public void setAttributeParentCode(String attributeParentCode) {
        this.attributeParentCode = attributeParentCode;
    }

    @Override
    public String toString() {
        return "XMLField4Export{" +
                "attributeColumn='" + attributeColumn + '\'' +
                ", attributeLabel='" + attributeLabel + '\'' +
                ", attributeType='" + attributeType + '\'' +
                ", attributeFormat='" + attributeFormat + '\'' +
                ", attributeLookupCode='" + attributeLookupCode + '\'' +
                ", attributeParentCode='" + attributeParentCode + '\'' +
                '}';
    }
}