package com.everwing.coreservice.wy.api.common;/**
 * Created by wust on 2017/9/7.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.common.attachment.Attachment;
import com.everwing.coreservice.common.wy.entity.common.attachment.AttachmentList;
import com.everwing.coreservice.common.wy.entity.common.attachment.AttachmentSearch;
import com.everwing.coreservice.common.wy.service.common.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/7
 * @author wusongti@lii.com.cn
 */
@Component
public class AttachmentApi {
    @Autowired
    private AttachmentService attachmentService;


    public RemoteModelResult<List<AttachmentList>> findByCondition(WyBusinessContext ctx, AttachmentSearch attachmentSearch) {
        RemoteModelResult<List<AttachmentList>> result = new RemoteModelResult<>();
        result.setModel(attachmentService.findByCondition(ctx,attachmentSearch));
        return result;
    }


    public RemoteModelResult<MessageMap> batchInsert(WyBusinessContext ctx, List<Attachment> attachments) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(attachmentService.batchInsert(ctx,attachments));
        return result;
    }

    public RemoteModelResult<MessageMap> delete(WyBusinessContext ctx, Attachment attachment) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(attachmentService.delete(ctx,attachment));
        return result;
    }

}
