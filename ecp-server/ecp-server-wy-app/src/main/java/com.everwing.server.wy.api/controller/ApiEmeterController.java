package com.everwing.server.wy.api.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.wy.api.business.meterdata.TcMeterDataApi;
import com.everwing.coreservice.wy.api.business.readingtask.TcReadingTaskApi;
import com.everwing.server.wy.api.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物业app电表接口
 * Created by zhugeruifei on 17/8/11.
 */
@RestController
@RequestMapping("{version}/emeter")
public class ApiEmeterController {

    private static final Logger logger= LogManager.getLogger(ApiEmeterController.class);

    private static final String pattern = "yyyy-MM-dd HH:mm:ss";

    private static final String pattern_year = "yyyy";

    private static final String pattern_month = "MM";

    @Autowired
    private TcReadingTaskApi tcReadingTaskApi;

    @Autowired
    private TcMeterDataApi tcMeterDataApi;

    /**
     * 根据用户查询当月所有电表抄表任务以及上月抄表数据
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("task/current")
    @ApiVersion(1.0)
    public BaseApiVo<List<MeterTaskVo<MeterDataVo>>> queryTaskByCurrMonth(HttpServletRequest request, String pageNo, String pageSize) {
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        String accountId = account.getAccountId();
        String companyId = account.getCompanyId();
        int meterType = 1;
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }

        logger.debug("根据用户查询当月所有电表抄表任务以及上月抄表数据," +
                        "请求参数:accountId={},companyId={},pageNo={},pageSize={},meterType={}"
                ,new Object[]{accountId,companyId,pageNo,pageSize,meterType});

        RemoteModelResult<List<TcReadingTask>> remoteModelResult = tcReadingTaskApi.queryCurrentByAccountId(companyId,accountId,meterType,Integer.parseInt(pageNo),Integer.parseInt(pageSize));
        logger.debug("根据用户查询当月所有电表抄表任务,远程调用返回数据：{}",remoteModelResult);

        if(remoteModelResult.isSuccess()) {
            final FastDateFormat df = FastDateFormat.getInstance(pattern);
            List<TcReadingTask> taskList = remoteModelResult.getModel();
            List<String> taskIds = new ArrayList<String>();
            Map<String,List<MeterDataVo>> idsMap = new HashMap<String,List<MeterDataVo>>();
            List<MeterTaskVo> meterTaskVoList = new ArrayList<MeterTaskVo>();
            MeterTaskVo meterTaskVo = null;

            for (TcReadingTask readingTask : taskList) {

                taskIds.add(readingTask.getId());
                idsMap.put(readingTask.getId(),new ArrayList<MeterDataVo>());

                meterTaskVo = new MeterTaskVo();
                meterTaskVo.setTaskId(readingTask.getId());
                meterTaskVo.setCreateTime(df.format(readingTask.getCreateTime()));
                meterTaskVo.setReadingPersonName(account.getAccountName());
                meterTaskVo.setStatus(String.valueOf(readingTask.getStatus()));
                meterTaskVo.setTaskContent(readingTask.getTaskContent());
                if(readingTask.getModifyTime() != null) {
                    meterTaskVo.setUpdateTime(df.format(readingTask.getModifyTime()));
                }
                if(readingTask.getStartTime() != null) {
                    meterTaskVo.setStartTime(df.format(readingTask.getStartTime()));
                }
                if(readingTask.getEndTime() != null) {
                    meterTaskVo.setEndTime(df.format(readingTask.getEndTime()));
                }
                meterTaskVoList.add(meterTaskVo);
            }

            RemoteModelResult<List<TcMeterData>> remoteModelResult2 = tcMeterDataApi.queryByTaskIds(companyId,taskIds,meterType);
            logger.debug("根据taskId集合查询对应的电表数据，远程调用返回数据：{}",remoteModelResult2);
            if(remoteModelResult2.isSuccess()) {
                List<TcMeterData> meterDataList = remoteModelResult2.getModel();
                EmeterDataVo emeterDataVo;
                for (TcMeterData meterData : meterDataList) {
                    emeterDataVo = new EmeterDataVo();
                    emeterDataVo.setMeterName(meterData.getMeterName());
                    emeterDataVo.setMeterCode(meterData.getMeterCode());
                    emeterDataVo.setLastMonthData(String.valueOf(meterData.getLastTotalReading()));
                    emeterDataVo.setLastCommonReading(String.valueOf(meterData.getLastCommonReading()));
                    emeterDataVo.setLastPeakReading(String.valueOf(meterData.getLastPeakReading()));
                    emeterDataVo.setLastVallyReading(String.valueOf(meterData.getLastVallyReading()));
                    idsMap.get(meterData.getTaskId()).add(emeterDataVo);
                }

                for (MeterTaskVo taskVo : meterTaskVoList) {
                    taskVo.setMeterDataVoList(idsMap.get(taskVo.getTaskId()));
                }

            }else {
                logger.debug("根据taskId集合查询对应的电表抄表数据，查询失败!");
            }
            returnApiVo.setData(meterTaskVoList);
        }else{
            logger.debug("根据用户查询当月所有电表抄表任务,查询失败!");
        }
        returnApiVo.setCode(remoteModelResult.getCode());
        returnApiVo.setMessage(remoteModelResult.getMsg());
        logger.debug("根据用户查询当月所有电表抄表任务以及上月抄表数据，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }

    /**
     * 查询电表历史任务(除当月的电表任务)
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("task/history")
    @ApiVersion(1.0)
    public BaseApiVo<List<MeterTaskVo>> queryHistoryTask(HttpServletRequest request, String pageNo, String pageSize) {
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        String accountId = account.getAccountId();
        String companyId = account.getCompanyId();
        int meterType = 1;
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }

        logger.debug("根据用户查询电表历史任务数据," +
                        "请求参数:accountId={},companyId={},pageNo={},pageSize={},meterType={}"
                ,new Object[]{accountId,companyId,pageNo,pageSize,meterType});

        RemoteModelResult<List<TcReadingTask>> remoteModelResult = tcReadingTaskApi.queryHistoryByAccountId(companyId,accountId,meterType,Integer.parseInt(pageNo),Integer.parseInt(pageSize));
        logger.debug("根据用户查询电表历史任务数据,远程调用返回数据：{}",remoteModelResult);

        if(remoteModelResult.isSuccess()) {
            final FastDateFormat df = FastDateFormat.getInstance(pattern);

            List<TcReadingTask> taskList = remoteModelResult.getModel();
            List<String> taskIds = new ArrayList<String>();
            Map<String,List<MeterDataVo>> idsMap = new HashMap<String,List<MeterDataVo>>();
            List<MeterTaskVo> meterTaskVoList = new ArrayList<MeterTaskVo>();
            MeterTaskVo meterTaskVo = null;
            for (TcReadingTask readingTask : taskList) {

                taskIds.add(readingTask.getId());
                idsMap.put(readingTask.getId(),new ArrayList<MeterDataVo>());

                meterTaskVo = new MeterTaskVo();
                meterTaskVo.setTaskId(readingTask.getId());
                meterTaskVo.setCreateTime(df.format(readingTask.getCreateTime()));
                meterTaskVo.setReadingPersonName(account.getAccountName());
                meterTaskVo.setStatus(String.valueOf(readingTask.getStatus()));
                meterTaskVo.setTaskContent(readingTask.getTaskContent());
                if(readingTask.getModifyTime() != null) {
                    meterTaskVo.setUpdateTime(df.format(readingTask.getModifyTime()));
                }
                if(readingTask.getStartTime() != null) {
                    meterTaskVo.setStartTime(df.format(readingTask.getStartTime()));
                }
                if(readingTask.getEndTime() != null) {
                    meterTaskVo.setEndTime(df.format(readingTask.getEndTime()));
                }
                meterTaskVoList.add(meterTaskVo);
            }
            returnApiVo.setData(meterTaskVoList);
        }else{
            logger.debug("根据用户查询电表历史任务数据,查询失败!");
        }
        returnApiVo.setCode(remoteModelResult.getCode());
        returnApiVo.setMessage(remoteModelResult.getMsg());
        logger.debug("根据用户查询电表历史任务数据，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }

    /**
     * 根据任务ID查询对应的电表数据
     * @param taskId
     * @return
     */
    @PostMapping("task/dataByTaskId")
    @ApiVersion(1.0)
    public BaseApiVo<List<MeterDataVo>> queryByTaskId(HttpServletRequest request,String taskId) {
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        String accountId = account.getAccountId();
        String companyId = account.getCompanyId();
        int meterType = 1;
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }

