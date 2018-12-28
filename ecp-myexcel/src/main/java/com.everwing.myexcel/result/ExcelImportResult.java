package com.everwing.myexcel.result;/**
 * Created by wust on 2017/5/8.
 */

import java.util.List;
import java.util.Map;

/**
 *
 * Function:导入结果对象
 * Reason:
 * Date:2017/5/8
 * @author wusongti@lii.com.cn
 */
public class ExcelImportResult implements java.io.Serializable {
    private static final long serialVersionUID = 3097417662878555270L;
    /** 列表数据集合，key=sheetIndex,value=List<EntityName> */
    private Map<String, List<?>> listMap;

    public Map<String, List<?>> getListMap() {
        return listMap;
    }

    public void setListMap(Map<String, List<?>> listMap) {
        this.listMap = listMap;
    }
}
