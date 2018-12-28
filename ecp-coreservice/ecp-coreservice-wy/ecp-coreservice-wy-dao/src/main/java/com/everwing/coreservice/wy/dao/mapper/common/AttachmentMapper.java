package com.everwing.coreservice.wy.dao.mapper.common;/**
 * Created by wust on 2017/9/6.
 */

import com.everwing.coreservice.common.wy.entity.common.attachment.Attachment;
import com.everwing.coreservice.common.wy.entity.common.attachment.AttachmentList;
import com.everwing.coreservice.common.wy.entity.common.attachment.AttachmentSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/6
 * @author wusongti@lii.com.cn
 */
public interface AttachmentMapper {

    List<AttachmentList> findByCondition(AttachmentSearch tProductAttachmentSearch) throws DataAccessException;

    /**
     * 批量新建附件
     * @param list
     * @return
     */
    int batchInsert(List<Attachment> list) throws DataAccessException;

    /**
     * @param attachment
     * @return
     */
    int delete(Attachment attachment) throws DataAccessException;

}
