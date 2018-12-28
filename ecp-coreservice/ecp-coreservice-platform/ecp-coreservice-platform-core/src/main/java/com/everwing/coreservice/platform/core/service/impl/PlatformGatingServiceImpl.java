package com.everwing.coreservice.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.BuildingStruct;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.extra.AppPkgDto;
import com.everwing.coreservice.common.platform.entity.extra.GatingLog;
import com.everwing.coreservice.common.platform.entity.generated.MkjLog;
import com.everwing.coreservice.common.platform.service.PlatformGatingService;
import com.everwing.coreservice.platform.dao.mapper.extra.AppPkgExtraMapper;
import com.everwing.coreservice.platform.dao.mapper.extra.GatingLinPhoneMapper;
import com.everwing.coreservice.platform.dao.mapper.extra.GatingLogExtraMapper;
import com.everwing.coreservice.platform.dao.mapper.generated.MkjLogMapper;
import com.everwing.coreservice.platform.dao.mapper.generated.TcBuildingMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class PlatformGatingServiceImpl implements PlatformGatingService{

    private static final Logger logger= LogManager.getLogger(PlatformGatingServiceImpl.class);

    @Autowired
    TcBuildingMapper tcBuildingMapper;

    @Autowired
    private GatingLogExtraMapper gatingLogExtraMapper;

    @Autowired
    private GatingLinPhoneMapper gatingLinPhoneMapper;

    @Autowired
    private MkjLogMapper mkjLogMapper;

    @Autowired
    private AppPkgExtraMapper appPkgExtraMapper;

    @Value("${linphonePkgUrlPreffix}")
    private String linphonePkgUrlPreffix;

    @Override
    public LinphoneResult queryGatingListByMobile(String accountId, String mobile) {
        return new LinphoneResult(gatingLinPhoneMapper.selectListByMobile(mobile));
    }

    @Override
    public LinphoneResult queryBuildings(String companyId, String gatingId) {
        List<Map<String,String>> buildings=gatingLinPhoneMapper.selectBuildingStruct(gatingId);
        Iterator<Map<String,String>> buidingIterator=buildings.iterator();
        while (buidingIterator.hasNext()){//移除空元素
            Map<String,String> building=buidingIterator.next();
            if(building==null){
                buidingIterator.remove();
            }
        }
        if(buildings.size()>0) {
            BuildingStruct buildingStruct = new BuildingStruct();
            buildingStruct.setBuildings(buildings);
            if (buildings.size() > 0) {
                buildingStruct.setProjectId(buildings.get(0).get("projectId"));
            }
            buildingStruct.setCompanyId(companyId);
            return new LinphoneResult(buildingStruct);
        }else {
            return new LinphoneResult();
        }
    }

    @Override
    public RemoteModelResult insertLog(String logId,String companyId, String projectId, String toBuildingCode, String fromBuildingCode, String gatingCode, String gatingAccount, String type, String createTime) {
        GatingLog gatingLog=new GatingLog();
        gatingLog.setCompanyId(companyId);
        gatingLog.setProjectId(projectId);
        gatingLog.setToBuildingCode(toBuildingCode);
        gatingLog.setFromBuildingCode(fromBuildingCode);
        gatingLog.setGatingCode(gatingCode);
        gatingLog.setGatingAccount(gatingAccount);
        gatingLog.setType(type);
        try {
            gatingLog.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime));
        } catch (ParseException e) {
            logger.info("传入日期字符串格式错误!");
        }
        int count=gatingLogExtraMapper.insertLog(gatingLog);
        if(count>0){
            logger.debug("加入门控机日志成功！");
            gatingLog.setLogId(logId);
            return new RemoteModelResult(gatingLog);
        }
        logger.debug("加入门控机日志失败！");
        return new RemoteModelResult(ReturnCode.API_RESOLVE_FAIL);
    }

    @Override
    public RemoteModelResult updateStatus(String gatingCode,String version, String onlineState, String videosState) {
        HashMap<String,String> hashMap=new HashMap(4);
        hashMap.put("gatingCode",gatingCode);
        hashMap.put("version",version);
        hashMap.put("onlineState",onlineState);
        hashMap.put("videosState",videosState);
        int count=gatingLinPhoneMapper.updateStatus(hashMap);
        if(count>0){
            return new RemoteModelResult();
        }
        return new RemoteModelResult(ReturnCode.API_RESOLVE_FAIL);
    }

    @Override
    public LinphoneResult queryGatingListByBId(String buildingId) {
        return new LinphoneResult(gatingLinPhoneMapper.selectGatingList(buildingId));
    }

    @Override
    public RemoteModelResult queryLogStatistics(String companyId, String mkAccountName,int pageNo,int pageSize) {
        int limit = (pageNo-1)*pageSize;
        return new RemoteModelResult(mkjLogMapper.queryLogStatistics(companyId,mkAccountName,limit,pageSize));
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto queryLogsByObj(MkjLog log) {
		
		Page page = log.getPage();
		int pageNo = page.getCurrentPage();
		int pageSize = page.getShowCount();
		int limit = (page.getCurrentPage() <= 0) ? 0 : (pageNo - 1) * pageSize;
		int totalCount = mkjLogMapper.countLogsByObj(log); 
		int totalPages = (totalCount % pageSize == 0) ?  ( totalCount / pageSize ) : (totalCount / pageSize) + 1;
		page.setTotalResult(totalCount);
		page.setTotalPage(totalPages);
		return new BaseDto(mkjLogMapper.queryLogsByObj(log.getCompanyId(),log.getProjectId(),log.getGatingAccount(),log.getStartTime(),log.getEndTime(),limit,pageSize),log.getPage()); 
	}

    @Override
    public RemoteModelResult queryMkLogData(String companyId, String gatingName, String logDate) {
        return new RemoteModelResult(mkjLogMapper.queryMkLogData(companyId,gatingName,logDate));
    }

    @Override
    public RemoteModelResult checkUpdate(String version, String type) {
        logger.info("开始查询版本更新信息!");
        AppPkgDto appPkgDto=appPkgExtraMapper.selectByType(type);
        if(appPkgDto==null){
            logger.info("未查询到type:{}对应的版本信息",type);
            appPkgDto=new AppPkgDto();
            appPkgDto.setStatus("0");
        }else {
            String currentVersion=appPkgDto.getCurrVersion();
            if(version.equals(currentVersion)){//设置是否需要更新
                appPkgDto.setStatus("0");
            }else {
                appPkgDto.setStatus("1");
            }
        }

        appPkgDto.setPackageUrl(linphonePkgUrlPreffix+appPkgDto.getPackageUrl());//拼接APK下载地址
        logger.debug("查询到版本信息:{}", JSON.toJSONString(appPkgDto));
        return new RemoteModelResult(appPkgDto);
    }

    @Override
    public String getCompanyByBuildingId(String buildingId) {
        return tcBuildingMapper.getCompanyByBuildingId(buildingId);
    }




}
