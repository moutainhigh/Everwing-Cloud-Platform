package com.everwing.server.gating.controller.gating;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.wy.api.gating.GatingApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 白名单controller
 *
 * @author DELL shiny
 * @create 2017/12/28
 */
@RestController
@RequestMapping("/gating/api/{version}")
public class WhitelistController {

    private static final Logger logger= LogManager.getLogger(WhitelistController.class);

    @Autowired
    private GatingApi gatingApi;

    @PostMapping("getWhitelist")
    public LinphoneResult getWhitelist(String companyId, String projectId, String gatingId){
        logger.debug("获取白名单列表开始");
        RemoteModelResult<List<Map<String,String>>> remoteModelResult=gatingApi.queryByCPG(companyId,projectId,gatingId);
        if(remoteModelResult.isSuccess()){
            logger.debug("获取白名单成功!");
            return new LinphoneResult(remoteModelResult.getModel());
        }
        logger.debug("获取白名单失败!");
        return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
    }
}
