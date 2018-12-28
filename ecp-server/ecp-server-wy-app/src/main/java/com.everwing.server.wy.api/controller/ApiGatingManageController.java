package com.everwing.server.wy.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.LogQueryDTO;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.platform.entity.extra.AppPkgDto;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.utils.HttpUtils;
import com.everwing.coreservice.common.utils.SipUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.dto.GatingDTO;
import com.everwing.coreservice.common.wy.dto.GatingLogStatisticsDTO;
import com.everwing.coreservice.common.wy.dto.ProjectGatingDTO;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.platform.api.PlatformGatingApi;
import com.everwing.coreservice.wy.api.gating.GatingApi;
import com.everwing.server.wy.api.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物业app门控接口
 * Created by zhugeruifei on 17/8/11.
 */
@RestController
@RequestMapping("{version}/mk")
public class ApiGatingManageController {

    private static final Logger logger= LogManager.getLogger(ApiGatingManageController.class);

    @Autowired
    private AccountApi accountApi;

    @Autowired
    private GatingApi gatingApi;

    @Autowired
    private PlatformGatingApi platformGatingApi;

    @Autowired
    private SipUtils sipUtils;

    /**
     * 根据登录账号查询对应的门控机列表
     * @param request
     */
    @PostMapping("projectList")
    @ApiVersion(1.0)
    public BaseApiVo<List<ProjectListVo>> queryProjectGating(HttpServletRequest request) {
        String companyId = getCompanyId(request);
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }

        logger.debug("根据登录账号查询对应的门控机列表，请求参数:" +
                        "companyId={}", new Object[]{companyId});

        RemoteModelResult<List<ProjectGatingDTO>> result = gatingApi.queryProjectGating(companyId);
        logger.debug("根据登录账号查询对应的门控机列表,远程调用返回数据：{}",result);

        if (result.isSuccess()) {
            final BeanCopier copier = BeanCopier.create(ProjectGatingDTO.class,ProjectListVo.class,false);
            List<ProjectListVo> voList = new ArrayList<ProjectListVo>();
            ProjectListVo projectListVo;
            List<ProjectGatingDTO> list = result.getModel();
            for (ProjectGatingDTO projectGatingDTO : list) {
                projectListVo = new ProjectListVo();
                copier.copy(projectGatingDTO,projectListVo,null);
                voList.add(projectListVo);
            }
            returnApiVo.setData(voList);
        } else {
            logger.debug("根据登录账号查询对应的门控机列表,查询失败!");
        }

