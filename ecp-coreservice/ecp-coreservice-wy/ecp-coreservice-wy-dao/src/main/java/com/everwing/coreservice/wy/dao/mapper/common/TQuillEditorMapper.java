package com.everwing.coreservice.wy.dao.mapper.common;/**
 * Created by wust on 2018/11/19.
 */

import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditor;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditorList;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditorSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/11/19
 * @author wusongti@lii.com.cn
 */
public interface TQuillEditorMapper {
    int batchInsert(List<TQuillEditor> tQuillEditors) throws DataAccessException;
    int delete(TQuillEditor tQuillEditor) throws DataAccessException;
    List<TQuillEditorList> findByCondition(TQuillEditorSearch tQuillEditorSearch);
}
