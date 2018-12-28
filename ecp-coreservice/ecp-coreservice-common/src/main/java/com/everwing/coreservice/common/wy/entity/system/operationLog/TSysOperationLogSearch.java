package com.everwing.coreservice.common.wy.entity.system.operationLog;/**
 * Created by wust on 2017/6/2.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:操作日志查询对象
 * Reason:
 * Date:2017/6/2
 * @author wusongti@lii.com.cn
 */
public class TSysOperationLogSearch extends TSysOperationLog{
    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
