package com.everwing.coreservice.common.platform.service;

import com.everwing.coreservice.common.dto.LinphoneResult;

public interface LinPhoneService {
    /**
     * 检测升级包
     * @param version 版本号
     * @param type 0=IOS邻音 1=Android邻音 2=门控机 3=非重庆室内机 4=物业APP 5=重庆室内机
     * @return 邻音通用返回结果
     */
    LinphoneResult queryUpdate(String version, String type);

    /**
     * 开门密码修改
     * @param buildingCode 资产对应编码
     * @param password 新的开门密码
     * @return 邻音通用返回结果
     */
    LinphoneResult updateDoorPassword(String buildingCode, String password);

}
