package com.everwing.coreservice.common.wy.service.common;/**
 * Created by wust on 2018/11/19.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditorList;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditorSearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/11/19
 * @author wusongti@lii.com.cn
 */
public interface QuillEditorService {
    List<TQuillEditorList> findByCondition(WyBusinessContext ctx,TQuillEditorSearch tQuillEditorSearch);
}
