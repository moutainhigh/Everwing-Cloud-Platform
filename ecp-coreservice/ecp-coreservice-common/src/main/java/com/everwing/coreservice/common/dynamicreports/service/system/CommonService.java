package com.everwing.coreservice.common.dynamicreports.service.system;/**
 * Created by wust on 2018/2/5.
 */

import com.everwing.coreservice.common.MessageMap;

/**
 *
 * Function:
 * Reason:
 * Date:2018/2/5
 * @author wusongti@lii.com.cn
 */
public interface CommonService {
    /**
     * 初始化系统菜单资源
     * @return
     */
    MessageMap initResources();

    /**
     * 登录
     * @param loginName
     * @param pwd
     * @return
     */
    MessageMap login(String loginName,String pwd);
}
