package com.everwing.coreservice.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.platform.entity.extra.AppPkgDto;
import com.everwing.coreservice.common.platform.service.LinPhoneService;
import com.everwing.coreservice.platform.dao.mapper.extra.AppPkgExtraMapper;
import com.everwing.coreservice.platform.dao.mapper.extra.BuildingExtraMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LinPhoneServiceImpl implements LinPhoneService{

    private Logger logger= LogManager.getLogger(LinPhoneServiceImpl.class);

    @Value("${linphonePkgUrlPreffix}")
    private String linphonePkgUrlPreffix;

    @Autowired
    private BuildingExtraMapper buildingExtraMapper;

    @Autowired
    private AppPkgExtraMapper appPkgExtraMapper;

    @Override
    public LinphoneResult queryUpdate(String version, String type) {
        logger.info("开始查询版本更新信息!");
        AppPkgDto appPkgDto=appPkgExtraMapper.selectByType(type);
        if(appPkgDto==null){
            logger.info("未查询到type:{}对应的版本信息",type);
            appPkgDto=new AppPkgDto();
            appPkgDto.setStatus("0");
            return new LinphoneResult(appPkgDto);
        }
        String currentVersion=appPkgDto.getCurrVersion();
        if(version.equals(currentVersion)){//设置是否需要更新
            appPkgDto.setStatus("0");
        }else {
            appPkgDto.setStatus("1");
        }
        appPkgDto.setPackageUrl(linphonePkgUrlPreffix+appPkgDto.getPackageUrl());//拼接APK下载地址
        logger.debug("查询到版本信息:{}", JSON.toJSONString(appPkgDto));
        return new LinphoneResult(appPkgDto);
    }

    @Override
    public LinphoneResult updateDoorPassword(String buildingCode, String password) {
        int count=buildingExtraMapper.updatePwdByBuildingCode(buildingCode,password);
        if(count==1){
            return new LinphoneResult(ResponseCode.RESOLVE_SUCCESS);
        }
        return new LinphoneResult(ResponseCode.RESOLVE_FAIL);
    }

}
