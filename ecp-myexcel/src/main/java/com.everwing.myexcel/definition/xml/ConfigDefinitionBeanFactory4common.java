package com.everwing.myexcel.definition.xml;/**
 * Created by wust on 2017/9/11.
 */

import com.everwing.myexcel.ConfigDefinitionBean;
import com.everwing.myexcel.exception.MyExcelException;
import com.everwing.myexcel.xmlobject.common.XMLExcel4Export;
import com.everwing.myexcel.xmlobject.common.XMLExcel4Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Function:获取普通XML配置对象
 * Reason:因为只需要第一次解析XML配置，后面可重复利用，节省时间
 * Date:2017/9/11
 * @author wusongti@lii.com.cn
 */
public class ConfigDefinitionBeanFactory4common {
    Map<String,ConfigDefinitionBean> registryMap = new HashMap<>(100);

    private ConfigDefinitionBeanFactory4common(){}
    private static ConfigDefinitionBeanFactory4common instance = new ConfigDefinitionBeanFactory4common();
    public static ConfigDefinitionBeanFactory4common getInstance(){
        return instance;
    }

    /**
     * 注册导出配置定义
     * @param xmlPath
     * @param excelDefinitionReader4XMLExport
     * @return
     */
    public ConfigDefinitionBean getExportRegisterConfigDefinitions(String xmlPath, XMLExcelDefinitionReader4commonExport excelDefinitionReader4XMLExport){
        if(registryMap.containsKey(xmlPath)){
            return registryMap.get(xmlPath);
        }else {
            Resource resource = new ClassPathResource(xmlPath);
            try {
                XMLExcel4Export xmlExcel4Export = new XMLExcel4Export();
                excelDefinitionReader4XMLExport.doRegisterConfigDefinitions(resource.getInputStream(),xmlExcel4Export);
                registryMap.put(xmlPath,xmlExcel4Export);
                return xmlExcel4Export;
            } catch (Exception e) {
                throw new MyExcelException(e);
            }
        }
    }

    /**
     * 注册导入配置定义
     * @param key
     * @param excelDefinitionReader4XMLImport
     * @return
     */
    public ConfigDefinitionBean getImportRegisterConfigDefinitions(String key, XMLExcelDefinitionReader4commonImport excelDefinitionReader4XMLImport){
        if(registryMap.containsKey(key)){
            return registryMap.get(key);
        }else {
            Resource resource = new ClassPathResource(key);
            try {
                XMLExcel4Import xmlExcel4Import = new XMLExcel4Import();
                excelDefinitionReader4XMLImport.doRegisterConfigDefinitions(resource.getInputStream(),xmlExcel4Import);
                registryMap.put(key,xmlExcel4Import);
                return xmlExcel4Import;
            } catch (Exception e) {
                throw new MyExcelException(e);
            }
        }
    }
}
