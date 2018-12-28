package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.platform.service.LinPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinPhoneApi {

    @Autowired
    private LinPhoneService linPhoneService;
    /**
     *检测升级包
     * @param version 当前门控机所使用的软件版本
     * @param type 版本类型
     * @return 邻音通用返回结果
     */
    @NoExceptionProxy
    public LinphoneResult checkUpdate(String version, String type) {
        return linPhoneService.queryUpdate(version,type);
    }

    /**
     * 开门密码修改
     * @param buildingCode 资产对应编码
     * @param password 新的开门密码
     * @return 邻音通用返回结果
     */
    @NoExceptionProxy
    public LinphoneResult modifyDoorPassword(String buildingCode, String password) {
        return  linPhoneService.updateDoorPassword(buildingCode,password);
    }

}
