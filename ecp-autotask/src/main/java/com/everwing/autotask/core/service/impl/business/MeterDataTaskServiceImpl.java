package com.everwing.autotask.core.service.impl.business;

import com.everwing.autotask.core.dao.*;
import com.everwing.autotask.core.service.business.MeterDataTaskService;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.MeterDataEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.meterrelation.TcMeterRelation;
import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author DELL shiny
 * @create 2018/6/5
 */
@Log4j2
@Service
public class MeterDataTaskServiceImpl implements MeterDataTaskService {

    @Autowired
    private TcReadingScheduleMapper tcReadingScheduleMapper;

    @Autowired
    private TcReadingTaskMapper tcReadingTaskMapper;

    @Autowired
    private TcMeterDataMapper tcMeterDataMapper;

    @Autowired
    private TcMeterRelationMapper tcMeterRelationMapper;

    @Autowired
    private TcWaterMeterMapper tcWaterMeterMapper;

    @Autowired
    private TcElectMeterMapper tcElectMeterMapper;

    @Autowired
    private TSysProjectMapper tSysProjectMapper;

    @Override
    public void scanSchedule(String companyId) {
        TcReadingSchedule param  = new TcReadingSchedule();
        List<TcReadingSchedule> schedules = tcReadingScheduleMapper.findUsingSchedule(param);
        if(CollectionUtils.isEmpty(schedules)){
            return ;
        }

        Map<String,Object> paramMap = new HashMap<String,Object>();
        for(TcReadingSchedule schedule : schedules){
            paramMap.clear();
            if(new Date().compareTo(schedule.getReadingEndTime()) > 0){
                schedule.setStatus(2);
                this.tcReadingScheduleMapper.modify(schedule);
                return ;
            }

            //1. 找出当前库内,生效的计划下,已经完成的任务. 对完成的任务进行复制,生成新的任务 (此时已完成),未生成新的任务
            List<TcReadingTask> tasks = tcReadingTaskMapper.findCanCopyTasks();

            if(CollectionUtils.isNotEmpty(tasks)){
                List<TcMeterData> newDatas = new ArrayList<TcMeterData>();
                List<TcMeterRelation> relations = new ArrayList<TcMeterRelation>();
                for(TcReadingTask task : tasks){
                    //首先需要判断,该任务是否已经生成了下个周期的任务
                    TcReadingTask nextTask = tcReadingTaskMapper.getNextTaskByCurrentId(task.getId());
                    if(null != nextTask){
                        //已经生成了下个月的任务,则此处不再生成.
                        continue;
                    }

                    newDatas.clear();
                    relations.clear();
                    String taskId = CommonUtils.getUUID();
                    String lastTaskId = task.getId();
                    try {
                        if(CommonUtils.isNotEmpty(task.getMeterDatas())){
                            for(TcMeterData data : task.getMeterDatas()){
                                if(data.getIsUsed() == 1) {

                                    //判断当前数据是否被重抄
                                    TcMeterData complaintData = tcMeterDataMapper.findComplatintData(data);
                                    if(null != complaintData){
                                        data = complaintData;
                                    }else{
                                        continue;		//未被投诉,即已经无效
                                    }
                                }

                                //判断该表在本次周期是否已经生成记录,若已经生成,则不再生成
                                List<TcMeterData> existsData = tcMeterDataMapper.findExistsData(data);
                                if(CommonUtils.isNotEmpty(existsData)){
                                    TcMeterData d = existsData.get(0);
                                    d.setLastCommonReading(data.getCommonReading());
                                    d.setLastPeakReading(data.getPeakReading());
                                    d.setLastTotalReading(data.getTotalReading());
                                    d.setLastVallyReading(data.getVallyReading());
                                    d.setCommonCount(0.0);
                                    d.setPeakCount(0.0);
                                    d.setUseCount(0.0);
                                    d.setVallyCount(0.0);
                                    this.tcMeterDataMapper.modify(d);
                                    continue; //本月已经生成此表的数据,该任务不再生成
                                }

                                TcMeterData newData = new TcMeterData();
                                BeanUtils.copyProperties(newData, data);
                                newData.setReadingTime(null);
                                newData.setId(CommonUtils.getUUID());
                                newData.setBeforeTaskId(lastTaskId);
                                newData.setTaskId(taskId);
                                newData.setLastCommonReading(data.getCommonReading());
                                newData.setLastPeakReading(data.getPeakReading());
                                newData.setLastTotalReading(data.getTotalReading());
                                newData.setLastVallyReading(data.getVallyReading());
                                newData.setTotalReading(0.0);
                                newData.setCommonReading(0.0);
                                newData.setPeakReading(0.0);
                                newData.setVallyReading(0.0);
                                newData.setCreateId("system");
                                newData.setModifyId("system");
                                newData.setCreateTime(new Date());
                                newData.setAuditStatus(0);
                                newData.setModifyTime(new Date());
                                newData.setIsReplaced(0);
                                newData.setRemark(null);
                                newData.setCommonCount(0.0);
                                newData.setPeakCount(0.0);
                                newData.setUseCount(0.0);
                                newData.setVallyCount(0.0);
                                newDatas.add(newData);
                            }
                            if(CollectionUtils.isNotEmpty(newDatas))
                                tcMeterDataMapper.batchAdd(newDatas);
                        }

                        TcReadingTask newTask  = new TcReadingTask();
                        BeanUtils.copyProperties(newTask, task);
                        newTask.setId(taskId);

                        //不再以上个月的抄表位置来复制了.直接按照绑定的表来生成新的抄表位置
                        if(!CollectionUtils.isEmpty(newDatas)){
                            paramMap.clear();
                            paramMap.put("meterType", task.getMeterType());
                            paramMap.put("datas", newDatas);
                            List<String> positions = tcMeterRelationMapper.findPositionByDatas(paramMap);
                            if(CommonUtils.isNotEmpty(positions)){
                                for(String position : positions){
                                    TcMeterRelation newRelation = new TcMeterRelation();
                                    newRelation.setBuildingCode(position);
                                    newRelation.setId(CommonUtils.getUUID());
                                    newRelation.setRelationId(taskId);
                                    newRelation.setType(3);
                                    relations.add(newRelation);
                                }
                                if(CollectionUtils.isNotEmpty(relations))
                                    tcMeterRelationMapper.batchAdd(relations);
                            }
                        }

                        newTask.setStartTime(CommonUtils.changeMonths(task.getStartTime(), schedule.getExecFreq()));
                        newTask.setEndTime(CommonUtils.changeMonths(task.getEndTime(), schedule.getExecFreq()));
                        newTask.setAuditStartTime(CommonUtils.changeMonths(task.getAuditStartTime(), schedule.getExecFreq()));
                        newTask.setAuditEndTime(CommonUtils.changeMonths(task.getAuditEndTime(), schedule.getExecFreq()));
                        newTask.setAuditStatus(0); //待审核状态
                        newTask.setStatus(1); //进行中状态
                        newTask.setCreateId("system");
                        newTask.setCreateTime(new Date());
                        newTask.setModifyId("system");
                        newTask.setModifyTime(new Date());
                        newTask.setLastTaskId(task.getId());
                        newTask.setTotalMeterCount(newDatas.size());
                        newTask.setRemainMeterCount(newDatas.size());
                        newTask.setCompleteMeterCount(0);
                        tcReadingTaskMapper.insert(newTask);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(CommonUtils.log("任务自动生成出现异常:  任务: " + task.toString() + " \n, 异常原因 : " + e.getMessage()));
                        continue;
                    }
                }
            }

            //0. 本月执行计划: 找出本月的总表数,更新到schedule内 TODO
            paramMap.put("id", schedule.getId());
            schedule.setTotalMeterCount(tcReadingScheduleMapper.getMeterCountByScheduleId(paramMap));
            this.tcReadingScheduleMapper.modify(schedule);

        }
    }

