package com.everwing.coreservice.common.wy.entity.common.quilleditor;/**
 * Created by wust on 2018/11/19.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:富文本持久化对象
 * Reason:
 * Date:2018/11/19
 * @author wusongti@lii.com.cn
 */
public class TQuillEditor extends BaseEntity{
    private static final long serialVersionUID = -9154282549828903925L;

    private String id;
    private String tableId;
    private String relationId;
    private String htmlContent;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
