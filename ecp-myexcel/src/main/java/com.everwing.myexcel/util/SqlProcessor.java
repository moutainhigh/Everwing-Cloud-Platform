package com.everwing.myexcel.util;

import com.everwing.myexcel.exception.MyExcelException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * SQL处理工具
 */
public class SqlProcessor {

    private static Configuration freeMarkerConfig = new freemarker.template.Configuration();

    // 存储解析过的SQL
    private static HashMap<String, Template> sqlMap = new HashMap<>();

    /**
     * 解析SQL
     * @param sqlKey
     * @param sql
     * @param object
     * @return
     * @throws Exception
     */
    public static String parseSQL(String sqlKey,String sql, Object object) throws MyExcelException {

        Template template = getTemplateByKey(sqlKey,sql);

        return getProcessString(template, object);
    }

    private static Template getLocalTemplate(String sqlName) {
        return sqlMap.get(sqlName);
    }

    private static Template getTemplateByKey(String sqlKey,String sql) throws MyExcelException {

        Template template = getLocalTemplate(sqlKey);

        if (template == null) {

            StringTemplateLoader loader = new StringTemplateLoader();

            loader.putTemplate(sqlKey, sql);

            freeMarkerConfig.setTemplateLoader(loader);

            try {
                template = freeMarkerConfig.getTemplate(sqlKey);
            } catch (IOException e) {
                throw new MyExcelException(e);
            }

            sqlMap.put(sqlKey, template);
        }
        return template;
    }

    private static String getProcessString(Template template ,Object dataModel) throws MyExcelException{
        StringWriter stringWriter = new StringWriter();
        try {
            template.process(dataModel, stringWriter);
        } catch (TemplateException e) {
            throw new MyExcelException(e);
        } catch (IOException e) {
            throw new MyExcelException(e);
        }
        return stringWriter.toString();
    }

}