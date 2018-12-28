package com.everwing.myexcel.xmlobject.common;

/**
 * 字段元素
 */
public class XMLField4Import {
    // index属性
    private Integer attributeIndex;

    // 值类型
    private String attributeType;

    // attributeProperty属性
    private String attributeProperty;

    // 键值对，例如：{1：成功；2：失败}
    private String attributeFormat;

    // attributeRequired属性
    private String attributeRequired;

    // 日期格式
    private String attributePattern;

    // lookupCode属性
    private String attributeLookupCode;

    // parentCode属性
    private String attributeParentCode;

    // 正则表达式
    private String attributeRegex;

    // 不符合正则表达式的时的错误消息
    private String attributeRegexErrMsg;


    public Integer getAttributeIndex() {
        return attributeIndex;
    }

    public void setAttributeIndex(Integer attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getAttributeProperty() {
        return attributeProperty;
    }

    public void setAttributeProperty(String attributeProperty) {
        this.attributeProperty = attributeProperty;
    }

    public String getAttributeFormat() {
        return attributeFormat;
    }

    public void setAttributeFormat(String attributeFormat) {
        this.attributeFormat = attributeFormat;
    }

    public String getAttributeRequired() {
        return attributeRequired;
    }

    public void setAttributeRequired(String attributeRequired) {
        this.attributeRequired = attributeRequired;
    }

    public String getAttributePattern() {
        return attributePattern;
    }

    public void setAttributePattern(String attributePattern) {
        this.attributePattern = attributePattern;
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

    public String getAttributeRegex() {
        return attributeRegex;
    }

    public void setAttributeRegex(String attributeRegex) {
        this.attributeRegex = attributeRegex;
    }

    public String getAttributeRegexErrMsg() {
        return attributeRegexErrMsg;
    }

    public void setAttributeRegexErrMsg(String attributeRegexErrMsg) {
        this.attributeRegexErrMsg = attributeRegexErrMsg;
    }

    @Override
    public String toString() {
        return "XMLField4Import{" +
                "attributeIndex=" + attributeIndex +
                ", attributeType='" + attributeType + '\'' +
                ", attributeProperty='" + attributeProperty + '\'' +
                ", attributeFormat='" + attributeFormat + '\'' +
                ", attributeRequired='" + attributeRequired + '\'' +
                ", attributePattern='" + attributePattern + '\'' +
                ", attributeLookupCode='" + attributeLookupCode + '\'' +
                ", attributeParentCode='" + attributeParentCode + '\'' +
                ", attributeRegex='" + attributeRegex + '\'' +
                ", attributeRegexErrMsg='" + attributeRegexErrMsg + '\'' +
                '}';
    }
}