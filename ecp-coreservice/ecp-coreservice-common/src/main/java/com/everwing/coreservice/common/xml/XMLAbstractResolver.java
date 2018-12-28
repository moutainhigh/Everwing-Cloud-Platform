/**
 * Project Name:webTYJ
 * File Name:ProcessingPermissionXML.java
 * Package Name:com.everwing.security.core
 * Date:2016年12月26日上午8:36:32
 * Copyright (c) 2016, wusongti@lii.com.cn All Rights Reserved.
 */

package com.everwing.coreservice.common.xml;

import com.everwing.coreservice.common.exception.ECPBusinessException;

import java.util.List;
import java.util.Map;

/**
 * ClassName:XMLAbstractResolver <br/>
 * Function: XML处理工具. <br/>
 * Reason:
 * Date:     2016年12月26日 上午8:36:32 <br/>
 *
 * @author wust
 * @see
 */
public interface XMLAbstractResolver {

    /**
     * 验证XML
     * @param xsdPath
     * @param xmlPath
     * @throws ECPBusinessException
     */
    void validateXML(String xsdPath, String xmlPath) throws ECPBusinessException;

    /**
     * 解析XML
     * @throws ECPBusinessException
     */
    void parseXML() throws ECPBusinessException;

    /**
     * 获取解析结果
     * @return
     */
    Map<String,List> getResult();
}

