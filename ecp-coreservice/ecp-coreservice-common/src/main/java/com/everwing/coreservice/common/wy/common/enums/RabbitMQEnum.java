package com.everwing.coreservice.common.wy.common.enums;/**
 * Created by wust on 2017/5/27.
 */

/**
 *
 * Function:
 * Reason:使用字面值作为常量，是枚举比常量的一大优势
 * Date:2017/5/27
 * @author wusongti@lii.com.cn
 */
public enum RabbitMQEnum {
    opt,            // 操作类型，参考下面的insert,batchInsert...
    companyId,
    data,
    insert,
    batchInsert,
    del,
    batchDel,
    modify,
    batchModify,
    insertOrModify
}