        logger.debug("根据任务ID查询对应的电表数据," +
                "请求参数:accountId={},companyId={},taskId={}",new Object[]{accountId,companyId,taskId});

        RemoteModelResult<List<TcMeterData>> remoteModelResult = tcMeterDataApi.queryByTaskId(companyId,taskId,meterType);
        logger.debug("根据任务ID查询对应的电表数据,远程调用返回数据：{}",remoteModelResult);

        if (remoteModelResult.isSuccess()) {
            final FastDateFormat df = FastDateFormat.getInstance(pattern);
            List<TcMeterData> meterDataList = remoteModelResult.getModel();
            List<MeterDataVo> meterDataVoList = new ArrayList<MeterDataVo>();

            EmeterDataVo emeterDataVo;
            for (TcMeterData meterData : meterDataList) {
                emeterDataVo = new EmeterDataVo();
                emeterDataVo.setMeterName(meterData.getMeterName());
                emeterDataVo.setMeterCode(meterData.getMeterCode());
                emeterDataVo.setLastMonthData(String.valueOf(meterData.getLastTotalReading()));
                emeterDataVo.setLastCommonReading(String.valueOf(meterData.getLastCommonReading()));
                emeterDataVo.setLastPeakReading(String.valueOf(meterData.getLastPeakReading()));
                emeterDataVo.setLastVallyReading(String.valueOf(meterData.getLastVallyReading()));
                meterDataVoList.add(emeterDataVo);
            }
            returnApiVo.setData(meterDataVoList);
        }else{
            logger.debug("根据任务ID查询对应的电表数据,查询失败!");
        }
        returnApiVo.setCode(remoteModelResult.getCode());
        returnApiVo.setMessage(remoteModelResult.getMsg());
        logger.debug("根据任务ID查询对应的电表数据，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }

    /**
     * 根据电表描述查询对应的电表数据
     * @param description
     * @return
     */
    @PostMapping("info")
    @ApiVersion(1.0)
    public BaseApiVo<List<MeterDataVo>> queryByDescription(HttpServletRequest request,String description) {
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class); 
        String accountId = account.getAccountId();
        String companyId = account.getCompanyId();
        int meterType = 1;
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }

