package com.everwing.coreservice.wy.api.common;/**
 * Created by wust on 2018/11/19.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditorList;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditorSearch;
import com.everwing.coreservice.common.wy.service.common.QuillEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/11/19
 * @author wusongti@lii.com.cn
 */
@Component
public class QuillEditorApi {
    @Autowired
    private QuillEditorService quillEditorService;

    public RemoteModelResult<List<TQuillEditorList>> findByCondition(WyBusinessContext ctx,TQuillEditorSearch tQuillEditorSearch) {
        RemoteModelResult<List<TQuillEditorList>> result = new RemoteModelResult<>();
        List<TQuillEditorList> lists = quillEditorService.findByCondition(ctx,tQuillEditorSearch);
        result.setModel(lists);
        return result;
    }
}
