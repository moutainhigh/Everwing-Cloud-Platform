/**
 * Created by wust on 2018/1/31.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.context.DynamicreportsBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRoleVO;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUser;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUserQO;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUserVO;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.dynamicreports.api.system.UserApi;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/31
 * @author wusongti@lii.com.cn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/spring-application-context.xml","classpath*:/spring/spring-mvc.xml"})
public class UserControllerTest {
    @Autowired
    private UserApi userApi;

    @Test
    public void testListPage(){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        Page page = new Page();
        page.setShowCount(10);
        page.setCurrentPage(1);

        TRightsUserQO tRightsUserQO = new TRightsUserQO();
        tRightsUserQO.setPage(page);
        RemoteModelResult<BaseDto> remoteModelResult =  userApi.listPage(tRightsUserQO);
        if(remoteModelResult.isSuccess()){
            baseDto = remoteModelResult.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
            baseDto.setMessageMap(mm);
        }
        System.out.println("###################结果："+baseDto.toString());
    }

    @Test
    public void testFindByCondition(){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        TRightsUserQO tRightsUserQO = new TRightsUserQO();
        RemoteModelResult<BaseDto> remoteModelResult =  userApi.findByCondition(tRightsUserQO);
        if(remoteModelResult.isSuccess()){
            baseDto = remoteModelResult.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
            baseDto.setMessageMap(mm);
        }
        System.out.println("###################结果："+baseDto.toString());
    }

    @Test
    public void testInsert(){
        MessageMap mm = new MessageMap();
        DynamicreportsBusinessContext ctx = DynamicreportsBusinessContext.getContext();

        String code = "";
        int i = 0;
        boolean flag = true;
        do{
            i ++;
            code = "1" + StringUtils.leftPad(i + "",6,"0");
            TRightsUserQO tRightsUserQO = new TRightsUserQO();
            tRightsUserQO.setStaffNumber(code);
            RemoteModelResult<BaseDto> remoteModelResult =  userApi.findByCondition(tRightsUserQO);
            if(remoteModelResult.isSuccess()){
                BaseDto baseDto = remoteModelResult.getModel();
                List<TRightsRoleVO> list = baseDto.getLstDto();
                if(CollectionUtils.isNotEmpty(list)){
                    flag = true;
                }else{
                    flag = false;
                }
            }else{
                mm.setFlag(MessageMap.INFOR_ERROR);
                mm.setMessage(remoteModelResult.getMsg());
                return;
            }
        }while (flag);

        TRightsUser tRightsUser = new TRightsUser();
        tRightsUser.setStaffNumber(code);
        tRightsUser.setLoginName(code);
        tRightsUser.setType(LookupItemEnum.staffType_staff.getStringValue());
        tRightsUser.setStatus(LookupItemEnum.enableDisable_enable.getStringValue());
        tRightsUser.setCreaterId(ctx.getUserId());
        tRightsUser.setCreaterName(ctx.getStaffName());
        RemoteModelResult<MessageMap> remoteModelResult =  userApi.save(tRightsUser);
        if(remoteModelResult.isSuccess()){
            mm = remoteModelResult.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }
        System.out.println("###################结果："+mm.toString());
    }

    @Test
    public void testModify(){
        MessageMap mm = new MessageMap();
        DynamicreportsBusinessContext ctx = DynamicreportsBusinessContext.getContext();
        String userId = "55caa40c-06f7-11e8-80c8-0050568e00c5";

        TRightsUserQO tRightsUserQO = new TRightsUserQO();
        tRightsUserQO.setUserId(userId);
        RemoteModelResult<BaseDto> remoteModelResult =  userApi.findByCondition(tRightsUserQO);
        if(remoteModelResult.isSuccess()){
            BaseDto baseDto = remoteModelResult.getModel();
            List<TRightsUserVO> list = baseDto.getLstDto();
            if(CollectionUtils.isNotEmpty(list)){
                TRightsUserVO tRightsUserVO = list.get(0);
                tRightsUserVO.setStaffName(LookupItemEnum.staffType_systemAdmin.getStringValue());
                tRightsUserVO.setModifyId(ctx.getUserId());
                tRightsUserVO.setModifyName(ctx.getStaffName());
                RemoteModelResult<MessageMap> remoteModelResult1 =  userApi.save(tRightsUserVO);
                if(remoteModelResult1.isSuccess()){
                    mm = remoteModelResult1.getModel();
                }else{
                    mm.setFlag(MessageMap.INFOR_ERROR);
                    mm.setMessage(remoteModelResult1.getMsg());
                }
            }else{
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("无此记录");
            }
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }
        System.out.println("###################结果："+mm.toString());
    }

    @Test
    public void testEnable(){
        MessageMap mm = new MessageMap();
        DynamicreportsBusinessContext ctx = DynamicreportsBusinessContext.getContext();
        String userId = "a55d9e8c-06f7-11e8-80c8-0050568e00c5";

        TRightsUserQO tRightsUserQO = new TRightsUserQO();
        tRightsUserQO.setUserId(userId);
        RemoteModelResult<BaseDto> remoteModelResult =  userApi.findByCondition(tRightsUserQO);
        if(remoteModelResult.isSuccess()){
            BaseDto baseDto = remoteModelResult.getModel();
            List<TRightsUserVO> list = baseDto.getLstDto();
            if(CollectionUtils.isNotEmpty(list)){
                TRightsUserVO tRightsUserVO = list.get(0);
                tRightsUserVO.setStatus(LookupItemEnum.enableDisable_enable.getStringValue());
                tRightsUserVO.setModifyId(ctx.getUserId());
                tRightsUserVO.setModifyName(ctx.getStaffName());
                RemoteModelResult<MessageMap> remoteModelResult1 =  userApi.save(tRightsUserVO);
                if(remoteModelResult1.isSuccess()){
                    mm = remoteModelResult1.getModel();
                }else{
                    mm.setFlag(MessageMap.INFOR_ERROR);
                    mm.setMessage(remoteModelResult1.getMsg());
                }
            }else{
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("无此记录");
            }
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }
        System.out.println("###################结果："+mm.toString());
    }

    @Test
    public void testDisable(){
        MessageMap mm = new MessageMap();
        DynamicreportsBusinessContext ctx = DynamicreportsBusinessContext.getContext();
        String userId = "a55d9e8c-06f7-11e8-80c8-0050568e00c5";

        TRightsUserQO tRightsUserQO = new TRightsUserQO();
        tRightsUserQO.setUserId(userId);
        RemoteModelResult<BaseDto> remoteModelResult =  userApi.findByCondition(tRightsUserQO);
        if(remoteModelResult.isSuccess()){
            BaseDto baseDto = remoteModelResult.getModel();
            List<TRightsUserVO> list = baseDto.getLstDto();
            if(CollectionUtils.isNotEmpty(list)){
                TRightsUserVO tRightsUserVO = list.get(0);
                tRightsUserVO.setStatus(LookupItemEnum.enableDisable_disable.getStringValue());
                tRightsUserVO.setModifyId(ctx.getUserId());
                tRightsUserVO.setModifyName(ctx.getStaffName());
                RemoteModelResult<MessageMap> remoteModelResult1 =  userApi.save(tRightsUserVO);
                if(remoteModelResult1.isSuccess()){
                    mm = remoteModelResult1.getModel();
                }else{
                    mm.setFlag(MessageMap.INFOR_ERROR);
                    mm.setMessage(remoteModelResult1.getMsg());
                }
            }else{
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("无此记录");
            }
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }
        System.out.println("###################结果："+mm.toString());
    }

    @Test
    public void testDelete(){
        MessageMap mm = new MessageMap();
        DynamicreportsBusinessContext ctx = DynamicreportsBusinessContext.getContext();
        String id = "a55d9e8c-06f7-11e8-80c8-0050568e00c5";
        RemoteModelResult<MessageMap> remoteModelResult =  userApi.delete(id);
        if(remoteModelResult.isSuccess()){
            mm = remoteModelResult.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }
        System.out.println("###################结果："+mm.toString());
    }
}
