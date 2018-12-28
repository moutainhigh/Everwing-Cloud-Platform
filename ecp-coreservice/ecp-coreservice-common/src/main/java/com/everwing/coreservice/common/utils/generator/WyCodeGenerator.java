package com.everwing.coreservice.common.utils.generator;/**
 * Created by wust on 2018/8/6.
 */

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * Function:物业系统专用的编码生成器
 * Reason:可在分布式并发环境下使用
 * Date:2018/8/6
 * @author wusongti@lii.com.cn
 */
@Component
public class WyCodeGenerator {

    private static final String CODE_TYPE_PRODUCT_CODE = "productCode";
    private static final String CODE_TYPE_PRODUCT_ORDER_CODE = "productOrderCode";
    private static final String CODE_TYPE_IMPORT_EXPORT_CODE = "importExportCode";
    private static final String CODE_TYPE_BUILDING_CODE = "buildingCode";
    private static final String CODE_TYPE_COMPANY_CODE = "companyCode";
    private static final String CODE_TYPE_PROJECT_CODE = "projectCode";
    private static final String CODE_TYPE_DEPARTMENT_CODE = "departmentCode";
    private static final String CODE_TYPE_ROLE_CODE = "roleCode";
    private static final String CODE_TYPE_USER_CODE = "userCode";
    private static final String CODE_TYPE_SYNCHRODATA_CODE = "synchrodataCode";

    private static final String CODE_PREFIX_PRODUCT_CODE = "PD";
    private static final String CODE_PREFIX_PRODUCT_ORDER_CODE = "PO";
    private static final String CODE_PREFIX_BUILDING_CODE = "BLDG";

    private WyCodeGenerator() {
    }


    /**
     * 产品编码
     * @return
     */
    public static String genProductCode(){
        int codeLength = 6;

        String productCode = "";

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String key = ctx.getCompanyId() + CODE_TYPE_PRODUCT_CODE + dateStr;

        long newValue = getNewValueByKey(key,1,1,TimeUnit.DAYS,"Product Code");

        productCode = CODE_PREFIX_PRODUCT_CODE + dateStr + StringUtils.leftPad(newValue + "",codeLength,"0");

        return productCode;
    }

    /**
     * 产品订单编码
     * @return
     */
    public static String genProductOrderCode(){
        int codeLength = 6;

        String productOrderCode = "";

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String key = ctx.getCompanyId() + CODE_TYPE_PRODUCT_ORDER_CODE + dateStr;

        long newValue = getNewValueByKey(key,1,1,TimeUnit.DAYS,"Product Order BatchNo");

        productOrderCode = CODE_PREFIX_PRODUCT_ORDER_CODE + dateStr + StringUtils.leftPad(newValue + "",codeLength,"0");

        return productOrderCode;
    }


    /**
     * 导入导出批次号
     * @param prefix
     * @return
     */
    public static String genImportExportCode(String prefix){
        int codeLength = 6;

        String batchNo = "";

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String key = ctx.getCompanyId() + prefix + CODE_TYPE_IMPORT_EXPORT_CODE + dateStr;

        long newValue = getNewValueByKey(key,1,1,TimeUnit.DAYS,"Import Export BatchNo");

        batchNo = prefix + dateStr + StringUtils.leftPad(newValue + "",codeLength,"0");

        return batchNo;
    }

    /**
     * 建筑编码
     * @return
     */
    public static String genBuildingCode(){
        int codeLength = 7;

        String buildingCode = "";

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String key = ctx.getCompanyId() + CODE_TYPE_BUILDING_CODE + dateStr;

        long newValue = getNewValueByKey(key,1,1,TimeUnit.DAYS,"Building Code");

        buildingCode = CODE_PREFIX_BUILDING_CODE + dateStr + StringUtils.leftPad(newValue + "",codeLength,"0");

        return buildingCode;
    }

    /**
     * t_synchrodata表的code
     * @return
     */
    public static String genSynchrodataCode(){
        int codeLength = 7;

        String result = "";

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String key = ctx.getCompanyId() + CODE_TYPE_SYNCHRODATA_CODE + dateStr;

        long newValue = getNewValueByKey(key,1,1,TimeUnit.DAYS,"Synchrodata Code");

        result = dateStr + StringUtils.leftPad(newValue + "",codeLength,"0");

        return result;
    }

    /**
     * 生成公司编码
     * @return
     */
    public static String genCompanyCode(){
        int codeLength = 4;

        String companyCode = "";

        String key = CODE_TYPE_COMPANY_CODE;

        long newValue = getNewValueByKey(key,1,1,TimeUnit.DAYS,"Company Code");

        companyCode = StringUtils.leftPad(newValue + "",codeLength,"0");

        return companyCode;
    }


    public static String genProjectCode(){
        int codeLength = 2;

        String code = "";

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String key =  ctx.getCompanyId() + CODE_TYPE_PROJECT_CODE;

        long newValue = getNewValueByKey(key,1,1,TimeUnit.DAYS,"Project Code");

        code = StringUtils.leftPad(newValue + "",codeLength,"0");

        return code;
    }

    public static String genDepartmentCode(){
        int codeLength = 2;

        String code = "";

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String key =  ctx.getCompanyId() + CODE_TYPE_DEPARTMENT_CODE;

        long newValue = getNewValueByKey(key,1,1,TimeUnit.DAYS,"Department Code");

        code = StringUtils.leftPad(newValue + "",codeLength,"0");

        return code;
    }

    public static String genRoleCode(){
        int codeLength = 2;

        String code = "";

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String key =  ctx.getCompanyId() + CODE_TYPE_ROLE_CODE;

        long newValue = getNewValueByKey(key,1,1,TimeUnit.DAYS,"Role Code");

        code = StringUtils.leftPad(newValue + "",codeLength,"0");

        return code;
    }

    public static String genUserCode(){
        int codeLength = 6;

        String userCode = "";


        WyBusinessContext ctx = WyBusinessContext.getContext();

        String key =  ctx.getCompanyId() + CODE_TYPE_USER_CODE;

        long newValue = getNewValueByKey(key,1,1,TimeUnit.DAYS,"User Code");

        userCode = StringUtils.leftPad(newValue + "",codeLength,"0");

        return userCode;
    }


    /**
     * 原子自增方法，失败时默认重试5次
     * @param key redis的key
     * @param value 自增值
     * @param timeout 超时阀
     * @param timeUnit 超时阀单位
     * @param desc 用于打印日志的描述
     * @return 自增value值后的值
     */
    private static long getNewValueByKey(String key,long value,long timeout,TimeUnit timeUnit,String desc){
        SpringRedisTools springRedisTools = SpringContextHolder.getBean("redisDataOperator");

        long newValue = 0;

        int tryCount = 5;

        do {
            newValue = springRedisTools.incrementForLong(key, value, timeout, timeUnit);
            if(newValue < 1 && tryCount > 0){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                tryCount --;
            }else{
                break;
            }
        }while (true);

        if(newValue < 1){
            throw new ECPBusinessException("生成["+desc+"]失败");
        }

        return newValue;
    }
}
