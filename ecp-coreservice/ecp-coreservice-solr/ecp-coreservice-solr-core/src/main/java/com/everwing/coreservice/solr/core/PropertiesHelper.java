package com.everwing.coreservice.solr.core;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件读取辅助类
 */
public class PropertiesHelper {
    private static Map<String, PropertiesHelper> instanceMap = new Hashtable<>();
    private static Map<String,Resource> resourceMap = new Hashtable<>();
    private static Map<String,Properties> propertiesMap = new Hashtable<>();

    private static PropertiesHelper instance = null;
    private static Resource resource = null;
    public static Properties proper = null;

    private PropertiesHelper(){}
    public static PropertiesHelper getInstance(String propertiesName){
        synchronized(PropertiesHelper.class){
            if(resourceMap.containsKey(propertiesName)){
                resource = resourceMap.get(propertiesName);
            }else{
                resource = new ClassPathResource(propertiesName);
                resourceMap.put(propertiesName,resource);
            }

            if(instanceMap.containsKey(propertiesName)){
                instance = instanceMap.get(propertiesName);
            }else{

                instance = new PropertiesHelper();
                instanceMap.put(propertiesName,instance);
            }

            if(propertiesMap.containsKey(propertiesName)){
                proper = propertiesMap.get(propertiesName);
            }else{
                InputStream in = null;
                try {
                    in = resource.getInputStream();
                    proper = new Properties();
                    proper.load(in);
                    propertiesMap.put(propertiesName,proper);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(in != null){
                        try {
                            in.close();
                        } catch (IOException e) {

                        }
                    }
                }
            }
        }
        return instance;
    }

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    public String getValue(String key){
        return proper.getProperty(key);
    }

    /**
     * 根据当前系统获取指定key的value
     * @param key
     * @return
     */
	public String getDefaultSystemValue(String key) {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("windows")) {
			return proper.getProperty(key + "4windows");
		} else if (os.contains("mac")) {
			return proper.getProperty(key + "4mac");
		} else {
			return proper.getProperty(key + "4linux");
		}
	}
}