    @Override
    public void autoComplete(String companyId) {
        //停止过期的计划
        tcReadingScheduleMapper.stopFailedSchedule();

        //将当前所有的进入任务开始时间,且状态为0新建的任务,改为执行中
        tcReadingTaskMapper.startInitTasks();

        //TODO 推送今天开始的任务给负责人
//		pushTask2Staff();

        //找出有效计划下,当前时间已超过end_time的任务 (此时正在执行中)
        List<TcReadingTask> tasks = tcReadingTaskMapper.findCanCompleteTasks();

        List<Map<String, String>> waterMeterMList = new ArrayList<Map<String, String>>(); //存储水表M表

        List<Map<String, String>> electMeterMList = new ArrayList<Map<String, String>>(); //存储M电表

        //存储taskId
        List<String> taskIdList = new ArrayList<String>();

        if (CollectionUtils.isNotEmpty(tasks)) {
            //对计划下的抄表数据进行统计
            TcReadingSchedule schedule = null;
            String projectId = null;
            for (TcReadingTask task : tasks) {

                schedule = tcReadingScheduleMapper.getScheduleById(task.getScheduleId());
                projectId = (null == schedule) ? "" : schedule.getProjectId();
                taskIdList.add(task.getId());
                int completeCount = 0;
                if (CollectionUtils.isNotEmpty(task.getMeterDatas())) {
                    for (TcMeterData data : task.getMeterDatas()) {
                        if (null == data) continue;
                        //对于M等级的表的子表已经计算过用量的过滤掉
                        String metercode = data.getMeterCode();
                        //根据task的metertype类型查找表
                        Integer meterType = task.getMeterType();
                        if (meterType == 0) {
                            //查找等级为M的水表  这里主要针对，如果以前产生了下一次M级表的Task,
                            TcWaterMeter tcWaterMeterM = tcWaterMeterMapper.getWaterMeterByCodeAndM(metercode, projectId);
                            //然后在聚合将子表的总用量汇总写入到总表当中
                            if (CommonUtils.isNotEmpty(tcWaterMeterM)) {
                                Map map = new HashMap<String, String>();
                                map.put("code", tcWaterMeterM.getCode());
                                map.put("projectId", projectId);
                                map.put("meterName", tcWaterMeterM.getWaterMeterName());
                                if (!waterMeterMList.contains(map)) {
                                    waterMeterMList.add(map);
                                }
                                //如果是M级表就跳过
                                continue;
//
                            }
                            //如果第一次抄表，未产生和M级表关联的抄表任务，这里需要从C级表开始查找,
                            TcWaterMeter tcWaterMeter = tcWaterMeterMapper.findMByCCodeAndProjectId(data.getMeterCode(), projectId);
                            if (CommonUtils.isNotEmpty(tcWaterMeter)) {
                                Map map = new HashMap<String, String>();
                                map.put("code", tcWaterMeter.getCode());
                                map.put("projectId", projectId);
                                map.put("meterName", tcWaterMeter.getWaterMeterName());
                                if (!waterMeterMList.contains(map)) {
                                    waterMeterMList.add(map);
                                }
                            }

                        } else if (meterType == 1) { //查找等级为C的电表  //电表
                            //查找等级为M的水表  这里主要针对，如果以前产生了下一次M级表的Task,
                            ElectMeter electMeterM = tcElectMeterMapper.getElectMeterByCodeAndM(metercode, projectId);
                            //然后在聚合将子表的总用量汇总写入到总表当中
                            if (CommonUtils.isNotEmpty(electMeterM)) {
                                Map map = new HashMap<String, String>();
                                map.put("code", electMeterM.getCode());
                                map.put("projectId", projectId);
                                map.put("meterName", electMeterM.getElectricitymetername());
                                if (!electMeterMList.contains(map)) {
                                    electMeterMList.add(map);
                                }

                                //如果是M级表就跳过
                                continue;
//
                            }
                            //如果第一次抄表，未产生和M级表关联的抄表任务，这里需要从C级表开始查找,
                            ElectMeter electMeter = tcElectMeterMapper.findMByCCodeAndProjectId(data.getMeterCode(), projectId);
                            if (CommonUtils.isNotEmpty(electMeter)) {
                                Map map = new HashMap<String, String>();
                                map.put("code", electMeter.getCode());
                                map.put("projectId", projectId);
                                map.put("meterName", electMeter.getElectricitymetername());
                                if (!electMeterMList.contains(map)) {
                                    electMeterMList.add(map);
                                }
                            }
                        }
                        if (data.getIsUsed() == 1) continue; //该条数据无效,不计算
                        calculationDosage(data, projectId);
                        completeCount++;
                    }
                }


                task.setCompleteMeterCount(completeCount);
                task.setRemainMeterCount(task.getTotalMeterCount() - task.getCompleteMeterCount());
                task.setStatus(2);    //任务完成
                tcReadingTaskMapper.update(task);
            }
        }


        //所有task全部自动完成之后
        //找出总表的meterdata reading time is null
        //存在  则更新useCount 为所有子表的总用量
        //不存在 则插入 再更新
        //聚合
        //插入一条新的reading time is null的 meterdata
        summaryMMeter(waterMeterMList, electMeterMList, taskIdList);
    }

