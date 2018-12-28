package com.everwing.coreservice.common.wy.entity.common.attachment;/**
 * Created by wust on 2017/9/6.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/6
 * @author wusongti@lii.com.cn
 */
public class Attachment extends BaseEntity implements Cloneable{
    private static final long serialVersionUID = 6083596171070629283L;

    private String id;

    private String tableId;

    private String relationId;

    // 附件类型
    private String attachmentType;

    // 对应文件系统里面存储的附件key
    private String attachmentKey;

    // 附件名称
    private String attachmentName;

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

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentKey() {
        return attachmentKey;
    }

    public void setAttachmentKey(String attachmentKey) {
        this.attachmentKey = attachmentKey;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
