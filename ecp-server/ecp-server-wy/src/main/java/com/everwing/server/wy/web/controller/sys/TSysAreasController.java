package com.everwing.server.wy.web.controller.sys;/**
 * Created by wust on 2018/3/20.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.entity.system.areas.TSysAreasList;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 * Function:城市数据
 * Reason:
 * Date:2018/3/20
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/system/TSysAreasController")
public class TSysAreasController {

    /**
     * 根据pid获取城市数据
     * @param pid
     * @return
     */
    @RequestMapping(value = "/getCityDatasByPid/{pid}",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto getCityDatasByPid(@PathVariable String pid){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        List<TSysAreasList> tSysAreasLists = DataDictionaryUtil.getAreasListByPid(pid);
        if(CollectionUtils.isNotEmpty(tSysAreasLists)){
            baseDto.setLstDto(tSysAreasLists);
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("没有找到记录");
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

}
