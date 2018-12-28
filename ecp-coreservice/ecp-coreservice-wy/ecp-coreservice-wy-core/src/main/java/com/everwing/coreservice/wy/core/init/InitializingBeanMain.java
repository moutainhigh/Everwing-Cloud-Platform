package com.everwing.coreservice.wy.core.init;/**
 * Created by wust on 2018/6/26.
 */

import com.everwing.coreservice.common.wy.initialization.AbstractInitializeData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Function:系统启动，初始化相关数据
 * Reason:
 * Date:2018/6/26
 *
 * @author wusongti@lii.com.cn
 */
@Component
public class InitializingBeanMain implements InitializingBean {
    @Autowired
    private AbstractInitializeData initializeBuildingData;

    @Autowired
    private AbstractInitializeData initializeLookupData;

    @Autowired
    private AbstractInitializeData initializeAreasData;

    @Autowired
    private AbstractInitializeData initializeDataSource;

    @Autowired
    private AbstractInitializeData initializeMenusResources;

    @Override
    public void afterPropertiesSet() throws Exception {
        initializeBuildingData.init();
        initializeLookupData.init();
        initializeAreasData.init();
        initializeDataSource.init();
        initializeMenusResources.init();
    }
}
