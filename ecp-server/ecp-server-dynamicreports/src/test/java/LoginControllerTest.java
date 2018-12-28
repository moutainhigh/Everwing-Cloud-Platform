/**
 * Created by wust on 2018/1/31.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.dynamicreports.DynamicreportsEnum;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.dynamicreports.api.system.CommonApi;
import com.everwing.coreservice.dynamicreports.api.system.RoleApi;
import com.everwing.coreservice.dynamicreports.api.system.UserApi;
import com.everwing.server.dynamicreports.utils.DynamicreportsJwtUtil;
import com.everwing.utils.Validate;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/31
 * @author wusongti@lii.com.cn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/spring-application-context.xml","classpath*:/spring/spring-mvc.xml"})
public class LoginControllerTest {
    @Autowired
    private CommonApi commonApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private RoleApi roleApi;

    @Autowired
    private SpringRedisTools springRedisTools;

    @Test
    public void test(){
        MessageMap mm = new MessageMap();

        String loginName = "1000001";
        String password = "123456";

        Validate.isTrue(!StringUtils.isBlank(CommonUtils.null2String(loginName)),"请输入登录账号");

        Validate.isTrue(!StringUtils.isBlank(CommonUtils.null2String(password)),"请输入登录密码");

        if(StringUtils.isBlank(CommonUtils.null2String(loginName))){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("请输入登录账号");
            return;
        }

        if(StringUtils.isBlank(CommonUtils.null2String(password))){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("请输入登录密码");
            return;
        }

        RemoteModelResult<MessageMap> remoteModelResult = commonApi.login(loginName,password);
        if(remoteModelResult.isSuccess()){
            mm = remoteModelResult.getModel();
            if(MessageMap.INFOR_SUCCESS.equalsIgnoreCase(mm.getFlag())){
                Object obj = springRedisTools.getByKey(String.format(ApplicationConstant.REPORT_WEB_LOGIN_KEY.getStringValue(),loginName));
                if(obj != null){
                    JSONObject jsonObject = (JSONObject) obj;
                    setJsonWebToken(mm,jsonObject.getJSONObject(DynamicreportsEnum.USERCONTEXT_userInfo.getStringValue()));
                    System.out.println("###########################"+jsonObject);
                    System.out.println("erere="+mm.getObj());
                }
            }else{
                System.out.println("##################"+mm.getMessage());
            }
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
            return;
        }
    }

    private void setJsonWebToken(MessageMap mm, JSONObject jsonObject){
        try {
            String token = DynamicreportsJwtUtil.getInstance().createToken(jsonObject,0);
            mm.setObj(token);
        } catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("登录失败：" + e.getMessage());
        }
    }
}
