package com.everwing.myexcel.factory;/**
 * Created by wust on 2017/8/4.
 */

import com.everwing.myexcel.definition.ExcelDefinitionReader;

/**
 *
 * Function:抽象工厂
 * Reason:负责生产ExcelDefinitionReader
 * Date:2017/8/4
 * @author wusongti@lii.com.cn
 */
public interface DefinitionFactory {
    ExcelDefinitionReader createExcelDefinitionReader();
}