        returnApiVo.setCode(result.getCode());
        returnApiVo.setMessage(result.getMsg());
        logger.debug("根据登录账号查询对应的门控机列表，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }

    /**
     * 根据项目ID查询对应的门控机列表
     * @param request
     * @param projectId
     * @return
     */
    @PostMapping("listByProject")
    @ApiVersion(1.0)
    public BaseApiVo<List<GatingVo>> queryByProjectId(HttpServletRequest request,String projectId) {
        String companyId = getCompanyId(request);
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }

        logger.debug("根据项目ID查询对应的门控机列表，请求参数:" +
                "companyId={}，projectId={}", new Object[]{companyId,projectId});

        RemoteModelResult<List<GatingDTO>> result = gatingApi.queryByProjectId(companyId,projectId);
        logger.debug("根据项目ID查询对应的门控机列表,远程调用返回数据：{}",result);

        if (result.isSuccess()) {
            final BeanCopier copier = BeanCopier.create(GatingDTO.class, GatingVo.class, false);
            List<GatingVo> voList = new ArrayList<GatingVo>();
            GatingVo gatingVo;
            List<GatingDTO> list = result.getModel();
            for (GatingDTO gatingDTO : list) {
                gatingVo = new GatingVo();
                copier.copy(gatingDTO,gatingVo,null);
                voList.add(gatingVo);
            }
            returnApiVo.setData(voList);
        } else {
            logger.debug("根据项目ID查询对应的门控机列表,查询失败!");
        }

        returnApiVo.setCode(result.getCode());
        returnApiVo.setMessage(result.getMsg());
        logger.debug("根据项目ID查询对应的门控机列表，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }

    /**
     * 根据账号查询门控机登录密码
     * @param request
     */
    @PostMapping("getPassword")
    @ApiVersion(1.0)
    public BaseApiVo<Map<String,String>> queryPassword(HttpServletRequest request,String mkAccountName) {
        BaseApiVo returnApiVo  = new BaseApiVo();
        logger.debug("根据账号查询门控机登录密码，请求参数:" +
                "mkAccountName={}", new Object[]{mkAccountName});

        RemoteModelResult<Account> accountRemoteModelResult = accountApi.queryByAccountName(mkAccountName,3);
        logger.debug("根据账号查询门控机登录密码,远程调用返回数据：{}",accountRemoteModelResult);

        if (accountRemoteModelResult.isSuccess()) {
            Account mkAccount = accountRemoteModelResult.getModel();
            Map<String,String> map = new HashMap<>();
            map.put("password",mkAccount.getPassword());
            returnApiVo.setData(map);
        }else {
            logger.debug("根据账号查询门控机登录密码,查询失败!");
        }
        returnApiVo.setCode(accountRemoteModelResult.getCode());
        returnApiVo.setMessage(accountRemoteModelResult.getMsg());
        logger.debug("根据账号查询门控机登录密码，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }

    /**
     * 根据账号统计门控机的开门次数
     * @param request
     * @param mkAccountName
     * @return
     */
    @PostMapping("LogStatistics")
    @ApiVersion(1.0)
    public BaseApiVo<List<GatingLogStatisticsVo>> queryLogStatistics(HttpServletRequest request,String mkAccountName,int pageNo,int pageSize) {
        String companyId = getCompanyId(request);
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }
        logger.debug("根据账号统计门控机的开门次数，请求参数:" +
                "companyId={}，mkAccountName={}", new Object[]{companyId,mkAccountName});

        RemoteModelResult<List<GatingLogStatisticsDTO>> statisticsResult = platformGatingApi.queryLogStatistics(companyId,mkAccountName,pageNo,pageSize);
        logger.debug("根据账号统计门控机的开门次数,远程调用返回数据：{}",statisticsResult);

        if (statisticsResult.isSuccess()) {
            List<GatingLogStatisticsDTO> list = statisticsResult.getModel();
            if (!list.isEmpty()) {
                List<GatingLogStatisticsVo> gatingLogStatisticsVoList = new ArrayList<>();
                GatingLogStatisticsVo gatingLogStatisticsVo;
                final BeanCopier copier = BeanCopier.create(GatingLogStatisticsDTO.class, GatingLogStatisticsVo.class, false);
                for (GatingLogStatisticsDTO gatingLogStatisticsDTO : list) {
                    gatingLogStatisticsVo = new GatingLogStatisticsVo();
                    copier.copy(gatingLogStatisticsDTO,gatingLogStatisticsVo,null);
                    gatingLogStatisticsVoList.add(gatingLogStatisticsVo);
                }
                returnApiVo.setData(gatingLogStatisticsVoList);
            }
        }else {
            logger.debug("根据账号查询门控机登录密码,查询失败!");
        }

        returnApiVo.setCode(statisticsResult.getCode());
        returnApiVo.setMessage(statisticsResult.getMsg());
        logger.debug("根据账号查询门控机登录密码，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }

    /**
     * 更新门控机视频状态
     * @param request
     * @param gatingCode
     * @param videosState
     * @return
     */
    @PostMapping("updateVideoStatus")
    @ApiVersion(1.0)
    public BaseApiVo updateVideoStatus(HttpServletRequest request,String gatingCode,String videosState) {
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        String companyId = account.getCompanyId();

        logger.debug("更新门控机视频状态，请求参数:" +
                "companyId={}，gatingCode={}，videosState={}", new Object[]{companyId,gatingCode,videosState});

        RemoteModelResult<BaseDto> baseDtoRemoteModelResult = gatingApi.updateStatus(companyId,gatingCode,null,videosState,null);
        logger.debug("更新门控机视频状态,远程调物业服务用返回数据：{}",baseDtoRemoteModelResult);

        if(baseDtoRemoteModelResult.isSuccess()){
            logger.debug("修改门控机视频状态写入物业成功!");
            Map<String,String> parameters=new HashMap<>(1);
            if("0".equals(videosState)){
                parameters.put("value","on");
            }else if("1".equals(videosState)){
                parameters.put("value","off");
            }
            sipUtils.sipGating(gatingCode,"enabledvideo",parameters);
            RemoteModelResult remoteModelResult = platformGatingApi.updateStatus(gatingCode,null,null,videosState);
            logger.debug("更新门控机视频状态,远程调平台服务用返回数据：{}",remoteModelResult);
            if(remoteModelResult.isSuccess()) {
                logger.info("打开或者关闭门控机视频同步到平台成功!");
            }else {
                logger.info("打开或者关闭门控机视频同步到平台失败!");
            }
        }else {
            logger.info("修改门控机视频状态写入物业失败!");
        }

        BaseApiVo returnApiVo  = new BaseApiVo();
        returnApiVo.setCode(baseDtoRemoteModelResult.getCode());
        returnApiVo.setMessage(baseDtoRemoteModelResult.getMsg());
        return returnApiVo;
    }

    /**
     * 检测是否有升级包
     * @param version
     * @param type
     * @return
     */
    @PostMapping("checkUpdate")
    @ApiVersion(1.0)
    public BaseApiVo<CheckUpdateVo> checkUpdate(String version,String type) {
        BaseApiVo returnApiVo = new BaseApiVo();
        logger.debug("检测是否有升级包，请求参数:" +
                "version={}，type={}", new Object[]{version,type});
        RemoteModelResult<AppPkgDto> reslut = platformGatingApi.checkUpdate(version,type);
        logger.debug("检测是否有升级包,远程调物业服务用返回数据：{}",reslut);
        CheckUpdateVo checkUpdateVo = null;
        if (reslut.isSuccess()) {
            checkUpdateVo = new CheckUpdateVo();
            final BeanCopier copier = BeanCopier.create(AppPkgDto.class, CheckUpdateVo.class, false);
            copier.copy(reslut.getModel(),checkUpdateVo,null);
        }else {
            logger.debug("检测是否有升级包,处理失败!");
        }
        returnApiVo.setCode(reslut.getCode());
        returnApiVo.setMessage(reslut.getMsg());
        returnApiVo.setData(checkUpdateVo);
        return returnApiVo;
    }

    /**
     * 查询门控机日志
     * @param request
     * @param mkAccountName
     * @param logDate
     * @return
     */
    @PostMapping("logQuery")
    @ApiVersion(1.0)
    public BaseApiVo<List> queryLogs(HttpServletRequest request,String mkAccountName,String logDate) {
        String companyId = getCompanyId(request);
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }

        logger.debug("检测是否有升级包，请求参数:" +
                "companyId={}，mkAccountName={}，logDate={}", new Object[]{companyId,mkAccountName,logDate});
        RemoteModelResult<LogQueryDTO> result = platformGatingApi.queryMkLogData(companyId,mkAccountName,logDate);
        returnApiVo.setCode(result.getCode());
        returnApiVo.setMessage(result.getMsg());
        returnApiVo.setData(result.getModel());
        return returnApiVo;
    }

    @PostMapping("mkjManager")
    @ApiVersion(1.0)
    public BaseApiVo mkjManager(String mkInstruct) {
        BaseApiVo returnApiVo  = new BaseApiVo();
        Map<String,String> resultMap = new HashMap<String,String>();
        try {
            logger.info("同步到sip服务器！");
            String result= HttpUtils.doPost(sipUtils.getBasePath()+"/remote/control", mkInstruct);
            JSONObject jsonObject=JSON.parseObject(result);
            String resultStr= (String) jsonObject.get("result");
            resultMap.put("sipReturn",resultStr);
            returnApiVo.setCode(ReturnCode.API_RESOLVE_SUCCESS.getCode());
        } catch (Exception e) {
            logger.error("同步到sip服务器异常",e.getMessage());
            returnApiVo.setCode(ReturnCode.SYSTEM_ERROR.getCode());
            returnApiVo.setMessage(ReturnCode.SYSTEM_ERROR.getDescription());
        }
        returnApiVo.setData(resultMap);
        return returnApiVo;
    }


    /**
     * 获取公司ID
     * @param request
     * @return
     */
    private String getCompanyId(HttpServletRequest request){
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        String companyId = account.getCompanyId();
        return companyId;
    }

}
