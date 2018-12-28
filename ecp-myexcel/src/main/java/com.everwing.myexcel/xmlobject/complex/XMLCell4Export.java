package com.everwing.myexcel.xmlobject.complex;/**
 * Created by wust on 2018/1/15.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/15
 * @author wusongti@lii.com.cn
 */
public class XMLCell4Export {
    // column属性
    private String attributeColumn;

    private String rowspan;

    private String colspan;

    private String cellText;


    public String getAttributeColumn() {
        return attributeColumn;
    }

    public void setAttributeColumn(String attributeColumn) {
        this.attributeColumn = attributeColumn;
    }

    public String getRowspan() {
        return rowspan;
    }

    public void setRowspan(String rowspan) {
        this.rowspan = rowspan;
    }

    public String getColspan() {
        return colspan;
    }

    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    public String getCellText() {
        return cellText;
    }

    public void setCellText(String cellText) {
        this.cellText = cellText;
    }
}
