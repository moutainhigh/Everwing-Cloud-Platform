package com.everwing.coreservice.common.xml.factory;/**
 * Created by wust on 2018/6/27.
 */

import com.everwing.coreservice.common.xml.XMLAbstractResolver;
import com.everwing.coreservice.common.xml.XMLDefinitionFactory;
import com.everwing.coreservice.common.xml.areas.XMLAreasResolver;

/**
 *
 * Function:
 * Reason:
 * Date:2018/6/27
 * @author wusongti@lii.com.cn
 */
public class XMLAreasFactory implements XMLDefinitionFactory {

    @Override
    public XMLAbstractResolver createXMLResolver() {
        XMLAbstractResolver xmlAbstractResolver = new XMLAreasResolver();
        return xmlAbstractResolver;
    }
}