    private void summaryMMeter(List<Map<String,String>> waterMeterMList,List<Map<String,String>> electMeterMList,List<String> taskIdList){
        //水表M级别表的聚合
        if(waterMeterMList.size()>0 && taskIdList.size()>0){
            for(Map<String,String> map : waterMeterMList){
                String code = map.get("code");
                String projectId = map.get("projectId");
                //查找这些子表
                List<String> codes = tcWaterMeterMapper.findByparentCodeAndProjectId(code, projectId);
                if(CollectionUtils.isEmpty(codes)){
                    log.info("M级表水表编号["+code+"]没有对应的子级水表表C等级表的抄表任务!");
                    continue;
                }
                //查询这些子表的meterData数据 这些子表都是已经自动完成了的。
                //找出这些子表自动完成后的的读数(自动完成时计算用量)

                List<TcMeterData> listMeterData = findSumCMeter(0,projectId,codes,taskIdList);
                Double useCount =0.00;
                String taskIdC="";
                for(TcMeterData tcMeterData : listMeterData){
                    if(CommonUtils.null2Double(tcMeterData.getUseCount())>0){
                        useCount = useCount+CommonUtils.null2Double(tcMeterData.getUseCount());
                    }
                    taskIdC= tcMeterData.getTaskId();
                }
                //每一条MeterData数据都必须很task关联
                //查找M级表的Task,诺不存在，则存放到其任意一条其子级表也就是C级的任务里
//				 //诺存在，找出其MeterData数据
                TcMeterData  meterDataM = findParentMMeter(0,code,projectId,taskIdList);
                if(CommonUtils.isEmpty(meterDataM)){
                    //新建
                    createMeterData(0,useCount,0.0,0.0,0.0,map,taskIdC);
                }else{//修改
                    modifyMeterData(meterDataM,useCount,0.0,0.0,0.0);
                }

            }
        }

        if(electMeterMList.size()>0 && taskIdList.size()>0){
            for(Map<String,String> map : electMeterMList){
                String code = map.get("code");
                String projectId = map.get("projectId");
                //查找这些子表
                List<String> codes = this.tcElectMeterMapper.findByparentCodeAndProjectId(code, projectId);
                if(CollectionUtils.isEmpty(codes)){
                    log.info("M级表电表编号["+code+"]没有对应的子级电表表C等级表的抄表任务!");
                    continue;
                }
                //查询这些子表的meterData数据 这些子表都是已经自动完成了的。
                //找出这些子表自动完成后的的读数(自动完成时计算用量)

                List<TcMeterData> listMeterData = findSumCMeter(1,projectId,codes,taskIdList);
                Double useCount =0.00;
                Double peakCount =0.00;
                Double vallCount =0.00;
                Double commCount=0.00;
                String taskIdC="";
                for(TcMeterData tcMeterData : listMeterData){
                    if(CommonUtils.null2Double(tcMeterData.getUseCount())>0){
                        useCount = useCount+CommonUtils.null2Double(tcMeterData.getUseCount());
                    }
                    if(CommonUtils.null2Double(tcMeterData.getPeakCount())>0){
                        peakCount = peakCount+CommonUtils.null2Double(tcMeterData.getPeakCount());
                    }
                    if(CommonUtils.null2Double(tcMeterData.getVallyCount())>0){
                        vallCount = vallCount+CommonUtils.null2Double(tcMeterData.getVallyCount());
                    }
                    if(CommonUtils.null2Double(tcMeterData.getCommonCount())>0){
                        commCount = commCount+CommonUtils.null2Double(tcMeterData.getCommonCount());
                    }
                    taskIdC= tcMeterData.getTaskId();
                }
                //每一条MeterData数据都必须很task关联
                //查找M级表的Task,诺不存在，则存放到其任意一条其子级表也就是C级的任务里
//				 //诺存在，找出其MeterData数据
                TcMeterData  meterDataM = findParentMMeter(1,code,projectId,taskIdList);
                if(CommonUtils.isEmpty(meterDataM)){
                    //新建
                    createMeterData(1,useCount,peakCount,vallCount,commCount,map,taskIdC);
                }else{//修改
                    modifyMeterData(meterDataM,useCount,peakCount,vallCount,commCount);
                }
            }
        }
    }

