package com.everwing.server.wy.web.controller.sys;/**
 * Created by wust on 2017/4/10.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.RC4;
import com.everwing.coreservice.common.wy.common.UserContextModel;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.system.menu.TSysMenu;
import com.everwing.coreservice.common.wy.entity.system.resource.TSysResource;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.wy.api.sys.TSysUserApi;
import com.everwing.server.wy.util.WyJwtHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 * Function:登录控制器
 * Reason:物业前端登录注销控制器
 * Date:2017-4-10 09:44:23
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
@Controller
@RequestMapping("/system/LoginController")
public class LoginController {

    static Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private TSysUserApi tSysUserApi;

    @Autowired
    private AccountApi accountApi;

    @Autowired
    private SpringRedisTools springRedisTools;


    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    public static final int cacheUserTimeOut =  60 * 12; // 缓存用户信息12个小时;

    private static final String JSONOBJECT_KEY_USER_ID = "userId";

    private static final String JSONOBJECT_KEY_LOGIN_NAME = "loginName";

    private static final String JSONOBJECT_KEY_STAFF_NUMBER = "staffNumber";

    private static final String JSONOBJECT_KEY_STAFF_NAME = "staffName";

    private static final String JSONOBJECT_KEY_PERMISSIONS = "permissions";

    private static final String JSONOBJECT_KEY_FIRSTMENUS = "firstMenus";

    private static final String JSONOBJECT_KEY_ROOT_COMPANY_ID = "rootCompanyId";

    private static final String JSONOBJECT_KEY_ROOT_COMPANY_NAME = "rootCompanyName";

    private static final String JSONOBJECT_KEY_JSON_WEB_TOKEN = "jsonWebToken";

    private static final String ENCRY_KEY = "jZ5$x!6yeAo1Qe^r_";

    private static final String CHECK_CODE_PREFIX = "XH_WY_LOGIN_QR_CODE";


    /**
     * 生成登录二维码校验字符
     * @param clientId
     * @return
     */
    @RequestMapping(value = "/genQRCodeId/{clientId}",method = RequestMethod.POST)
    public @ResponseBody MessageMap genQRCodeId(@PathVariable String clientId) {
        MessageMap mm = new MessageMap();
        String checkCode = CHECK_CODE_PREFIX + "." + UUID.randomUUID().toString() + "." + clientId;
        logger.info("checkCode：{}",checkCode);
        String encodeUUID = RC4.encry_RC4_string(checkCode, ENCRY_KEY);
        mm.setObj(encodeUUID);
        springRedisTools.addData(encodeUUID,"",1L, TimeUnit.MINUTES);
        return mm;
    }


    /**
     * 手机扫码后发送登录请求的信号
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/sendLoginSignalFromApp",method = RequestMethod.POST)
    public @ResponseBody JSONObject sendLoginSignalFromApp(@RequestBody JSONObject jsonObject) {
        JSONObject resultJSONObject = new JSONObject();
        resultJSONObject.put("code","000000");

        String checkCode = CommonUtils.null2String(jsonObject.getString("data"));

        if(StringUtils.isBlank(checkCode)){
            resultJSONObject.put("code","100004");
            resultJSONObject.put("message","检查码不能为空");
            return resultJSONObject;
        }

        String realCheckCodeStr = RC4.decry_RC4(checkCode,ENCRY_KEY);

        String[] checkCodes = realCheckCodeStr.split("\\.");
        if(checkCodes.length != 4){
            if(logger.isInfoEnabled()){
                logger.info("非法的检查码：{}，{}",checkCode,realCheckCodeStr);
            }

            resultJSONObject.put("code","100002");
            resultJSONObject.put("message","无法识别扫码来源");
            return resultJSONObject;
        }

        String prefix = checkCodes[0];
        String uuid = checkCodes[1];
        String clientId = checkCodes[2];
        String loginName = checkCodes[3];

        String redisKey =  RC4.encry_RC4_string(prefix + "." + uuid  + "." +  clientId,ENCRY_KEY);
        Object obj = springRedisTools.getByKey(redisKey);
        if(obj == null){
            resultJSONObject.put("code","100001");
            resultJSONObject.put("message","每个二维码只能使用一次且有效期限为1分钟");
            messagingTemplate.convertAndSend("/topic/scanCode/" + clientId,resultJSONObject);
            return resultJSONObject;
        }
        springRedisTools.deleteByKey(redisKey);


        if(!CHECK_CODE_PREFIX.equals(prefix)){
            if(logger.isInfoEnabled()){
                logger.info("非法的检查码：{}，{}",checkCode,realCheckCodeStr);
            }

            resultJSONObject.put("code","100002");
            resultJSONObject.put("message","无法识别扫码来源");
            return resultJSONObject;
        }

        RemoteModelResult<Account> remoteModelResult = this.accountApi.queryByAccountName(loginName, Dict.ACCOUNT_TYPE_STAFF.getIntValue());
        if(remoteModelResult.isSuccess()){
            Account account = remoteModelResult.getModel();
            if(account == null){
                resultJSONObject.put("code","101001");
                resultJSONObject.put("message","无效的登录账号【"+loginName+"】");
                messagingTemplate.convertAndSend("/topic/scanCode/"+clientId,resultJSONObject);
                return resultJSONObject;
            }
        }else{
            resultJSONObject.put("code","100005");
            resultJSONObject.put("message","后台出了点状况，请联系管理员");
            messagingTemplate.convertAndSend("/topic/scanCode/"+clientId,resultJSONObject);
            return resultJSONObject;
        }

        resultJSONObject.put("checkCode",checkCode);
        messagingTemplate.convertAndSend("/topic/scanCode/"+clientId,resultJSONObject);
        return resultJSONObject;
    }


    /**
     * 物业系统二维码登录入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/loginForScanCode",method = RequestMethod.POST)
    public @ResponseBody BaseDto loginForScanCode(@RequestBody JSONObject jsonObject) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        String checkCode = CommonUtils.null2String(jsonObject.getString("checkCode"));
        if(StringUtils.isBlank(checkCode)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("登录账号不能为空");
            baseDto.setMessageMap(mm);
        }else{
            String realCheckCodeStr = RC4.decry_RC4(checkCode,ENCRY_KEY);
            String[] checkCodes = realCheckCodeStr.split("\\.");
            String loginName = checkCodes[3];
            RemoteModelResult<Account> remoteModelResult = this.accountApi.queryByAccountName(loginName, Dict.ACCOUNT_TYPE_STAFF.getIntValue());
            if(remoteModelResult.isSuccess()){
                Account account = remoteModelResult.getModel();
                String password = account.getPassword();
                baseDto = this.doLogin(loginName,password);
            }
        }
        return baseDto;
    }

    /**
     * 物业系统账号密码登录入口
     * @param tSysUserSearch
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Common,businessName="登录物业平台",operationType= OperationEnum.Login)
    @RequestMapping(value = "/loginForAccount",method = RequestMethod.POST)
    public @ResponseBody BaseDto loginForAccount(@RequestBody  TSysUserSearch tSysUserSearch){
        BaseDto baseDto = new BaseDto();

        MessageMap mm = new MessageMap(MessageMap.INFOR_SUCCESS,"");

        if(logger.isInfoEnabled()){
            logger.info("########################物业WEB端登录开始###########################");
        }

        String loginName = tSysUserSearch.getLoginName();
        String password = tSysUserSearch.getPassword();

        if(StringUtils.isBlank(CommonUtils.null2String(loginName))){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("请输入登录账号");
            baseDto.setMessageMap(mm);
            if(logger.isInfoEnabled()){
                logger.info("#################登录账号为空");
            }
        }else if(StringUtils.isBlank(CommonUtils.null2String(password))){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("请输入登录密码");
            baseDto.setMessageMap(mm);
            if(logger.isInfoEnabled()){
                logger.info("#################登录密码为空");
            }
        }else{
            baseDto = this.doLogin(loginName,Base64.encodeBase64String(RC4.encry_RC4_string(password, Constants.ENCRY_KEY).getBytes()));
        }
        return baseDto;
    }





    /**
     * 登出
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Common,businessName="登出物业平台",operationType= OperationEnum.Logout)
    @RequestMapping(value = "/loginOut",method =RequestMethod.POST)
    public @ResponseBody  MessageMap  loginOut(){
        MessageMap mm = new MessageMap();
        String loginName = WyBusinessContext.getContext().getLoginName();
        if(StringUtils.isNotEmpty(loginName)){
            String key  = String.format(ApplicationConstant.WY_WEB_LOGIN_KEY.getStringValue(),loginName);
            springRedisTools.deleteByKey(key);
        }
        return mm;
    }





    /**
     * 根据菜单ID去缓存获取其子菜单
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/findMenuByLoginNameAndPid/{menuId}",method =RequestMethod.POST)
    public @ResponseBody  BaseDto  findMenuByLoginNameAndPid(@PathVariable String menuId){
        BaseDto baseDto = new BaseDto();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        String key  = String.format(ApplicationConstant.WY_WEB_LOGIN_KEY.getStringValue(),ctx.getLoginName());
        MessageMap mm = new MessageMap(MessageMap.INFOR_SUCCESS,"");
        if(StringUtils.isBlank(CommonUtils.null2String(ctx.getLoginName()))){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("请先登录");
        }else{
            UserContextModel redisUserContext = springRedisTools.getByKey(key) == null ? null : (UserContextModel)springRedisTools.getByKey(key);
            if(redisUserContext == null){
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("请先登录");
            }else{
                Map<String,List<TSysMenu>> map = redisUserContext.getGroupMenusByPid();
                List<TSysMenu> menuList = map.get(menuId);
                baseDto.setLstDto(menuList);
            }
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 执行登录操作
     * @param loginName
     * @param password
     * @return
     */
    private BaseDto doLogin(String loginName,String password){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap(MessageMap.INFOR_SUCCESS,"");

        String key  = String.format(ApplicationConstant.WY_WEB_LOGIN_KEY.getStringValue(),loginName);

        UserContextModel userContext = springRedisTools.getByKey(key) == null ? null : (UserContextModel)springRedisTools.getByKey(key);
        if(userContext == null) {
            // 从数据库获取
            if(logger.isInfoEnabled()){
                logger.info("#################从缓存中获取用户[{}]的登录信息为空，继续从数据库获取用户信息",key);
            }

            RemoteModelResult<MessageMap> remoteModelResult1 = tSysUserApi.login(loginName, password);
            if (remoteModelResult1.isSuccess()) {
                MessageMap messageMap = remoteModelResult1.getModel();
                if(!MessageMap.INFOR_SUCCESS.equals(messageMap.getFlag())){
                    mm = messageMap;
                }else{
                    userContext = messageMap.getObj() == null ? null : (UserContextModel) messageMap.getObj();

                    JSONObject jsonObject = new JSONObject();
                    simplifyUserContext(userContext,jsonObject);
                    genJsonWebToken(jsonObject,userContext.getLoginUser().getLoginName());
                    baseDto.setT(jsonObject);

                    springRedisTools.addData(key, userContext);

                    if(logger.isInfoEnabled()){
                        logger.info("登录成功[{}]",key);
                    }
                }
            } else {
                mm.setFlag(MessageMap.INFOR_ERROR);
                mm.setMessage(remoteModelResult1.getMsg());

                if(logger.isInfoEnabled()){
                    logger.info("登录失败[{}],{}",key,remoteModelResult1.getMsg());
                }
            }
        }else{
            // 从缓存获取

            if(StringUtils.isBlank(password)){
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("请输入密码");

                if(logger.isInfoEnabled()){
                    logger.info("#################从缓存中获取到用户[{}]的登录信息，但登录密码错误",key);
                }
            }else{
                if(password.equals(userContext.getLoginUser().getPassword())){
                    JSONObject jsonObject = new JSONObject();
                    simplifyUserContext(userContext,jsonObject);
                    genJsonWebToken(jsonObject,userContext.getLoginUser().getLoginName());
                    baseDto.setT(jsonObject);

                    if(logger.isInfoEnabled()){
                        logger.info("#################从缓存中获取到用户[{}]的登录信息，刷新缓存时间",key);
                    }
                }else{
                    mm.setFlag(MessageMap.INFOR_WARNING);
                    mm.setMessage("账号或密码错误");

                    if(logger.isInfoEnabled()){
                        logger.info("#################从缓存中获取到用户[{}]的登录信息，请输入登录密码",key);
                    }
                }
            }
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 生成登录token
     * @param jsonObject
     * @param loginName
     */
    private void genJsonWebToken(JSONObject jsonObject, String loginName) {
        String token = null;
        try {
            token = WyJwtHelper.getInstance().createWyLoginToken(loginName,cacheUserTimeOut );
        } catch (Exception e) {
           logger.error("产生token失败："+e);
        }
        jsonObject.put(JSONOBJECT_KEY_JSON_WEB_TOKEN,token);
    }

    private void simplifyUserContext(UserContextModel userContext, JSONObject jsonObject){
        if(userContext != null){
            /**
             * 简化并设置资源权限
             */
            if(CollectionUtils.isNotEmpty(userContext.getResources())){
                List<TSysResource> permissions = userContext.getResources();
                List<String> simplePermissions = new ArrayList<>(userContext.getResources().size());
                for (TSysResource permission : permissions) {
                    simplePermissions.add(permission.getSrcPermission());
                }

                jsonObject.put(JSONOBJECT_KEY_PERMISSIONS,simplePermissions);  // 登录用户拥有的所有资源权限
            }

            /**
             * 简化并设置一级菜单
             */
            if(userContext.getGroupMenusByLevel() != null){
                if(CollectionUtils.isNotEmpty(userContext.getGroupMenusByLevel().get(1))){
                    List<TSysMenu> menus = userContext.getGroupMenusByLevel().get(1);
                    JSONArray jsonArray = new JSONArray(menus.size());
                    for (TSysMenu menu : menus) {
                        JSONObject menuJsonObject = new JSONObject();
                        menuJsonObject.put("menuId",menu.getMenuId());
                        menuJsonObject.put("menuName",menu.getMenuName());
                        menuJsonObject.put("menuDesc",menu.getMenuDesc());
                        menuJsonObject.put("menuImg",menu.getMenuImg());
                        menuJsonObject.put("menuUrl",menu.getMenuUrl());
                        menuJsonObject.put("menuLevel",menu.getMenuLevel());
                        menuJsonObject.put("menuPermission",menu.getMenuPermission());
                        jsonArray.add(menuJsonObject);
                    }

                    jsonObject.put(JSONOBJECT_KEY_FIRSTMENUS,jsonArray);
                }
            }
        }

        /**
         * 设置用户信息
         */
        jsonObject.put(JSONOBJECT_KEY_USER_ID,userContext.getLoginUser().getUserId());
        jsonObject.put(JSONOBJECT_KEY_LOGIN_NAME,userContext.getLoginUser().getLoginName());
        jsonObject.put(JSONOBJECT_KEY_STAFF_NUMBER,userContext.getLoginUser().getStaffNumber());
        jsonObject.put(JSONOBJECT_KEY_STAFF_NAME,userContext.getLoginUser().getStaffName());



        /**
         * 设置顶级公司
         */
        jsonObject.put(JSONOBJECT_KEY_ROOT_COMPANY_ID,userContext.getRootCompany().getCompanyId());
        jsonObject.put(JSONOBJECT_KEY_ROOT_COMPANY_NAME,userContext.getRootCompany().getName());
    }
}
