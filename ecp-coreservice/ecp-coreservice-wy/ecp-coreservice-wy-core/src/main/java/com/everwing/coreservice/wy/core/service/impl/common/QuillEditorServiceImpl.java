package com.everwing.coreservice.wy.core.service.impl.common;/**
 * Created by wust on 2018/11/19.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditorList;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditorSearch;
import com.everwing.coreservice.common.wy.service.common.QuillEditorService;
import com.everwing.coreservice.wy.dao.mapper.common.TQuillEditorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/11/19
 * @author wusongti@lii.com.cn
 */
@Service("quillEditorServiceImpl")
public class QuillEditorServiceImpl implements QuillEditorService {
    @Autowired
    private TQuillEditorMapper tQuillEditorMapper;

    @Override
    public List<TQuillEditorList> findByCondition(WyBusinessContext ctx, TQuillEditorSearch tQuillEditorSearch) {
        return tQuillEditorMapper.findByCondition(tQuillEditorSearch);
    }
}