    private void modifyMeterData(TcMeterData data,Double totalUseCount,Double peakUseCount,
                                 Double vallUseCount,Double commUseCount){
        data.setUseCount(totalUseCount);
        data.setReadingTime(new Date());
        data.setTotalReading(totalUseCount);
        data.setLastTotalReading(0.00);
        data.setPeakCount(peakUseCount);
        data.setPeakReading(peakUseCount);
        data.setLastPeakReading(0.00);
        data.setVallyCount(vallUseCount);
        data.setVallyReading(vallUseCount);
        data.setLastVallyReading(0.00);
        data.setCommonCount(commUseCount);
        data.setCommonReading(commUseCount);
        data.setLastCommonReading(0.00);
        data.setModifyId("system");
        data.setModifyTime(new Date());
        data.setRemark("M级表,其用量等于其各个子表的用量之和,本次读数等于本次用量，上次读数等于0");
        this.tcMeterDataMapper.modify(data);
    }

    private void createMeterData(Integer meterType,Double totalUseCount,Double peakUseCount,
                                 Double vallUseCount,Double commUseCount,Map<String,String> map,
                                 String taskId){
        TcMeterData data = new TcMeterData();
        data.setUseCount(totalUseCount);
        data.setTotalReading(totalUseCount);
        data.setLastTotalReading(0.00);
        data.setPeakCount(peakUseCount);
        data.setPeakReading(peakUseCount);
        data.setLastPeakReading(0.00);
        data.setVallyCount(vallUseCount);
        data.setVallyReading(vallUseCount);
        data.setLastVallyReading(0.00);
        data.setCommonCount(commUseCount);
        data.setCommonReading(commUseCount);
        data.setLastCommonReading(0.00);
        data.setMeterType(meterType);
        data.setTaskId(taskId);
        data.setMeterCode(map.get("code"));
        data.setMeterName(map.get("meterName"));
        data.setProjectId(map.get("projectId"));
        TSysProject tSysProject = tSysProjectMapper.findByCode(map.get("code"));
        if(CommonUtils.isNotEmpty(tSysProject)){
            data.setProjectName(tSysProject.getName());
        }
        data.setRemark("M级表,其用量等于其各个子表的用量之和,本次读数等于本次用量，上次读数等于0");
        completeFullInformation(data);
        List<TcMeterData> listMeterData = new ArrayList<TcMeterData>();
        listMeterData.add(data);
        tcMeterDataMapper.batchAdd(listMeterData);
    }

