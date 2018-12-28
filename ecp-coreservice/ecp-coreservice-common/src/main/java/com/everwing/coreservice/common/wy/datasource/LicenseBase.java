package com.everwing.coreservice.common.wy.datasource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.utils.CommonUtils;

import java.io.File;
import java.util.*;


public class LicenseBase {


    /**
     * @describe 查询serverId对应的数据源记录
     */
    public static String getDataSourceName(String companyId) {
        String url;
        String dsLinks = "";


        if (dsLinks == null || dsLinks.length() == 2) {
            return "";
        }

        dsLinks = dsLinks.substring(1, dsLinks.length() - 1);
        JSONObject resultInf = JSONObject.parseObject(dsLinks);
        String stateMsg = resultInf.getString("state");

        HashMap<String, String> map = null;
        if ("success".equals(stateMsg)) {
            map = new HashMap<String, String>();
            JSONArray companyInf = resultInf.getJSONArray("info");
            JSONObject obj = null;
            for (int i = 0; i < companyInf.size(); i++) {
                obj = JSONObject.parseObject(CommonUtils.null2String(companyInf.get(i)));
                if (companyId != null && companyId.equals(obj.get("companyId"))) {
                    map.put("COMPANY_ID", obj.getString("companyId"));
                    map.put("URL", obj.getString("jdbcUrl"));
                    map.put("USERNAME", obj.getString("jdbcUsername"));
                    map.put("PASSWORD", obj.getString("jdbcPassword"));
                    break;
                }
            }
        }

        url = map.get("URL").toString();
        //jdbc:mysql://120.24.223.107:3306/home_jiajia?Unicode=true&amp;characterEncoding=UTF-8
        for (int i = url.length(); i > 0; i--) {
            if (url.substring(i - 1, i).equals("?")) {
                url = url.substring(0, i - 1);
                break;
            }
        }
        for (int i = url.length(); i > 0; i--) {
            if (url.substring(i - 1, i).equals("/")) {
                url = url.substring(i, url.length());
                break;
            }
        }
        return url;
    }


    public static List<String> listCompany() {
        String dsLinks = "";
        List<String> companyList = new ArrayList();
        try {
            //TODO
            /*String loginUrl = PropertiesHelper.optValue("upfUrl");
			org.apache.cxf.jaxws.JaxWsProxyFactoryBean factory = com.flf.util.ClientUtil.getClientFactory(loginUrl + "/CompanyInfoService", com.flf.service.CompanyInfoService.class);
			com.flf.service.CompanyInfoService companyService = (com.flf.service.CompanyInfoService) factory.create();// WebService
			dsLinks=companyService.selectAll();*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dsLinks == null || dsLinks.length() == 2) {
            return null;
        }

        dsLinks = dsLinks.substring(1, dsLinks.length() - 1);
        JSONObject resultInf = JSONObject.parseObject(dsLinks);
        String stateMsg = resultInf.getString("state");

        HashMap<String, String> map = null;

        if ("success".equals(stateMsg)) {
            map = new HashMap<String, String>();
            JSONArray companyInf = resultInf.getJSONArray("info");
            JSONObject obj = null;
            for (int i = 0; i < companyInf.size(); i++) {
                obj = JSONObject.parseObject(CommonUtils.null2String(companyInf.get(i)));
                companyList.add(obj.getString("companyId"));
            }
        }
        return companyList;
    }

    //文件大小
    public static Double getfileSize(String databaseName) {
        String configPath = "upload/" + databaseName;
        //传入文件路径
        File file = new File(configPath);
        //测试此文件是否存在
        if (file.exists()) {
            //如果是文件夹
            //这里只检测了文件夹中第一层 如果有需要 可以继续递归检测
            if (file.isDirectory()) {
                int size = 0;
                for (File zf : file.listFiles()) {
                    if (zf.isDirectory()) continue;
                    size += zf.length();
                }
                System.out.println("文件夹 " + file.getName() + " Size: " + (size / 1024d / 1024d) + "mb");
                return size / 1024d / 1024d;
            } else {
                System.out.println(file.getName() + " Size: " + (file.length() / 1024d / 1024d) + "mb");
                return file.length() / 1024d / 1024d;
            }
            //如果文件不存在
        } else {
            return 0D;
        }
    }

    //使用月份
    public static int calculateMonthIn(Date date1, Date date2) {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(date1);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(date2);
        int c =
                (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12 + cal1.get(Calendar.MONTH)
                        - cal2.get(Calendar.MONTH);
        return c;
    }

}
