package com.everwing.myexcel.definition.xml;/**
 * Created by wust on 2017/9/11.
 */

import com.everwing.myexcel.ConfigDefinitionBean;
import com.everwing.myexcel.exception.MyExcelException;
import com.everwing.myexcel.xmlobject.complex.XMLExcel4Export;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Function:获取复杂XML配置对象
 * Reason:因为只需要第一次解析XML配置，后面可重复利用，节省时间
 * Date:2017/9/11
 * @author wusongti@lii.com.cn
 */
public class ConfigDefinitionBeanFactory4complex {
    Map<String,ConfigDefinitionBean> registryMap = new HashMap<>(100);

    private ConfigDefinitionBeanFactory4complex(){}
    private static ConfigDefinitionBeanFactory4complex instance = new ConfigDefinitionBeanFactory4complex();
    public static ConfigDefinitionBeanFactory4complex getInstance(){
        return instance;
    }

    /**
     * 注册导出配置定义
     * @param xmlPath
     * @param excelDefinitionReader4XMLComplexExport
     * @return
     */
    public ConfigDefinitionBean getExportRegisterConfigDefinitions(String xmlPath, XMLExcelDefinitionReader4complexExport excelDefinitionReader4XMLComplexExport){
        if(registryMap.containsKey(xmlPath)){
            return registryMap.get(xmlPath);
        }else {
            Resource resource = new ClassPathResource(xmlPath);
            try {
                XMLExcel4Export xmlExcel4Export = new XMLExcel4Export();
                excelDefinitionReader4XMLComplexExport.doRegisterConfigDefinitions(resource.getInputStream(),xmlExcel4Export);
                registryMap.put(xmlPath,xmlExcel4Export);
                return xmlExcel4Export;
            } catch (Exception e) {
                throw new MyExcelException(e);
            }
        }
    }
}