    private void completeFullInformation(TcMeterData data){
        data.setCircleCorrection(0.00);
        data.setPeakCircleCorrection(0.00);
        data.setValleyCircleCorrection(0.00);
        data.setAverageCircleCorrection(0.00);
        data.setCorrection(0.00);
        data.setPeakCorrection(0.00);
        data.setValleyCorrection(0.00);
        data.setAverageCorrection(0.00);
        data.setAuditStatus(0);
        data.setReadingPersonName("system");
        data.setReadingPersonId("system");
        data.setReadingTime(new Date());
        data.setIsUsed(0);
        data.setCreateId("system");
        data.setCreateTime(new Date());
    }

    /**
     * 查找M级表
     */
    private TcMeterData findParentMMeter(Integer meterType,String code,String projectId,List<String> taskIdList){
        Map<String,Object> mParam = new HashMap<String,Object>();
        mParam.put("meterCode", code);
        mParam.put("projectId", projectId);
        mParam.put("taskIds", taskIdList);
        mParam.put("meterType", meterType);
        TcMeterData  meterDataM =  tcMeterDataMapper.findMByMeterCodeAndProIdTasks(mParam);
        return meterDataM;
    }

    private List<TcMeterData> findSumCMeter(Integer meterType,String projectId,List<String> codes,List<String> taskIdList){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("projectId", projectId);
        paramMap.put("meterType", meterType);
        paramMap.put("taskIds", taskIdList);
        paramMap.put("codes", codes);
        //找出这些子表自动完成后的的读数(自动完成时计算用量)
        List<TcMeterData> listMeterData = tcMeterDataMapper.findByTaskIdsAndCodes(paramMap);
        return listMeterData;
    }

