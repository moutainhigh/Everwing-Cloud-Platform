package com.everwing.coreservice.common.wy.entity.property.property;/**
 * Created by wust on 2017/8/14.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/14
 * @author wusongti@lii.com.cn
 */
public class ProprietorInfo implements java.io.Serializable{
    private static final long serialVersionUID = 4905061684013325020L;
    private String oldHolder;

    private String oldHolderName;

    public String getOldHolder() {
        return this.oldHolder;
    }

    public void setOldHolder(String oldHolder) {
        this.oldHolder = oldHolder;
    }

    public String getOldHolderName() {
        return this.oldHolderName;
    }

    public void setOldHolderName(String oldHolderName) {
        this.oldHolderName = oldHolderName;
    }
}
