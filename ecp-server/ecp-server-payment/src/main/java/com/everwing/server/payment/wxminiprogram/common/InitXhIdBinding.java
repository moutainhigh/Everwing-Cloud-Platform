package com.everwing.server.payment.wxminiprogram.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.everwing.cache.redis.SpringRedisTools;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;

/**
 * spring启动后初始化xhid绑定的数据
 * @author zhuge
 * @create 2018/6/5
 */
@Component
public class InitXhIdBinding implements InitializingBean {

    private static final Logger logger= LogManager.getLogger(InitXhIdBinding.class);

    @Value(value="classpath:config/xhid.json")
    private Resource xhidData;

    @Autowired
    @Qualifier("redisDataOperator")
    private SpringRedisTools redisTools;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("初始化xhId绑定。。。。。。。。。。。。。");
        String xhIdDataJson = IOUtils.toString(xhidData.getInputStream(), Charset.forName("UTF-8"));
        logger.debug("系统xhId绑定数据：{}",xhIdDataJson);
        JSONArray array = JSON.parseArray(xhIdDataJson);
        List <XhIdBinding> list = array.toJavaList(XhIdBinding.class);
        for(XhIdBinding x : list) {
            redisTools.deleteByKey(x.getXhId());
            redisTools.addData(x.getXhId(),x);
        }

    }
}
