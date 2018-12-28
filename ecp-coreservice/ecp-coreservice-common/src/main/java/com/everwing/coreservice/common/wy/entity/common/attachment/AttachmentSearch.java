package com.everwing.coreservice.common.wy.entity.common.attachment;/**
 * Created by wust on 2017/9/6.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/6
 * @author wusongti@lii.com.cn
 */
public class AttachmentSearch extends Attachment {
    private static final long serialVersionUID = -3497472208128840470L;

    private Page page;

    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
