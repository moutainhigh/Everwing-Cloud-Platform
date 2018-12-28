package com.everwing.coreservice.wy.core.service.impl.common;/**
 * Created by wust on 2017/9/7.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.common.attachment.Attachment;
import com.everwing.coreservice.common.wy.entity.common.attachment.AttachmentList;
import com.everwing.coreservice.common.wy.entity.common.attachment.AttachmentSearch;
import com.everwing.coreservice.common.wy.service.common.AttachmentService;
import com.everwing.coreservice.wy.dao.mapper.common.AttachmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/7
 * @author wusongti@lii.com.cn
 */
@Service("attachmentServiceImpl")
public class AttachmentServiceImpl implements AttachmentService {
    @Autowired
    private AttachmentMapper attachmentMapper;


    @Override
    public List<AttachmentList> findByCondition(WyBusinessContext ctx, AttachmentSearch attachmentSearch) {
        return attachmentMapper.findByCondition(attachmentSearch);
    }


    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap batchInsert(WyBusinessContext ctx, List<Attachment> attachments) {
        MessageMap mm = new MessageMap();
        attachmentMapper.batchInsert(attachments);
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap delete(WyBusinessContext ctx, Attachment attachment) {
        MessageMap mm = new MessageMap();
        attachmentMapper.delete(attachment);
        return mm;
    }
}