        logger.debug("根据电表描述查询对应的电表数据，" +
                        "请求参数:accountId={},companyId={},meterType={},description={}",
                new Object[]{accountId,companyId,meterType,description});

        RemoteModelResult<List<TcMeterData>> remoteModelResult = tcMeterDataApi.queryByDescription(companyId,description,meterType);
        logger.debug("根据电表描述查询对应的电表数据,远程调用返回数据：{}",remoteModelResult);

        if (remoteModelResult.isSuccess()) {
            List<TcMeterData> meterDataList = remoteModelResult.getModel();
            List<MeterDataVo> meterDataVoList = new ArrayList<MeterDataVo>();

            EmeterDataVo emeterDataVo;
            for (TcMeterData meterData : meterDataList) {
                emeterDataVo = new EmeterDataVo();
                emeterDataVo.setMeterName(meterData.getMeterName());
                emeterDataVo.setMeterCode(meterData.getMeterCode());
                emeterDataVo.setLastMonthData(String.valueOf(meterData.getLastTotalReading()));
                emeterDataVo.setLastCommonReading(String.valueOf(meterData.getLastCommonReading()));
                emeterDataVo.setLastPeakReading(String.valueOf(meterData.getLastPeakReading()));
                emeterDataVo.setLastVallyReading(String.valueOf(meterData.getLastVallyReading()));
                meterDataVoList.add(emeterDataVo);
            }
            returnApiVo.setData(meterDataVoList);
        }else{
            logger.debug("根据电表描述查询对应的电表数据,查询失败!");
        }
        returnApiVo.setCode(remoteModelResult.getCode());
        returnApiVo.setMessage(remoteModelResult.getMsg());
        logger.debug("根据电表描述查询对应的电表数据，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }

    /**
     * 根据年份查询对应的电表流水数据
     * @param meterCode
     * @param year
     * @return
     */
    @PostMapping("yearData")
    @ApiVersion(1.0)
    public BaseApiVo<List<MeterYearDataVo>> queryYearDate(HttpServletRequest request,String meterCode,String year) {
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        String accountId = account.getAccountId();
        String companyId = account.getCompanyId();
        int meterType = 1;
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }

        logger.debug("根据年份查询对应的电表流水数据，" +
                        "请求参数:accountId={},companyId={},meterType={},meterCode={},year={}",
                new Object[]{accountId,companyId,meterType,meterCode,year});

        RemoteModelResult<List<TcMeterData>> remoteModelResult = tcMeterDataApi.queryYearData(companyId,meterCode,meterType,year);
        logger.debug("根据年份查询对应的电表流水数据,远程调用返回数据：{}",remoteModelResult);

        if (remoteModelResult.isSuccess()) {
            List<TcMeterData> tcMeterDataList = remoteModelResult.getModel();
            final FastDateFormat df = FastDateFormat.getInstance(pattern_year);
            final FastDateFormat df1 = FastDateFormat.getInstance(pattern_month);

            List<MeterYearDataVo> meterYearDataVoList = new ArrayList<MeterYearDataVo>();
            MeterYearDataVo meterYearDataVo = null;

            for (TcMeterData meterData : tcMeterDataList) {
                meterYearDataVo = new MeterYearDataVo();
                meterYearDataVo.setMeterCode(meterData.getMeterCode());
                meterYearDataVo.setTotalReading(String.valueOf(meterData.getTotalReading()));
                meterYearDataVo.setCommonReading(String.valueOf(meterData.getCommonReading()));
                meterYearDataVo.setpeakReading(String.valueOf(meterData.getPeakReading()));
                meterYearDataVo.setVallyReading(String.valueOf(meterData.getVallyReading()));
                meterYearDataVo.setMonth(df.format(meterData.getCreateTime()));
                meterYearDataVo.setYear(df1.format(meterData.getCreateTime()));
                meterYearDataVoList.add(meterYearDataVo);
            }
            returnApiVo.setData(meterYearDataVoList);
        }else {
            logger.debug("根据年份查询对应的电表流水数据,查询失败!");
        }
        returnApiVo.setCode(remoteModelResult.getCode());
        returnApiVo.setMessage(remoteModelResult.getMsg());
        logger.debug("根据年份查询对应的电表流水数据，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }

    /**
     * 上传/修改电表抄表数据
     * @param taskId
     * @param meterCode
     * @param totalReading
     * @param fileId
     * @return
     */
    @PostMapping("modifyData")
    @ApiVersion(1.0)
    public BaseApiVo modifyData(HttpServletRequest request, String taskId, String meterCode,
                                String totalReading,String peakrReading,String vallyReading,String commonReading,String fileId) {
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        String accountId = account.getAccountId();
        String companyId = account.getCompanyId();
        int meterType = 0;
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }

        logger.debug("修改电表抄表数据，" +
                        "请求参数:accountId={},companyId={},meterType={},meterCode={},taskId={},totalReading={},fileId={}",
                new Object[]{accountId,companyId,meterType,meterCode,taskId,fileId});

        RemoteModelResult<Integer> remoteModelResult = tcMeterDataApi.modifyEMeterData(companyId,taskId,accountId,meterCode,
                totalReading,peakrReading,vallyReading,commonReading,fileId);
        logger.debug("修改电表抄表数据,远程调用返回数据：{}",remoteModelResult);

        if(!remoteModelResult.isSuccess()) {
            logger.debug("修改电表抄表数据失败!");
        }
        Map<String,String> map = new HashMap<String,String>();
        map.put("meterCode",meterCode);
        returnApiVo.setData(map);
        returnApiVo.setCode(remoteModelResult.getCode());
        returnApiVo.setMessage(remoteModelResult.getMsg());
        logger.debug("修改水表抄表数据，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }
}