    private void calculationDosage(TcMeterData data,String projectId){
        StringBuffer remarkBuffer = new StringBuffer();
        double rate = 1;
        if(data.getMeterType() == 1){
            ElectMeter meter = tcElectMeterMapper.getElectMeterByCode(data.getMeterCode(), projectId);
            if(meter!=null) {
                rate = CommonUtils.isEmpty(meter.getRate()) ? 1 : meter.getRate();
                double lastPeakReading = CommonUtils.null2Double(data.getLastPeakReading());
                double peakReading = CommonUtils.null2Double(data.getPeakReading());

                if (peakReading == 0) {
                    peakReading = lastPeakReading;
                    remarkBuffer.append("峰值,");
                }
                if (peakReading >= lastPeakReading) {    //只计算本月读数大于上月读数的
                    data.setPeakCount((peakReading - lastPeakReading) * rate);
                } else {
                    data.setRemark("峰值读数: 本月少于上月读数, 处于异常状态. ");
                    data.setPeakCount(0.0);
                    data.setExStatus(MeterDataEnum.EX_STATUS_ERROR.getIntValue());
                    this.tcMeterDataMapper.modify(data);
                    return;
                }


                double vallyReading = CommonUtils.null2Double(data.getVallyReading());
                double lastVallyReading = CommonUtils.null2Double(data.getLastVallyReading());

                if (vallyReading == 0) {
                    vallyReading = lastVallyReading;
                    remarkBuffer.append("谷值,");
                }
                if (vallyReading >= lastVallyReading) {
                    data.setVallyCount((vallyReading - lastVallyReading) * rate);
                } else {
                    data.setRemark("谷值读数: 本月少于上月读数, 处于异常状态. ");
                    data.setVallyCount(0.0);
                    data.setExStatus(MeterDataEnum.EX_STATUS_ERROR.getIntValue());
                    this.tcMeterDataMapper.modify(data);
                    return;
                }


                double commonReading = CommonUtils.null2Double(data.getCommonReading());
                double lastCommonReading = CommonUtils.null2Double(data.getLastCommonReading());

                if (commonReading == 0) {
                    commonReading = lastCommonReading;
                    remarkBuffer.append("平值,");
                }
                if (commonReading >= lastCommonReading) {
                    data.setCommonCount((commonReading - lastCommonReading) * rate);
                } else {
                    data.setRemark("平值读数: 本月少于上月读数, 处于异常状态. ");
                    data.setCommonCount(0.0);
                    data.setExStatus(MeterDataEnum.EX_STATUS_ERROR.getIntValue());
                    this.tcMeterDataMapper.modify(data);
                    return;
                }
            }
        }else{
            //水表
            TcWaterMeter meter = this.tcWaterMeterMapper.getWaterMeterByCode(data.getMeterCode(),projectId);
            if(meter!=null) {
                rate = (null == meter) ? 1 : meter.getRate();
            }
        }

        double totalReading = CommonUtils.null2Double(data.getTotalReading());
        double lastTotalReading = CommonUtils.null2Double(data.getLastTotalReading());

        if(totalReading == 0){
            totalReading = lastTotalReading;
            remarkBuffer.append("总读数,");
        }
        if(totalReading >= lastTotalReading){
            data.setUseCount((totalReading - lastTotalReading) * rate);
        }else{
            data.setRemark("总读数: 本月少于上月读数, 处于异常状态. ");
            data.setUseCount(0.0);
            data.setExStatus(MeterDataEnum.EX_STATUS_ERROR.getIntValue());
            this.tcMeterDataMapper.modify(data);
            return;
        }
        if(remarkBuffer.length() > 0){
            data.setRemark(remarkBuffer.deleteCharAt(remarkBuffer.lastIndexOf(",")).append("为空,默认为上月读数.").toString());
            if(null == data.getReadingTime()){
                data.setReadingTime(new Date());
            }
            remarkBuffer.delete(0, remarkBuffer.length());
        }

        modifyLastData(data);
        data.setModifyId("system");
        data.setModifyTime(new Date());
        data.setExStatus(MeterDataEnum.EX_STATUS_COMMON.getIntValue());
        tcMeterDataMapper.modify(data);

    }

    private void modifyLastData(TcMeterData data){
        //查找当前完成的表  在下个周期内的任务是否产生 (针对 计划修改 , 修改保存后,会产生下个月的新任务以及抄表数据,但是数据都无last_*_reading , 需要从本月填入)
        TcMeterData nextData = tcMeterDataMapper.findNextData(data);
        if(null != nextData){
            nextData.setLastCommonReading(data.getCommonReading());
            nextData.setLastPeakReading(data.getPeakReading());
            nextData.setLastTotalReading(data.getTotalReading());
            nextData.setLastVallyReading(data.getVallyReading());
            tcMeterDataMapper.modify(nextData);
        }
    }
}
