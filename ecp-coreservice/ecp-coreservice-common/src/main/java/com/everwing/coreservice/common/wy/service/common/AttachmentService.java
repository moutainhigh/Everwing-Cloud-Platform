package com.everwing.coreservice.common.wy.service.common;/**
 * Created by wust on 2017/9/7.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.common.attachment.Attachment;
import com.everwing.coreservice.common.wy.entity.common.attachment.AttachmentList;
import com.everwing.coreservice.common.wy.entity.common.attachment.AttachmentSearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/7
 * @author wusongti@lii.com.cn
 */
public interface AttachmentService {

    List<AttachmentList> findByCondition(WyBusinessContext ctx, AttachmentSearch attachmentSearch);


    MessageMap batchInsert(WyBusinessContext ctx, List<Attachment> attachments);


    MessageMap delete(WyBusinessContext ctx, Attachment attachment);
}
