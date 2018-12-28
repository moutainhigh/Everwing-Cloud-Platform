package com.everwing.myexcel.definition;/**
 * Created by wust on 2017/5/8.
 */


import com.everwing.myexcel.ConfigDefinitionBean;

/**
 *
 * Function:注册配置接口
 * Reason:
 *
 * Date:2017/5/8
 * @author wusongti@lii.com.cn
 */
public interface ExcelDefinitionReader {
    /**
     * 注册配置
     * @return
     */
    ConfigDefinitionBean registerConfigDefinitions();
}
