/**
 * Created by wust on 2018/1/31.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.context.DynamicreportsBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRole;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRoleQO;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRoleVO;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.dynamicreports.api.system.RoleApi;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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
public class RoleControllerTest {
    @Autowired
    private RoleApi roleApi;

    @Test
    public void testListPage(){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        DynamicreportsBusinessContext ctx = DynamicreportsBusinessContext.getContext();
        Page page = new Page();
        page.setShowCount(10);
        page.setCurrentPage(1);
        TRightsRoleQO tRightsRoleQO = new TRightsRoleQO();
        tRightsRoleQO.setPage(page);
        RemoteModelResult<BaseDto> remoteModelResult =  roleApi.listPage(tRightsRoleQO);
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
        DynamicreportsBusinessContext ctx = DynamicreportsBusinessContext.getContext();
        TRightsRoleQO tRightsRoleQO = new TRightsRoleQO();
        RemoteModelResult<BaseDto> remoteModelResult =  roleApi.findByCondition(tRightsRoleQO);
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
            code = "ROLE" + new DateTime().toString("yyyy") + StringUtils.leftPad(i + "",4,"0");
            TRightsRoleQO tRightsRoleQO = new TRightsRoleQO();
            tRightsRoleQO.setCode(code);
            RemoteModelResult<BaseDto> remoteModelResult =  roleApi.findByCondition(tRightsRoleQO);
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

        TRightsRole tRightsRole = new TRightsRole();
        tRightsRole.setCode(code);
        tRightsRole.setRoleName("岗位" + code);
        tRightsRole.setStatus("enable");
        tRightsRole.setCreaterId(ctx.getUserId());
        tRightsRole.setCreaterName(ctx.getStaffName());
        RemoteModelResult<MessageMap> remoteModelResult =  roleApi.save(tRightsRole);
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
        String roleId = "bca41b4b-06ef-11e8-80c8-0050568e00c5";

        TRightsRoleQO tRightsRoleQO = new TRightsRoleQO();
        tRightsRoleQO.setRoleId(roleId);
        RemoteModelResult<BaseDto> remoteModelResult =  roleApi.findByCondition(tRightsRoleQO);
        if(remoteModelResult.isSuccess()){
            BaseDto baseDto = remoteModelResult.getModel();
            List<TRightsRoleVO> list = baseDto.getLstDto();
            if(CollectionUtils.isNotEmpty(list)){
                TRightsRoleVO tRightsRoleVO = list.get(0);
                tRightsRoleVO.setRoleDesc(System.currentTimeMillis() + "");
                tRightsRoleVO.setModifyId(ctx.getUserId());
                tRightsRoleVO.setModifyName(ctx.getStaffName());
                RemoteModelResult<MessageMap> remoteModelResult1 =  roleApi.save(tRightsRoleVO);
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
        String roleId = "bca41b4b-06ef-11e8-80c8-0050568e00c5";

        TRightsRoleQO tRightsRoleQO = new TRightsRoleQO();
        tRightsRoleQO.setRoleId(roleId);
        RemoteModelResult<BaseDto> remoteModelResult =  roleApi.findByCondition(tRightsRoleQO);
        if(remoteModelResult.isSuccess()){
            BaseDto baseDto = remoteModelResult.getModel();
            List<TRightsRoleVO> list = baseDto.getLstDto();
            if(CollectionUtils.isNotEmpty(list)){
                TRightsRoleVO tRightsRoleVO = list.get(0);
                tRightsRoleVO.setStatus(LookupItemEnum.enableDisable_enable.getStringValue());
                tRightsRoleVO.setModifyId(ctx.getUserId());
                tRightsRoleVO.setModifyName(ctx.getStaffName());
                RemoteModelResult<MessageMap> remoteModelResult1 =  roleApi.save(tRightsRoleVO);
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
        String roleId = "bca41b4b-06ef-11e8-80c8-0050568e00c5";

        TRightsRoleQO tRightsRoleQO = new TRightsRoleQO();
        tRightsRoleQO.setRoleId(roleId);
        RemoteModelResult<BaseDto> remoteModelResult =  roleApi.findByCondition(tRightsRoleQO);
        if(remoteModelResult.isSuccess()){
            BaseDto baseDto = remoteModelResult.getModel();
            List<TRightsRoleVO> list = baseDto.getLstDto();
            if(CollectionUtils.isNotEmpty(list)){
                TRightsRoleVO tRightsRoleVO = list.get(0);
                tRightsRoleVO.setStatus(LookupItemEnum.enableDisable_disable.getStringValue());
                tRightsRoleVO.setModifyId(ctx.getUserId());
                tRightsRoleVO.setModifyName(ctx.getStaffName());
                RemoteModelResult<MessageMap> remoteModelResult1 =  roleApi.save(tRightsRoleVO);
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
        String id = "f720ac56-06ef-11e8-80c8-0050568e00c5";
        RemoteModelResult<MessageMap> remoteModelResult =  roleApi.delete(id);
        if(remoteModelResult.isSuccess()){
            mm = remoteModelResult.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }
        System.out.println("###################结果："+mm.toString());
    }
}
